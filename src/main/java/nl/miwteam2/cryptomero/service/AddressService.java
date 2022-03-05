package nl.miwteam2.cryptomero.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.miwteam2.cryptomero.domain.Address;
import nl.miwteam2.cryptomero.repository.JdbcAddressDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

/**
 * @author Petra Coenen
 * @version 1.6
 */

@Service
public class AddressService {

    private final JdbcAddressDao jdbcAddressDao;

    private final Logger logger = LoggerFactory.getLogger(AddressService.class);

    @Autowired
    public AddressService(JdbcAddressDao dao) {
        jdbcAddressDao = dao;
        logger.info("New AddressService");
    }

    /**
    * Stores a customer address in the database.
    * @param  address   The address to be stored.
    * @return           The auto-generated id from the database if the address is stored successfully, 0 otherwise.
    */
    public int storeAddress(Address address) {
        boolean isValid = false;
        try { // Check if postal code and house number combination match street name and city
            isValid = isValidAddress(address);
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        if (isValid) {
            return jdbcAddressDao.storeAddress(address);
        }
        else return 0;
    }

    /**
    * Retrieves a customer address from the database.
    * @param  id    Id value of the address to be retrieved from the database.
    * @return       ...
    */
    public Address getAddressById(int id) {
        return jdbcAddressDao.findById(id);
    }

    /**
    * Retrieves all customer addresses stored in the database.
    * @return        A list of all the customer addresses currently in the database.
    */
    public List<Address> getAllAddresses() {
        return jdbcAddressDao.getAll();
    }

    /**
    * Updates an existing customer address in the database.
    * @param  address   The updated address to be stored.
    * @return           1 in case the address is updated successfully, 0 otherwise.
    */
    public int updateAddress(Address address) { return jdbcAddressDao.updateOne(address); }

    /**
    * Deletes a customer address from the database.
    * @param  id        Id value of the address to be deleted.
    * @return           1 in case the address is deleted successfully, 0 otherwise.
    */
    public int deleteAddress(int id) { return jdbcAddressDao.deleteOne(id); }

    /**
    * Checks if a String conforms to the Dutch postal code format ('1234AB').
    * @param  postalCode    The postal code String to be checked.
    * @return               True if the String conforms to the Dutch postal code format.
    */
    public boolean isValidFormat(String postalCode) {
        return postalCode.matches("[1-9][0-9]{3}[a-zA-Z]{2}");
    }

    /**
    * Checks if a given postal code and house number combination corresponds to the given street name and city.
    * @param  address     The customer address to be checked.
    * @return             True if the given postal code and house number combination corresponds to street name and city.
    * @throws IOException           If an input or output exception occurred.
    * @throws InterruptedException  When the thread is interrupted before or during the activity.
    */
    public boolean isValidAddress(Address address) throws IOException, InterruptedException {
        Map<String, Object> pdokMap = getPdokStreetNameCity(address);
        if (pdokMap != null) {
            return (pdokMap.get("straatnaam").equals(address.getStreetName())
                    && pdokMap.get("woonplaatsnaam").equals(address.getCity()));
        }
        else return false;
    }

    /**
    * Retrieves the corresponding street name and city from the PDOK Locatieserver for a given postal code and
    * house number combination.
    * @param  address    The customer address to be checked.
    * @return            A map containing street name and city that correspond to postal code and house number.
    * @throws IOException           If an input or output exception occurred.
    * @throws InterruptedException  When the thread is interrupted before or during the activity.
    */
    public Map<String, Object> getPdokStreetNameCity(Address address) throws IOException, InterruptedException {
        String houseString = getHouseString(address);
        String url = createPdokUrl(address.getPostalCode(), houseString);
        HttpRequest request = createGetRequest(url);
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return processJsonAddress(response);
    }

    /**
    * Converts house number and addition (if present) from a customer address into a (single) String.
    * @param  address   The address containing the house number and addition to be converted.
    * @return           A String representation of the house number and (if present) addition.
    */
    public String getHouseString(Address address) {
        if (address.getHouseAdd() != null) {
            return address.getHouseNo() + address.getHouseAdd();
        }
        else return Integer.toString(address.getHouseNo());
    }

    /**
    * Creates a url that can be used to query the PDOK Locatieserver for the street name and city belonging to a given
    * postal code and house number combination.
    * @param  postalCode    Postal code.
    * @param  houseString   House number and (if present) addition.
    * @return               A String url that can be used to query the PDOK Locatieserver.
    */
    public String createPdokUrl(String postalCode, String houseString) {
        String BASE_URL_PDOK =
                "http://geodata.nationaalgeoregister.nl/locatieserver/v3/free?q=%s+and+%s&fl=straatnaam,woonplaatsnaam";
        return String.format(BASE_URL_PDOK, postalCode, houseString);
    }

    /**
    * Creates a HTTP GET request with an 'Accept: application/json' header.
    * @param  url    The url to be used in the request.
    * @return        A GET HttpRequest with an 'Accept: application/json' header.
    */
    public HttpRequest createGetRequest(String url) {
        return HttpRequest.newBuilder()
                .GET()
                .header("accept", "application/json")
                .uri(URI.create(url))
                .build();
    }

    /**
    * Extracts street name and city from a PDOK Locatieserver JSON response requested by a url generated by the
    * createPdokUrl() method.
    * @param  response    The HttpResponse to be processed.
    * @return             A map containing street name and city retrieved from the PDOK Locatieserver.
    * @throws JsonProcessingException   When a problem occurs during processing of JSON content.
    */
    public Map<String, Object> processJsonAddress(HttpResponse<String> response) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.body());
        JsonNode streetNameCity = root.path("response").path("docs").get(0);
        return mapper.convertValue(streetNameCity, new TypeReference<>() {});
    }
}
