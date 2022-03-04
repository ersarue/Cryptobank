package nl.miwteam2.cryptomero.repository;

import nl.miwteam2.cryptomero.domain.Address;
import nl.miwteam2.cryptomero.domain.BankAccount;
import nl.miwteam2.cryptomero.domain.Customer;
import nl.miwteam2.cryptomero.domain.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RootRepository {

    private JdbcUserAccountDao jdbcUserAccountDao;
    private JdbcAddressDao jdbcAddressDao;
    private JdbcCustomerDao jdbcCustomerDao;
    private JdbcBankAccountDao jdbcBankAccountDao;

    @Autowired

    public RootRepository(JdbcUserAccountDao jdbcUserAccountDao, JdbcAddressDao jdbcAddressDao,
                          JdbcCustomerDao jdbcCustomerDao, JdbcBankAccountDao jdbcBankAccountDao) {
        this.jdbcUserAccountDao = jdbcUserAccountDao;
        this.jdbcAddressDao = jdbcAddressDao;
        this.jdbcCustomerDao = jdbcCustomerDao;
        this.jdbcBankAccountDao = jdbcBankAccountDao;
    }

    public Customer findCustomerById(int id) {

        Customer customer = jdbcCustomerDao.findById(id);
        UserAccount userAccount = jdbcUserAccountDao.findById(id);


        customer.setIdAccount(userAccount.getIdAccount());
        customer.setEmail(userAccount.getEmail());
        customer.setPassword(userAccount.getPassword());
        Address address = jdbcAddressDao.findById(jdbcCustomerDao.findAddressIdOfCustomer(customer));
        customer.setAddress(address);
        return customer;
    }
    /**
     * Find a bankaccount in the database.
     * @param  id   this is the userid.
     */
    public BankAccount findBankaccountById(int id) {
        UserAccount userAccount = jdbcUserAccountDao.findById(id);
        BankAccount bankAccount= jdbcBankAccountDao.findById(id);
        bankAccount.setUserAccount(userAccount);
        return bankAccount;
    }
    /**
     * Find all bankaccounts in the database.
     */
    public List<BankAccount> getAll(){
        List<UserAccount> userAccountList = jdbcUserAccountDao.getAll();
        List<BankAccount> bankAccountList =jdbcBankAccountDao.getAll();
        for (int i=0;i< bankAccountList.size();i++){
            UserAccount userAccount = userAccountList.get(i);
            BankAccount bankAccount = bankAccountList.get(i);
            bankAccount.setUserAccount(userAccount);
        }
        return bankAccountList;
    }

    public UserAccount findUserAccountById(int id){
        return jdbcUserAccountDao.findById(id);
    }

}
