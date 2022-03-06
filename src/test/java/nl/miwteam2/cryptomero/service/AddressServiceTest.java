package nl.miwteam2.cryptomero.service;

import nl.miwteam2.cryptomero.domain.Address;
import nl.miwteam2.cryptomero.repository.JdbcAddressDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Contains unit tests for methods in the AddressService class.
 * @author Petra Coenen
 * @version 1.2
 */

class AddressServiceTest {

    private AddressService serviceUnderTest;
    private JdbcAddressDao daoMock = Mockito.mock(JdbcAddressDao.class);

    private Address addressZutphen = new Address(14, "Kerklaan", 3, "A", "1234AB", "Zutphen");
    private Address addressTiel = new Address(6, "Mareplein", 45, "3489DH", "Tiel");
    private List<Address> allAddresses = new ArrayList<>();

    public AddressServiceTest() {
        super();
        this.serviceUnderTest = new AddressService(daoMock);
    }

    @BeforeEach
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
    void testCheckForUniqueAddress() {
        Address address1 = new Address(14, "Kerklaan", 3, "A", "1234AB", "Zutphen");
        Address address2 = new Address(6, "Mareplein", 45, "3489DH", "Tiel");
        Address address3 = new Address(3, "Dorpsstraat", 7, "3759JF", "Doorn");
        assertThat(serviceUnderTest.checkForUniqueAddress(address1)).isNotNull().isEqualTo(14);
        assertThat(serviceUnderTest.checkForUniqueAddress(address2)).isNotNull().isEqualTo(6);
        assertThat(serviceUnderTest.checkForUniqueAddress(address3)).isNotNull().isEqualTo(-2);
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