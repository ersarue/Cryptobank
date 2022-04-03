package nl.miwteam2.cryptomero.service;

import nl.miwteam2.cryptomero.domain.Address;
import nl.miwteam2.cryptomero.repository.AddressDao;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Petra Coenen
 */

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AddressServiceTest {

    private AddressService serviceUnderTest;
    private AddressDao daoMock = Mockito.mock(AddressDao.class);

    private Address addressZutphen = new Address(14, "Kerklaan", 3, "A", "1234AB", "Zutphen");
    private Address addressHaarlem = new Address(4, "Schachgelstraat", 5, "ZW", "3898CJ", "Haarlem");
    private Address addressTiel = new Address(6, "Mareplein", 45, "3489DH", "Tiel");
    private List<Address> allAddresses = new ArrayList<>();

    public AddressServiceTest() {
        super();
        this.serviceUnderTest = new AddressService(daoMock);
    }

    @BeforeAll
    public void setup() {
        Collections.addAll(allAddresses, addressZutphen, addressTiel);
        Mockito.when(daoMock.findById(14)).thenReturn(addressZutphen);
        Mockito.when(daoMock.findById(6)).thenReturn(addressTiel);
        Mockito.when(daoMock.getAll()).thenReturn(allAddresses);
    }

    @Test
    void testGetAddressById() {
        Address expected = new Address(14, "Kerklaan", 3, "A", "1234AB", "Zutphen");
        Address actual = serviceUnderTest.getAddressById(14);
        assertThat(actual).isNotNull().isEqualTo(expected);

        expected = new Address(6, "Mareplein", 45, "3489DH", "Tiel");
        actual = serviceUnderTest.getAddressById(6);
        assertThat(actual).isNotNull().isEqualTo(expected);
    }

    @Test
    void testGetAllAddresses() {
        Address address1 = new Address(14, "Kerklaan", 3, "A", "1234AB", "Zutphen");
        Address address2 = new Address(6, "Mareplein", 45, "3489DH", "Tiel");
        List<Address> expected = new ArrayList<>();
        Collections.addAll(expected, address1, address2);
        List<Address> actual = serviceUnderTest.getAllAddresses();
        assertThat(actual).isNotNull().isEqualTo(expected);
    }

    @Test
    void testCheckRequiredFields() {
        // Test address that has values for all required fields
        List<String> expected = new ArrayList<>();
        List<String> actual = serviceUnderTest.checkRequiredFields(addressTiel);
        assertThat(actual).isNotNull().isEqualTo(expected);

        // Test address with one empty required field (street name)
        Address addressDordrecht = new Address(null, 39, "4834HD", "Dordrecht");
        expected = new ArrayList<>();
        Collections.addAll(expected, "straatnaam");
        actual = serviceUnderTest.checkRequiredFields(addressDordrecht);
        assertThat(actual).isNotNull().isEqualTo(expected);

        // Test address with two empty required fields (street name and city)
        Address addressCoenplein = new Address("Coenplein", 2, null, null);
        expected = new ArrayList<>();
        Collections.addAll(expected, "postcode", "woonplaats");
        actual = serviceUnderTest.checkRequiredFields(addressCoenplein);
        assertThat(actual).isNotNull().isEqualTo(expected);

        // Test address with all required fields empty
        Address emptyAddress = new Address(null, 0, null, null);
        expected = new ArrayList<>();
        Collections.addAll(expected, "straatnaam", "huisnummer", "postcode", "woonplaats");
        actual = serviceUnderTest.checkRequiredFields(emptyAddress);
        assertThat(actual).isNotNull().isEqualTo(expected);
    }

    @Test
    void testCheckForUniqueAddress() {
        Address addressZutphen = new Address(14, "Kerklaan", 3, "A", "1234AB", "Zutphen");
        Address addressTiel = new Address(6, "Mareplein", 45, "3489DH", "Tiel");
        Address addressDoorn = new Address(3, "Dorpsstraat", 7, "3759JF", "Doorn");
        // Test addresses that already exist in the database (= not unique)
        assertThat(serviceUnderTest.checkForUniqueAddress(addressZutphen)).isNotNull().isEqualTo(14);
        assertThat(serviceUnderTest.checkForUniqueAddress(addressTiel)).isNotNull().isEqualTo(6);
        // Test address that does not yet exist in the database (= unique)
        assertThat(serviceUnderTest.checkForUniqueAddress(addressDoorn)).isNotNull().isEqualTo(-2);
    }

    @Test
    void testGetHouseString() {
        String expected = "3A";
        String actual = serviceUnderTest.getHouseString(addressZutphen);
        assertThat(actual).isNotNull().isEqualTo(expected);

        expected = "5ZW";
        actual = serviceUnderTest.getHouseString(addressHaarlem);
        assertThat(actual).isNotNull().isEqualTo(expected);

        expected = "45";
        actual = serviceUnderTest.getHouseString(addressTiel);
        assertThat(actual).isNotNull().isEqualTo(expected);
    }

    @DisplayName("testValidPostalCodeFormats")
    @ParameterizedTest
    @ValueSource(strings = {"1001AM","1001AM","8394VR","4317AE"})
    void isValidFormat(String postalCode) {
        assertTrue(serviceUnderTest.isValidFormat(postalCode));
    }

    @DisplayName("testInvalidPostalCodeFormats")
    @ParameterizedTest
    @ValueSource(strings = {"invalidPostalCode","3829 AB", "3284  KD","123KR","4317ERF","2214 D3","4291F9"})
    void isInvalidFormat(String postalCode) {
        assertFalse(serviceUnderTest.isValidFormat(postalCode));
    }
}