package nl.miwteam2.cryptomero.service;

import nl.miwteam2.cryptomero.domain.UserAccount;
import nl.miwteam2.cryptomero.repository.RootRepository;
import nl.miwteam2.cryptomero.repository.UserAccountDao;
import nl.miwteam2.cryptomero.service.Authentication.HashService;
import nl.miwteam2.cryptomero.service.Authentication.SaltMaker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.LIST;
import static org.junit.jupiter.api.Assertions.*;

class UserAccountServiceTest {

  private UserAccountDao userAccountDao = Mockito.mock(UserAccountDao.class);
  private RootRepository rootRepository = Mockito.mock(RootRepository.class);
  private UserAccountService userAccountServiceToTest;
  private UserAccount userAccountToStore;
  private HashService hash;
  private SaltMaker salt;

  private UserAccountServiceTest(){
    this.userAccountServiceToTest = new UserAccountService(hash, salt, rootRepository, userAccountDao);
  }

  @BeforeEach
  void setUp() {
    UserAccount userAccountWithId = new UserAccount(1, "test@test.com", "test");
    UserAccount userAccountWithEmail = new UserAccount("test2@test.com", "test2");
    userAccountToStore = new UserAccount("test123", "test123");
    userAccountToStore.setIdAccount(123);
    Mockito.when(rootRepository.findUserAccountById(1)).thenReturn(userAccountWithId);
    Mockito.when(rootRepository.findUserAccountByEmail("test@test.com")).thenReturn(userAccountWithEmail);
    Mockito.when(rootRepository.findUserAccountById(123)).thenReturn(userAccountToStore);
  }

  @Test
  void findById() {
    UserAccount actual = rootRepository.findUserAccountById(1);
    UserAccount expected = new UserAccount(1, "test@test.com", "test");
    assertThat(actual).isNotNull().isEqualTo(expected);
  }

  @Test
  void findByEmail() {
    UserAccount actual = rootRepository.findUserAccountByEmail("test@test.com");
    UserAccount expected = new UserAccount("test2@test.com", "test2");
    assertThat(actual).isNotNull().isEqualTo(expected);
  }

  @Test
  void storeOne() {
    UserAccount actual = userAccountServiceToTest.storeOne(userAccountToStore);
    assertThat(actual).isNotNull();
    assertThat(actual.getIdAccount()).isEqualTo(123);
    assertThat(actual.getEmail()).isEqualTo("test123@test.com");
    assertThat(actual.getPassword()).isEqualTo("test123");
  }

  @Test
  void getAll() {
    List<UserAccount> actual = userAccountServiceToTest.getAll();
    List<UserAccount> expected = new ArrayList<>();
    assertThat(actual).isNotNull().isEqualTo(expected);
  }
}