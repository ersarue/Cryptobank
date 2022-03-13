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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Stijn Klijn
 */

@Service
public class AssetService {

    private static final String URL = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest?convert=eur";
    private static final String API_KEY = "cb014412-0aa2-406c-972e-2c8c2841a732";

    private JdbcAssetDao jdbcAssetDao;
    private JdbcRateDao jdbcRateDao;
    private List<Asset> assets;
    private Map<String, Double> rates;

    @Autowired
    public AssetService(JdbcAssetDao jdbcAssetDao, JdbcRateDao jdbcRateDao) throws IOException, InterruptedException {
        this.jdbcAssetDao = jdbcAssetDao;
        this.jdbcRateDao = jdbcRateDao;
        this.assets = jdbcAssetDao.getAll();
        this.rates = new HashMap<>();
        //TODO onderstaande in de constructor laten of eruit halen?
        updateRates();
    }

    public void updateRates() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = createGetRequest(URL);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        processJsonResponse(response);

        for (Asset asset : assets) {
            Rate rate = new Rate(asset, LocalDateTime.now(), rates.get(asset.getAssetName()));
            jdbcRateDao.storeOne(rate);
        }
    }

    public HttpRequest createGetRequest(String url) {
        return HttpRequest.newBuilder()
                .GET()
                .header("X-CMC_PRO_API_KEY", API_KEY)
                .uri(URI.create(url))
                .build();
    }

    public void processJsonResponse(HttpResponse<String> response) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode data = mapper.readTree(response.body()).path("data");
        for (JsonNode coin : data) {
            rates.put(coin.get("name").toString().replace("\"", ""), coin.path("quote").path("EUR").get("price").doubleValue());
        }
    }

    public Map<String, Double> getRates() {
        return rates;
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
}
