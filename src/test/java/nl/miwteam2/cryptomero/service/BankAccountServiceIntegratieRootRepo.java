package nl.miwteam2.cryptomero.service;

import nl.miwteam2.cryptomero.domain.BankAccount;
import nl.miwteam2.cryptomero.domain.UserAccount;
import nl.miwteam2.cryptomero.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

public class BankAccountServiceIntegratieRootRepo {
    private BankAccountService bankAccountService;
    private RootRepository repo;
    private JdbcBankAccountDao jdbcBankAccountDao;
    private JdbcUserAccountDao jdbcUserAccountDao;
    private JdbcAddressDao jdbcAddressDao;
    private JdbcCustomerDao jdbcCustomerDao;


    public BankAccountServiceIntegratieRootRepo(){
        super();
        this.jdbcBankAccountDao= Mockito.mock(JdbcBankAccountDao.class);
        this.repo=new RootRepository(jdbcUserAccountDao,jdbcAddressDao,jdbcCustomerDao,jdbcBankAccountDao);
        this.bankAccountService=new BankAccountService(jdbcBankAccountDao,repo);

    }
    @BeforeEach
    public void testSetup() {
        BankAccount bankAccount = new BankAccount("NL67INGB0007755322", 1000000);
        Mockito.when(jdbcBankAccountDao.findById(1)).thenReturn(bankAccount);
    }
    @Test
    public void integratieTest(){
        BankAccount actual = jdbcBankAccountDao.findById(1);
        UserAccount userAccount = new UserAccount(1,"marcel@hva.nl", "password");
        actual.setUserAccount(userAccount);
        BankAccount expected = new BankAccount("NL67INGB0007755322", 1000000);
        expected.setUserAccount(userAccount);
        assertThat(actual).isNotNull().isEqualTo(expected);
    }
}
