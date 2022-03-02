package nl.miwteam2.cryptomero.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Contains unit tests for methods in the AddressService class.
 * @author Petra Coenen
 * @version 1.0
 */

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @InjectMocks
    AddressService serviceUnderTest;

    @DisplayName("testValidPostalCodeFormats")
    @ParameterizedTest
    @ValueSource(strings = {"1001AM","1001 AM","8394VR","4317 AE"})
    void isValidFormat(String postalCode) {
        assertTrue(serviceUnderTest.isValidFormat(postalCode));
    }

    @DisplayName("testInvalidPostalCodeFormats")
    @ParameterizedTest
    @ValueSource(strings = {"invalidPostalCode","3284  KD","123KR","4317ERF","2214 D3","4291F9"})
    void isInvalidFormat(String postalCode) {
        assertFalse(serviceUnderTest.isValidFormat(postalCode));
    }
}