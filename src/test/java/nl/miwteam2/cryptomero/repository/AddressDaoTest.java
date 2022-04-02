package nl.miwteam2.cryptomero.repository;

import nl.miwteam2.cryptomero.domain.Address;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
* @author Petra Coenen
* @version 1.1
*/

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AddressDaoTest {

    private GenericDao<Address> daoUnderTest;

    private Address addressAdam;
    private Address addressHague;

    @Autowired
    public AddressDaoTest(GenericDao<Address> AddressDao) {
        this.daoUnderTest = AddressDao;
    }

    @BeforeAll
    public void setup() {
        addressAdam = new Address("Nieuwezijds Voorburgwal", 147, "1012RJ", "Amsterdam");
        addressHague = new Address("George Maduroplein", 1, "2584 RZ", "Den Haag");
    }

    @Test
    void storeOne() {
        int expected = 3;
        int actual = daoUnderTest.storeOne(addressAdam);
        assertThat(actual).isNotNull().isEqualTo(expected);

        expected = 4;
        actual = daoUnderTest.storeOne(addressHague);
        assertThat(actual).isNotNull().isEqualTo(expected);
    }

    @Test
    void findById() {
        Address expected = new Address(2, "Street", 99, "D",
                "1000CC", "Duckstad");
        Address actual = daoUnderTest.findById(2);
        assertThat(actual).isNotNull().isEqualTo(expected);
    }

    @Test
    void getAll() {
        List<Address> allAdresses = daoUnderTest.getAll();
        assertThat(allAdresses).isNotNull().isNotEmpty();
        assertThat(allAdresses.size()).isEqualTo(2);
    }

    @Test
    void updateOne() {
        int expectedReturn = 1;
        int actualReturn = daoUnderTest.updateOne(new Address(1,"Street", 101, "B",
                "1000AA", "City"));
        assertThat(actualReturn).isNotNull().isEqualTo(expectedReturn);

        Address expectedResult = new Address(1,"Street", 101, "B", "1000AA",
                "City");
        Address actualResult = daoUnderTest.findById(1);
        assertThat(actualResult).isNotNull().isEqualTo(expectedResult);
    }

    @Test
    void deleteOne() {
        int expectedReturn = 1;
        int actualReturn = daoUnderTest.deleteOne(2);
        assertThat(actualReturn).isNotNull().isEqualTo(expectedReturn);
    }
}
