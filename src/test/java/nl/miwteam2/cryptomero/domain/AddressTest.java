package nl.miwteam2.cryptomero.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Petra Coenen
 */

class AddressTest {

    @Test
    void testPostalCodeTrim() {
        Address testAddress1 = new Address(2, "Kerklaan", 3, "2142 NE", "Haarlem");
        Address testAddress3 = new Address(6, "Marepad", 5, "A"," 8394 KG", "Lisse");
        Address testAddress4 = new Address(9, "Eikenweg", 89,null, "Lisse");

        assertThat(testAddress1.getPostalCode()).isNotNull().isEqualTo("2142NE");
        assertThat(testAddress3.getPostalCode()).isNotNull().isEqualTo("8394KG");
        assertThat(testAddress4.getPostalCode()).isNull();
    }
}