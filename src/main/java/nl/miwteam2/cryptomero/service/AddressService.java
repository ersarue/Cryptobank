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
 * @version 1.5
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
        try { // Check if postal code and house number match street name and city
            isValid = isValidAddress(address);
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        if (isValid) {
            jdbcAddressDao.storeOne(address);
            return address.getIdAddress();
        }
        else return 0;
    }

    /**
    * Retrieves a customer address from the database.
    * @param  id    Id value of the address to be retrieved from the database.
    * @return       The address corresponding to the given id value or 0 in case the given id matches no address.
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
        Map<String, Object> pdokMap = getStreetNameCity(address.getPostalCode(), address.getHouseNo());
        return (pdokMap.get("straatnaam").equals(address.getStreetName())
                && pdokMap.get("woonplaatsnaam").equals(address.getCity()));
    }

    /**
    * Retrieves the corresponding street name and city from the PDOK Locatieserver for a given postal code and
    * house number combination.
    * @param  postalCode    Postal code.
    * @param  houseNo       House number.
    * @return               A map containing street name and city corresponding to the postal code and house number combination.
    * @throws IOException           If an input or output exception occurred.
    * @throws InterruptedException  When the thread is interrupted before or during the activity.
    */
    public Map<String, Object> getStreetNameCity(String postalCode, int houseNo) throws IOException, InterruptedException {
        String url = createPdokUrl(postalCode, houseNo);
        HttpRequest request = createGetRequest(url);
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return processJsonAddress(response);
    }

    /**
    * Creates a url that can be used to query the PDOK Locatieserver for the street name and city belonging to a given
    * postal code and house number combination.
    * @param  postalCode    Postal code.
    * @param  houseNo       House number.
    * @return               A String url that can be used to query the PDOK Locatieserver.
    */
    public String createPdokUrl(String postalCode, int houseNo) {
        String BASE_URL_PDOK =
                "http://geodata.nationaalgeoregister.nl/locatieserver/v3/free?q=%s+and+%d&fl=straatnaam,woonplaatsnaam";
        return String.format(BASE_URL_PDOK, postalCode, houseNo);
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
    * Extracts street name and city from a PDOK Locatieserver JSON-response requested by a url generated by the
    * createPdokUrl() method.
    * @param  response    The HttpResponse to be processed
    * @return             A map containing street name and city
    * @throws JsonProcessingException   If a problem is encountered when processing (parsing, generating) JSON content that are not pure I/O problems.
    */
    public Map<String, Object> processJsonAddress(HttpResponse<String> response) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.body());
        JsonNode address = root.path("response").path("docs").get(0);
        return mapper.convertValue(address, new TypeReference<>() {});
    }
}
