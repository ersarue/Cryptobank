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
public class RateService {

    private static final String URL = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest?convert=eur";
    private static final String API_KEY = "cb014412-0aa2-406c-972e-2c8c2841a732";
    private static final long UPDATE_INTERVAL = 1000 * 3600 * 6; //Update interval in milliseconds (6 hours)

    private JdbcAssetDao jdbcAssetDao;
    private JdbcRateDao jdbcRateDao;
    private List<Asset> assets;
    private Map<String, Double> latestRates;

    @Autowired
    public RateService(JdbcAssetDao jdbcAssetDao, JdbcRateDao jdbcRateDao) {
        this.jdbcAssetDao = jdbcAssetDao;
        this.jdbcRateDao = jdbcRateDao;
        this.assets = jdbcAssetDao.getAll();
        this.latestRates = new HashMap<>();

        //Update all asset rates every given time interval, using an external API
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new UpdateRates(), 0, UPDATE_INTERVAL);
    }

    /**
     * Return latest rate for all assets
     * @return                      The latest rate for all assets
     */
    public List<Rate> getLatest() {
        return jdbcRateDao.getLatest();
    }

    /**
     * Return latest rate for the specified asset
     * @param name                  The specified asset
     * @return                      The latest rate for the specified asset
     */
    public Rate getLatestByName(String name) {
        return jdbcRateDao.getLatestByName(name);
    }

    /**
     * Return historical rates for the specified asset
     * This algorithm contains a nested for-loop and therefore will run in O(n^2) time
     * It is inefficient for large amounts of datapoints, which is therefore limited to 50
     * @param name                  The specified asset
     * @param interval              Requested time interval: "daypart", "day", "month", "quarter", "hyear" or "year"
     * @param numberOfDatapoints    Requested number of datapoints
     * @return                      TreeMap with the requested number of datapoints.
     *                              Key represents timepoint and value represents rate
     */
    public Map<LocalDateTime, Double> getHistory(String name, String interval, int numberOfDatapoints) {

        if (numberOfDatapoints <= 0 || numberOfDatapoints > 50) {
            throw new IllegalArgumentException("Number of datapoints must be between 1 and 50");
        }

        //Specify current time as endTimepoint and interval * numberOfDatapoints as startTimepoint.
        LocalDateTime endTimepoint = LocalDateTime.now();
        LocalDateTime startTimepoint = subtractInterval(endTimepoint, interval, numberOfDatapoints);

        //Retrieve all (unfiltered) rate information since startTimepoint from database
        List<Rate> unfilteredDatapoints =  jdbcRateDao.getHistory(name, startTimepoint);

        //Create TreeMap to store the filtered information
        Map<LocalDateTime, Double> filteredDatapoints = new TreeMap<>();

        //Define the limits of the current interval. We start with the most recent interval
        LocalDateTime nextTimepoint = endTimepoint;
        LocalDateTime previousTimepoint = subtractInterval(nextTimepoint, interval, 1);

        for (int i = numberOfDatapoints - 1; i >= 0; i--) {
            //Starting with the last datapoint, find the most recent rate information for the current interval
            Rate currentRate = null;
            for (Rate rate : unfilteredDatapoints) {
                if (rate.getTimepoint().isAfter(previousTimepoint) && rate.getTimepoint().isBefore(nextTimepoint)) {
                    currentRate = rate;
                }
            }

            //If information was found for this interval, it is stored. Then the current interval shifts one unit to the left
            filteredDatapoints.put(nextTimepoint, currentRate != null ? currentRate.getRate() : null);
            nextTimepoint = previousTimepoint;
            previousTimepoint = subtractInterval(previousTimepoint, interval, 1);
        }

        return filteredDatapoints;
    }

    /**
     * Subtract given time interval from the given timepoint
     * @param dateTime              The timepoint to subtract from
     * @param interval              The interval that must be subtracted: "daypart", "day", "month", "quarter", "hyear" or "year"
     * @param times                 The number of times the interval should be subtracted
     * @return                      The resulting timepoint
     */
    private LocalDateTime subtractInterval(LocalDateTime dateTime, String interval, int times) {
        switch (interval) {
            case "daypart": return dateTime.minusHours(6L * times);
            case "day": return dateTime.minusDays(times);
            case "month": return dateTime.minusMonths(times);
            case "quarter": return dateTime.minusMonths(3L * times);
            case "hyear": return dateTime.minusMonths(6L * times);
            case "year": return dateTime.minusYears(times);
            default: throw new IllegalArgumentException(String.format("Interval %s does not exist", interval));
        }
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
                storeLatestRate();
            } catch (InterruptedException | IOException e) {
                System.out.println("Failed to update asset rates: " + e.getMessage());
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
            //Process the JSON response: put the appropriate rates in the latestRates Map
            ObjectMapper mapper = new ObjectMapper();
            JsonNode data = mapper.readTree(response.body()).path("data");
            for (JsonNode coin : data) {
                latestRates.put(coin.get("name").toString().replace("\"", ""), coin.path("quote").path("EUR").get("price").doubleValue());
            }
        }

        private void storeLatestRate() {
            //For every asset, construct the latest rate, write it to the database and notify AssetService
            for (Asset asset : assets) {
                Rate rate = new Rate(asset, LocalDateTime.now(), latestRates.get(asset.getAssetName()));
                jdbcRateDao.storeOne(rate);
            }
        }
    }
}
