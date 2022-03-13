package nl.miwteam2.cryptomero.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.miwteam2.cryptomero.domain.Asset;
import nl.miwteam2.cryptomero.domain.Rate;
import nl.miwteam2.cryptomero.repository.JdbcAssetDao;
import nl.miwteam2.cryptomero.repository.JdbcRateDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Stijn Klijn
 */
@Service
public class AssetService {

    private static final String URL = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest?convert=eur";
    private static final String API_KEY = "cb014412-0aa2-406c-972e-2c8c2841a732";
    private static final long UPDATE_INTERVAL = 1000 * 3600 * 6; //Update interval in milliseconds (6 hours)

    private JdbcAssetDao jdbcAssetDao;
    private JdbcRateDao jdbcRateDao;
    private List<Asset> assets;
    private Map<String, Double> rates;

    @Autowired
    public AssetService(JdbcAssetDao jdbcAssetDao, JdbcRateDao jdbcRateDao) {
        this.jdbcAssetDao = jdbcAssetDao;
        this.jdbcRateDao = jdbcRateDao;
        this.assets = jdbcAssetDao.getAll();
        this.rates = new HashMap<>();

        //Update all asset rates every given time interval
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new UpdateRates(), 0, UPDATE_INTERVAL);
    }

    public List<Asset> getAll() {
        return jdbcAssetDao.getAll();
    }

    public Asset findByName(String name) {
        return jdbcAssetDao.findByName(name);
    }

    public void storeOne(Asset asset) {
        jdbcAssetDao.storeOne(asset);
    }

    public void updateOne(Asset asset) {
        jdbcAssetDao.updateOne(asset);
    }

    public void deleteOne(String name) {
        jdbcAssetDao.deleteOne(name);
    }

    private class UpdateRates extends TimerTask {

        @Override
        public void run() {
            try {
                //Setup client and request, receive response and process the response
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = createGetRequest(URL);
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                processJsonResponse(response);
            } catch (InterruptedException | IOException e) {
                System.out.println("Failed to update asset rates: " + e.getMessage());
            }

            //For every asset, construct the latest rate, write it to the database and update rate in asset model
            for (Asset asset : assets) {
                Rate rate = new Rate(asset, LocalDateTime.now(), rates.get(asset.getAssetName()));
                jdbcRateDao.storeOne(rate);
                asset.setRate(rate.getRate());
            }
        }

        private HttpRequest createGetRequest(String url) {
            //Build the HTTP request
            return HttpRequest.newBuilder()
                    .GET()
                    .header("X-CMC_PRO_API_KEY", API_KEY)
                    .uri(URI.create(url))
                    .build();
        }

        private void processJsonResponse(HttpResponse<String> response) throws JsonProcessingException {
            //Process the JSON response: put the appropriate rates in the rates Map
            ObjectMapper mapper = new ObjectMapper();
            JsonNode data = mapper.readTree(response.body()).path("data");
            for (JsonNode coin : data) {
                rates.put(coin.get("name").toString().replace("\"", ""), coin.path("quote").path("EUR").get("price").doubleValue());
            }
        }
    }
}
