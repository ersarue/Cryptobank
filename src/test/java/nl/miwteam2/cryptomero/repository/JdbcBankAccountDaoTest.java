package nl.miwteam2.cryptomero.repository;

import nl.miwteam2.cryptomero.domain.BankAccount;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@ActiveProfiles("test")
class JdbcBankAccountDaoTest {
    private JdbcBankAccountDao jdbcBankAccountDao;

    @Autowired
    public JdbcBankAccountDaoTest(JdbcBankAccountDao jdbcBankAccountDao){
        super();
        this.jdbcBankAccountDao=jdbcBankAccountDao;
    }
    @Test
    void findById() {
        //BankAccount actual = jdbcBankAccountDao.findById(1);
        //BankAccount expected = new BankAccount("NL67INGB0007755322", 1000000.0000);
        //assertThat(actual).isNotNull().isEqualTo(expected);
    }

    @Test
    void getAll() {
    }
}