package nl.miwteam2.cryptomero.repository;

import nl.miwteam2.cryptomero.domain.Address;
import nl.miwteam2.cryptomero.domain.BankAccount;
import nl.miwteam2.cryptomero.domain.Customer;
import nl.miwteam2.cryptomero.domain.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class CustomerRepository implements GenericDao<Customer>{

    private JdbcUserAccountDao jdbcUserAccountDao;
    private JdbcAddressDao jdbcAddressDao;
    private JdbcCustomerDao jdbcCustomerDao;
    private JdbcWalletDao jdbcWalletDao;
    private JdbcBankAccountDao jdbcBankAccountDao;

    @Autowired
    public CustomerRepository(JdbcUserAccountDao jdbcUserAccountDao, JdbcAddressDao jdbcAddressDao,
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
    public Customer findById(int id) {

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

    public int storeOne(Customer customer) {
        return jdbcCustomerDao.storeOne(customer);
    }

    public List<Customer> getAll() {
        return jdbcCustomerDao.getAll();
    }

    public int updateOne(Customer customer) {
        return jdbcCustomerDao.updateOne(customer);
    }

    public int deleteOne(int id) {
        return jdbcCustomerDao.deleteOne(id);
    }

    public int findAddressIdOfCustomer(Customer customer) {
        return jdbcCustomerDao.findAddressIdOfCustomer(customer);
    }
}
