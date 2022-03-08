package nl.miwteam2.cryptomero.repository;

import nl.miwteam2.cryptomero.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author Marcel Brachten, Samuël Geurts, Stijn Klijn
 */

@Repository
public class RootRepository {

    private JdbcUserAccountDao jdbcUserAccountDao;
    private JdbcAddressDao jdbcAddressDao;
    private JdbcCustomerDao jdbcCustomerDao;
    private JdbcWalletDao jdbcWalletDao;
    private JdbcBankAccountDao jdbcBankAccountDao;

    @Autowired
    public RootRepository(JdbcUserAccountDao jdbcUserAccountDao, JdbcAddressDao jdbcAddressDao,
                          JdbcCustomerDao jdbcCustomerDao, JdbcWalletDao jdbcWalletDao,
                          JdbcBankAccountDao jdbcBankAccountDao) {
        this.jdbcUserAccountDao = jdbcUserAccountDao;
        this.jdbcAddressDao = jdbcAddressDao;
        this.jdbcCustomerDao = jdbcCustomerDao;
        this.jdbcWalletDao = jdbcWalletDao;
        this.jdbcBankAccountDao = jdbcBankAccountDao;
    }

    /**
     * Retrieve a customer from the database
     * @param id            Id of the customer to be retrieved
     * @return              The retrieved customer
     */
    public Customer findCustomerById(int id) {

        //Retrieve Customer, UserAccount, BankAccount and wallet for the given id
        Customer customer = jdbcCustomerDao.findById(id);
        UserAccount userAccount = jdbcUserAccountDao.findById(id);
        BankAccount bankAccount = jdbcBankAccountDao.findById(id);
        Map<String, Double> wallet = jdbcWalletDao.findById(userAccount.getIdAccount());

        //Retrieve address of this customer
        customer.setIdAccount(userAccount.getIdAccount());
        int addressId = jdbcCustomerDao.findAddressIdOfCustomer(customer);
        Address address = jdbcAddressDao.findById(addressId);

        //Set all customer fields appropriately
        customer.setEmail(userAccount.getEmail());
        customer.setPassword(userAccount.getPassword());
        customer.setAddress(address);
        customer.setBankAccount(bankAccount);
        customer.setWallet(wallet);

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
