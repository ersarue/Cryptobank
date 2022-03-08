package nl.miwteam2.cryptomero.service;

import nl.miwteam2.cryptomero.domain.BankAccount;
import nl.miwteam2.cryptomero.domain.Customer;
import nl.miwteam2.cryptomero.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Samuël Geurts & Stijn Klijn
 */

@Service
public class CustomerService implements GenericService<Customer> {

    private static final double INITIAL_BALANCE = 1000000;

    private CustomerRepository customerRepository;
    private AddressService addressService;
    private BankAccountService bankAccountService;
    private UserAccountService userAccountService;

    @Autowired
    public CustomerService(CustomerRepository customerRepository,AddressService addressService,
            BankAccountService bankAccountService,UserAccountService userAccountService) {
        this.customerRepository = customerRepository;
        this.addressService = addressService;
        this.bankAccountService = bankAccountService;
        this.userAccountService = userAccountService;
    }

    /**
     * Stores a new customer in the database
     * @param customer      The customer to be stored
     * @return              The stored customer if storage was successful
     * @throws Exception    If the customer cannot be stored
     */
    @Override
    public Customer storeOne(Customer customer) throws Exception {

        //Check whether all fields are valid and throw exception otherwise
        if (!isEveryFieldOfValidLength(customer)) throw new Exception("Invalid field length");
        if (!isValidEmail(customer.getEmail())) throw new Exception("Invalid e-mail");
        if (userAccountService.isEmailAlreadyInUse(customer.getEmail())) throw new Exception("E-mail already in use");
        if (!isValidPassword(customer.getPassword())) throw new Exception("Invalid password");
        if (!isValidBsn(customer.getBsn())) throw new Exception("Invalid bsn");

        //Attempt to store customer address and throw exception if address is invalid
        int addressId = addressService.storeAddress(customer.getAddress());
        customer.getAddress().setIdAddress(addressId);
        if (addressId == 0) throw new Exception("Invalid address");

        //Store customer in the database and receive the auto-generated key
        int userId = userAccountService.storeOne(customer);
        customer.setIdAccount(userId);
        customerRepository.storeOne(customer);

        //Generate and store new bank account
        BankAccount bankAccount = new BankAccount(customer, bankAccountService.generateIban(), INITIAL_BALANCE);
        customer.setBankAccount(bankAccount);
        bankAccountService.storeOne(bankAccount);

        //Setup empty wallet
        Map<String, Double> wallet = new HashMap<>();
        customer.setWallet(wallet);

        return customer;
    }

    /**
     * Retrieve customer with the given id from the database
     * @param id            Id of the customer to retrieve
     * @return              The retrieved customer
     */
    @Override
    public Customer findById(int id) {
        return customerRepository.findById(id);
    }

    @Override
    public List<Customer> getAll() {
        //Omitted until required
        return null;
    }

    @Override
    public Customer updateOne(Customer customer) {
        //Omitted until required
        return null;
    }

    @Override
    public Customer deleteOne(int id) {
        //Omitted until required
        return null;
    }

    /**
     * Check whether all required fields are not null and are not empty strings, and no fields are too long
     * @param customer      The customer to be stored
     * @return              Boolean representing whether this condition is met
     */
    private boolean isEveryFieldOfValidLength(Customer customer) {
        final int MAX_LENGTH_EMAIL = 30;
        final int MAX_LENGTH_PASSWORD = 64;
        final int MAX_LENGTH_FIRST_NAME = 45;
        final int MAX_LENGTH_NAME_PREFIX = 15;
        final int MAX_LENGTH_LAST_NAME = 45;
        final int MAX_LENGTH_TELEPHONE = 30;

        return customer.getEmail().length() > 0 && customer.getEmail().length() <= MAX_LENGTH_EMAIL &&
                customer.getPassword().length() > 0 && customer.getPassword().length() <= MAX_LENGTH_PASSWORD &&
                customer.getFirstName().length() > 0 &&  customer.getFirstName().length() <= MAX_LENGTH_FIRST_NAME &&
                (customer.getNamePrefix() == null || customer.getNamePrefix().length() < MAX_LENGTH_NAME_PREFIX) &&
                customer.getLastName().length() > 0 && customer.getLastName().length() <= MAX_LENGTH_LAST_NAME &&
                customer.getDob() != null &&
                customer.getTelephone().length() > 0 && customer.getTelephone().length() <= MAX_LENGTH_TELEPHONE &&
                customer.getAddress() != null;
    }

    /**
     * OWASP email validation according to Baeldung
     * @param email         The email to be validated
     * @return              Boolean representing whether this email is valid
     */
    private boolean isValidEmail(String email) {
        return email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    }

    /**
     * Check whether password conforms to requirements
     * @param password      The password to be checked
     * @return              Boolean representing whether this password meets the requirements
     */
    private boolean isValidPassword(String password) {
        //TODO eventuele eisen aan wachtwoord hier formuleren
        return true;
    }

    /**
     * Check whether bsn conforms to "11-proef"
     * @param bsn           The bsn to be checked
     * @return              Boolean representing whether this condition is met
     */
    private boolean isValidBsn(String bsn) {
        //TODO: Als dit via een externe API kan heeft dat de voorkeur, maar die kan ik vooralsnog niet vinden
        final int MIN_LENGTH = 8;
        final int MAX_LENGTH = 9;
        final int[] FACTORS = {9, 8, 7, 6, 5, 4, 3, 2, -1};
        final int DIVISOR = 11;
        if (bsn.length() < MIN_LENGTH || bsn.length() > MAX_LENGTH) {
            //bsn too short or too long
            return false;
        }
        if (!bsn.matches("\\d+")) {
            //bsn not consisting of only numbers
            return false;
        }
        if (bsn.length() == 8) {
            //prepend 0 to ensure bsn consists of 9 numbers
            bsn = "0" + bsn;
        }
        //Apply "11-proef"
        int sum = 0;
        for (int i = 0; i < bsn.length(); i++) {
            int digit = Integer.parseInt(bsn.substring(i, i + 1));
            sum += digit * FACTORS[i];
        }
        return sum % DIVISOR == 0;
    }
}
