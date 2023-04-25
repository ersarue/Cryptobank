package nl.miwteam2.cryptomero.repository;

import nl.miwteam2.cryptomero.domain.Address;
import nl.miwteam2.cryptomero.domain.BankAccount;
import nl.miwteam2.cryptomero.domain.Customer;
import nl.miwteam2.cryptomero.domain.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author Samuël Geurts & Stijn Klijn
 */

@Repository
public class CustomerRepository implements GenericDao<Customer>{

    private UserAccountDao userAccountDao;
    private AddressDao addressDao;
    private CustomerDao customerDao;
    private WalletDao walletDao;
    private BankAccountDao bankAccountDao;

    @Autowired
    public CustomerRepository(UserAccountDao userAccountDao, AddressDao addressDao,
                              CustomerDao customerDao, WalletDao walletDao,
                              BankAccountDao bankAccountDao) {
        this.userAccountDao = userAccountDao;
        this.addressDao = addressDao;
        this.customerDao = customerDao;
        this.walletDao = walletDao;
        this.bankAccountDao = bankAccountDao;
    }
    /**
     * Retrieve a customer from the database
     * @param id            Id of the customer to be retrieved
     * @return              The retrieved customer
     */
    public Customer findById(int id) {

        //Retrieve Customer, UserAccount, BankAccount and wallet for the given id
        Customer customer = customerDao.findById(id);
        UserAccount userAccount = userAccountDao.findById(id);
        BankAccount bankAccount = bankAccountDao.findById(id);
        Map<String, Double> wallet = walletDao.findById(userAccount.getIdAccount());

        //Retrieve address of this customer
        customer.setIdAccount(userAccount.getIdAccount());
        int addressId = customerDao.findAddressIdOfCustomer(customer);
        Address address = addressDao.findById(addressId);

        //Set all customer fields appropriately
        customer.setEmail(userAccount.getEmail());
        customer.setPassword(userAccount.getPassword());
        customer.setAddress(address);
        customer.setBankAccount(bankAccount);
        customer.setWallet(wallet);

        return customer;
    }

    public int storeOne(Customer customer) {
        return customerDao.storeOne(customer);
    }

    public List<Customer> getAll() {
        return customerDao.getAll();
    }

    public int updateOne(Customer customer) {
        return customerDao.updateOne(customer);
    }

    public int deleteOne(int id) {
        return customerDao.deleteOne(id);
    }

    public int findAddressIdOfCustomer(Customer customer) {
        return customerDao.findAddressIdOfCustomer(customer);
    }

    public boolean isBSNAlreadyInUse(String bsn) {
        return customerDao.isBSNAlreadyInUse(bsn);
    }
}
