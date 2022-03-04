package nl.miwteam2.cryptomero.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Petra Coenen
 * @version 1.0
 */

class AddressTest {

    @Test
    void testPostalCodeTrim() {
        Address testAddress1 = new Address(2, "Kerklaan", 3, "2142 NE", "Haarlem");
        Address testAddress2 = new Address(4, "Lindeweg", 34, "3928 KS ", "Tiel");
        Address testAddress3 = new Address(6, "Marepad", 5, "A"," 8394 KG", "Lisse");

        assertEquals("2142NE", testAddress1.getPostalCode());
        assertEquals("3928KS", testAddress2.getPostalCode());
        assertEquals("8394KG", testAddress3.getPostalCode());
    }
}