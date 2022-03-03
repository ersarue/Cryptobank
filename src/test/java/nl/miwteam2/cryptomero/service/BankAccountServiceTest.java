package nl.miwteam2.cryptomero.service;

import nl.miwteam2.cryptomero.domain.BankAccount;
import nl.miwteam2.cryptomero.domain.UserAccount;
import nl.miwteam2.cryptomero.repository.JdbcBankAccountDao;
import nl.miwteam2.cryptomero.repository.RootRepository;
import nl.miwteam2.cryptomero.service.BankAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

public class BankAccountServiceTest {

    private JdbcBankAccountDao jdbcBankAccountDao= Mockito.mock(JdbcBankAccountDao.class);
    private RootRepository rootRepository=Mockito.mock(RootRepository.class);
    private BankAccountService bankAccountServiceTest;

    public BankAccountServiceTest(){
        this.bankAccountServiceTest = new BankAccountService(jdbcBankAccountDao,rootRepository);

    }

    @BeforeEach
    public void testSetup() {
        UserAccount userAccount = new UserAccount(1,"marcel@yahoo.com", "qwerty");
        BankAccount bankAccount = new BankAccount("NL67INGB0007755322", 1000000);
        bankAccount.setUserAccount(userAccount);
        Mockito.when(rootRepository.findBankaccountById(1)).thenReturn(bankAccount);
    }
    @Test
    public void testFindById() {
        BankAccount actual = bankAccountServiceTest.findById(1);
        UserAccount userAccount = new UserAccount(1,"marcel@yahoo.com", "qwerty");
        BankAccount expected = new BankAccount("NL67INGB0007755322", 1000000);
        expected.setUserAccount(userAccount);
        assertThat(actual).isNotNull().isEqualTo(expected);

    }

}