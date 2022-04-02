package nl.miwteam2.cryptomero.repository;

import nl.miwteam2.cryptomero.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * @author Marcel Brachten, Samuël Geurts, Stijn Klijn, Ercan Ersaru
 */

@Repository
public class RootRepository {

  private UserAccountDao userAccountDao;
  private AddressDao addressDao;
  private CustomerDao customerDao;
  private WalletDao walletDao;
  private BankAccountDao bankAccountDao;

  @Autowired
  public RootRepository(UserAccountDao userAccountDao, AddressDao addressDao,
						CustomerDao customerDao, WalletDao walletDao,
						BankAccountDao bankAccountDao) {
	this.userAccountDao = userAccountDao;
	this.addressDao = addressDao;
	this.customerDao = customerDao;
	this.walletDao = walletDao;
	this.bankAccountDao = bankAccountDao;
  }

  /**
   * Find a bankaccount in the database.
   *
   * @param id this is the userid.
   */
  public BankAccount findBankaccountById(int id) {
	UserAccount userAccount = userAccountDao.findById(id);
	BankAccount bankAccount = bankAccountDao.findById(id);
	bankAccount.setUserAccount(userAccount);
	return bankAccount;
  }

  /**
   * Find all bankaccounts in the database.
   */
  public List<BankAccount> getAll() {
	List<UserAccount> userAccountList = userAccountDao.getAll();
	List<BankAccount> bankAccountList = bankAccountDao.getAll();
	for (int i = 0; i < bankAccountList.size(); i++) {
	  UserAccount userAccount = userAccountList.get(i);
	  BankAccount bankAccount = bankAccountList.get(i);
	  bankAccount.setUserAccount(userAccount);
	}
	return bankAccountList;
  }

  /**
   * Find user account by id and email address
   */

  public UserAccount findUserAccountById(int id) {return userAccountDao.findById(id);}

  public UserAccount findUserAccountByEmail(String email) {
	return userAccountDao.findByEmail(email);
  }


}
