package nl.miwteam2.cryptomero.service;

import nl.miwteam2.cryptomero.domain.UserAccount;
import nl.miwteam2.cryptomero.repository.GenericDao;
import nl.miwteam2.cryptomero.repository.JdbcUserAccountDao;
import nl.miwteam2.cryptomero.repository.RootRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test UserAccountService
 *
 * @author Ercan Ersaru, studentnr: 500893336 - MIW Cohort 26
 */

class UserAccountServiceTest {

  private JdbcUserAccountDao jdbcUserAccountDao = Mockito.mock(JdbcUserAccountDao.class);
  private RootRepository rootRepository = Mockito.mock(RootRepository.class);
  private GenericDao<UserAccount> userAccountDaoMock = Mockito.mock(JdbcUserAccountDao.class);
  private UserAccountService userAccountServiceTest;

  UserAccountServiceTest() {
	userAccountServiceTest = new UserAccountService(rootRepository, userAccountDaoMock);
  }

  @BeforeEach
  void setUp() {
	UserAccount userAccount = new UserAccount(1, "test1@test.com", "test1");
//	UserAccount userAccount2 = new UserAccount(2, "test2@test.com", "test2");
	Mockito.when(rootRepository.findUserAccountById(1)).thenReturn(userAccount);
//	Mockito.when(rootRepository.findUserAccountById(2)).thenReturn(userAccount);
  }

  @Test
  void findById() {
	UserAccount actualUserAccount1 = userAccountServiceTest.findById(1);
//	UserAccount actualUserAccount2 = userAccountServiceTest.findById(2);
	UserAccount expectedUserAccount1 = new UserAccount(1, "test1@test.com", "test1");
//	UserAccount expectedUserAccount2 = new UserAccount(2, "test2@test.com", "test2");
	expectedUserAccount1.setIdAccount(1);
//	expectedUserAccount1.setIdAccount(2);
	assertThat(actualUserAccount1).isNotNull().isEqualTo(expectedUserAccount1);
//	assertThat(actualUserAccount2).isNotNull().isEqualTo(expectedUserAccount2);

  }

  @Test
  void storeOne() {
  }
}