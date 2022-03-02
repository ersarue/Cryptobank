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
 * @version 1.3
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

    public Address getAddressById(int id) {
        return jdbcAddressDao.findById(id);
    }

    public List<Address> getAllAddresses() {
        return jdbcAddressDao.getAll();
    }

    public int updateAddress(Address address) { return jdbcAddressDao.updateOne(address); }

    public int deleteAddress(int id) { return jdbcAddressDao.deleteOne(id); }

    public boolean isValidFormat(String postalCode) {
        return postalCode.matches("[1-9][0-9]{3}\\s?[a-zA-Z]{2}");
    }

    public boolean isValidAddress(Address address) throws IOException, InterruptedException {
        Map<String, Object> pdokMap = getStreetNameCity(address.getPostalCode(), address.getHouseNo());
        return (pdokMap.get("straatnaam").equals(address.getStreetName())
                && pdokMap.get("woonplaatsnaam").equals(address.getCity()));
    }

    public Map<String, Object> getStreetNameCity(String postalCode, int houseNo) throws IOException, InterruptedException {
        String url = createPdokUrl(postalCode, houseNo);
        HttpRequest request = createGetRequest(url);
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return processJsonAddress(response);
    }

    public String createPdokUrl(String postalCode, int houseNo) {
        String BASE_URL_PDOK =
                "http://geodata.nationaalgeoregister.nl/locatieserver/v3/free?q=%s+and+%d&fl=straatnaam,woonplaatsnaam";
        return String.format(BASE_URL_PDOK, postalCode, houseNo);
    }

    public HttpRequest createGetRequest(String url) {
        return HttpRequest.newBuilder()
                .GET()
                .header("accept", "application/json")
                .uri(URI.create(url))
                .build();
    }

    public Map<String, Object> processJsonAddress(HttpResponse<String> response) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.body());
        JsonNode address = root.path("response").path("docs").get(0);
        return mapper.convertValue(address, new TypeReference<>() {});
    }
}
