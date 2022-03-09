package nl.miwteam2.cryptomero.repository;

import nl.miwteam2.cryptomero.domain.Customer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Fail.fail;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JdbcCustomerDaoTest {

    private GenericDao<Customer> jdbcCustomerDao;

    @Autowired
    public JdbcCustomerDaoTest(GenericDao<Customer> jdbcCustomerDao) {
        this.jdbcCustomerDao = jdbcCustomerDao;
    }


    @BeforeAll
    public void setup() {

    }

    @Test
    void findById() {
        Customer expected = new Customer("firstName", "namePrefix", "lastName",
                LocalDate.parse("2000-01-01"), "182358197", "0612345678");
        Customer actual = jdbcCustomerDao.findById(1);
        assertThat(actual).isNotNull().isEqualTo(expected);
    }

    @Test
    void storeOne() {
        //TODO
    }

    @Test
    void getAll() {
        //TODO
    }

    @Test
    void updateOne() {
        //TODO
    }

    @Test
    void deleteOne() {
        //TODO
    }

    @Test
    void findAddressIdOfCustomer() {
        //TODO
    }
}