package nl.miwteam2.cryptomero.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.miwteam2.cryptomero.domain.Asset;
import nl.miwteam2.cryptomero.repository.JdbcAssetDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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

    @Autowired
    public AssetService(JdbcAssetDao jdbcAssetDao) throws IOException, InterruptedException {
        this.jdbcAssetDao = jdbcAssetDao;
        //TODO dit in de constructor laten of eruit halen?
        updateAssetRates();
    }

    public void updateAssetRates() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = createGetRequest(URL);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(processJsonResponse(response));

    }

    public HttpRequest createGetRequest(String url) {
        return HttpRequest.newBuilder()
                .GET()
                .header("X-CMC_PRO_API_KEY", API_KEY)
                .uri(URI.create(url))
                .build();
    }

    public Map<String, Double> processJsonResponse(HttpResponse<String> response) throws JsonProcessingException {
        Map<String, Double> rates = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode data = mapper.readTree(response.body()).path("data");
        for (JsonNode coin : data) {
            rates.put(coin.get("name").toString(), coin.path("quote").path("EUR").get("price").doubleValue());
        }
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
