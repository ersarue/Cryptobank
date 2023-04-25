package nl.miwteam2.cryptomero.repository;

import nl.miwteam2.cryptomero.domain.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomerDaoTest {

    private GenericDao<Customer> customerDao;

    @Autowired
    public CustomerDaoTest(GenericDao<Customer> customerDao) {
        this.customerDao = customerDao;
    }

    @Test
    void findById() {
        Customer expected = new Customer("firstName", "namePrefix", "lastName",
                LocalDate.parse("2000-01-01"), "182358197", "0612345678");
        Customer actual = customerDao.findById(1);
        assertThat(actual).isNotNull().isEqualTo(expected);
    }
}