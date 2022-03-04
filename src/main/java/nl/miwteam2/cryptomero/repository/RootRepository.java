package nl.miwteam2.cryptomero.repository;

import nl.miwteam2.cryptomero.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

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

    public Customer findCustomerById(int id) {

        Customer customer = jdbcCustomerDao.findById(id);
        UserAccount userAccount = jdbcUserAccountDao.findById(id);
        BankAccount bankAccount = jdbcBankAccountDao.findById(id);
        Map<Asset, Double> wallet = jdbcWalletDao.findById(userAccount.getIdAccount());

        customer.setIdAccount(userAccount.getIdAccount());
        int addressId = jdbcCustomerDao.findAddressIdOfCustomer(customer);
        Address address = jdbcAddressDao.findById(addressId);

        customer.setEmail(userAccount.getEmail());
        customer.setPassword(userAccount.getPassword());
        customer.setAddress(address);
        customer.setBankAccount(bankAccount);
        customer.setWallet(wallet);

        return customer;
    }
    public BankAccount findBankaccountById(int id) {
        UserAccount userAccount = jdbcUserAccountDao.findById(id);
        BankAccount bankAccount= jdbcBankAccountDao.findById(id);
        bankAccount.setUserAccount(userAccount);
        return bankAccount;
    }

    public UserAccount findUserAccountById(int id){
        return jdbcUserAccountDao.findById(id);
    }

}
