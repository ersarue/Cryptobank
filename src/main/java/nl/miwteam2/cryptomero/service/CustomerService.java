package nl.miwteam2.cryptomero.service;

import nl.miwteam2.cryptomero.domain.BankAccount;
import nl.miwteam2.cryptomero.domain.Customer;
import nl.miwteam2.cryptomero.domain.CustomerDto;
import nl.miwteam2.cryptomero.domain.UserAccount;
import nl.miwteam2.cryptomero.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author Samuël Geurts & Stijn Klijn
 */

@Service
public class CustomerService {

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
     * @param customerDto   The customer data to be stored
     * @return              The stored customer if storage was successful
     * @throws Exception    If the customer cannot be stored
     */

    public Customer storeOne(CustomerDto customerDto) throws Exception {

        //Check whether all fields are valid, otherwise throw exception
        List<String> errors = checkFieldValidity(customerDto);
        if (!errors.isEmpty()) throw new Exception(String.join("\n", errors));

        //Attempt to store customer address and throw exception if address is invalid
        int addressId = addressService.storeAddress(customerDto.getAddress());
        customerDto.getAddress().setIdAddress(addressId);
        if (addressId == 0) {
            errors.add("Invalid address");
            throw new Exception(String.join("\n", errors));
        }

        Customer customer = new Customer(customerDto);

        //Store customer in the database and receive the auto-generated key
        UserAccount userAccount = userAccountService.storeOne(customer);
        customer.setIdAccount(userAccount.getIdAccount());
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
    public Customer findById(int id) {
        return customerRepository.findById(id);
    }

    public List<Customer> getAll() {
        //Omitted until required
        return null;
    }

    public Customer updateOne(Customer customer) {
        //Omitted until required
        return null;
    }

    public Customer deleteOne(int id) {
        //Omitted until required
        return null;
    }

    /**
     * Check whether all fields are valid
     * @param customer      The customer to be stored
     * @return              String representing whether all fields are valid or which errors occurred.
     */
    private List<String> checkFieldValidity(CustomerDto customer) throws NoSuchAlgorithmException, IOException, InterruptedException {
        List<String> errors = new ArrayList<>();
        List<String> missingFields = checkMissingFields(customer);
        List<String> overflowingFields = checkOverflowingFields(customer);
        int numberOfBreaches = numberOfPasswordBreaches(customer.getPassword());

        if (!missingFields.isEmpty()) errors.add("Missing fields: " + String.join(", ", missingFields));
        if (!overflowingFields.isEmpty()) errors.add("Overflowing fields: " + String.join(", ", overflowingFields));
        if (!isValidEmail(customer.getEmail())) errors.add("Invalid e-mail");
        if (userAccountService.isEmailAlreadyInUse(customer.getEmail())) errors.add("E-mail already in use");
        if (!isValidPassword(customer.getPassword())) errors.add("Invalid password");
        if (numberOfBreaches!=0) errors.add("This password has been seen " + numberOfBreaches + " times before");
        if (!isValidDob(customer.getDob())) errors.add("Invalid date of birth (customer too young)");
        if (!isValidBsn(customer.getBsn())) errors.add("Invalid bsn");
        if (customerRepository.isBSNAlreadyInUse(customer.getBsn())) errors.add("bsn already in use");
        return errors;
    }

    /**
     * Check whether all required fields are not null and are not empty strings
     * @param customer      The customer to be stored
     * @return              List with the fields that are missing
     */
    private List<String> checkMissingFields(CustomerDto customer) {
        List<String> missingFields = new ArrayList<>();
        if (customer.getEmail().isEmpty()) missingFields.add("e-mail");
        if (customer.getPassword().isEmpty()) missingFields.add("password");
        if (customer.getFirstName().isEmpty()) missingFields.add("first name");
        if (customer.getLastName().isEmpty()) missingFields.add("last name");
        if (customer.getDob() == null) missingFields.add("date of birth");
        if (customer.getTelephone().isEmpty()) missingFields.add("telephone");
        if (customer.getAddress() == null) missingFields.add("address");
        return missingFields;
    }

    /**
     * Check whether no fields are overflowing
     * @param customer      The customer to be stored
     * @return              List with the fields that are overflowing
     */
    private List<String> checkOverflowingFields(CustomerDto customer) {
        final int MAX_LENGTH_EMAIL = 30;
        final int MAX_LENGTH_PASSWORD = 64;
        final int MAX_LENGTH_FIRST_NAME = 45;
        final int MAX_LENGTH_NAME_PREFIX = 15;
        final int MAX_LENGTH_LAST_NAME = 45;
        final int MAX_LENGTH_TELEPHONE = 30;

        List<String> overflowingFields = new ArrayList<>();
        if (customer.getEmail().length() > MAX_LENGTH_EMAIL) overflowingFields.add("e-mail");
        if (customer.getPassword().length() > MAX_LENGTH_PASSWORD) overflowingFields.add("password");
        if (customer.getFirstName().length() > MAX_LENGTH_FIRST_NAME) overflowingFields.add("first name");
        if (customer.getNamePrefix().length() > MAX_LENGTH_NAME_PREFIX) overflowingFields.add("name prefix");
        if (customer.getLastName().length() > MAX_LENGTH_LAST_NAME) overflowingFields.add("last name");
        if (customer.getTelephone().length() > MAX_LENGTH_TELEPHONE) overflowingFields.add("telephone");
        return overflowingFields;
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
     * Check whether password is part of haveibeenpwned.com password datatset
     * @param password      The password to be checked
     * @return              Boolean representing whether this password is breached
     */
    private int numberOfPasswordBreaches(String password) throws NoSuchAlgorithmException, IOException, InterruptedException {
        String sha1 = stringToSHa1Hex(password);
        Map<String,Integer> hashedPasswordlist = getHashedPasswordlist(sha1);
        if (hashedPasswordlist.containsKey(sha1)){
            return hashedPasswordlist.get(sha1);
        } else {
            return 0;
        }
    }
    /**
     * turns a String into a Sha-1 hex string
     * @param string       The string to be hashed (a password in this case)
     * @return             The hashed string in hex format
     */
    private String stringToSHa1Hex(String string) throws NoSuchAlgorithmException {
        MessageDigest msdDigest = MessageDigest.getInstance("SHA-1");
        msdDigest.update(string.getBytes());
        byte[] digest = msdDigest.digest();
        return HexFormat.of().formatHex(digest).toUpperCase();
    }

    /**
     * Does a get request at pwnedpasswords.com. The first 5 characters of he hashed password are given within the
     * request. All known hashcodes with the same first 5 characters are returned in the respons body included the
     * number of breaches.
     * @param sha1       The sha-1 string to be checked
     * @return           A map containing sha-1 Strings as keys and the occurrence number as values;
     */
    private Map<String, Integer> getHashedPasswordlist(String sha1) throws IOException, InterruptedException {
        //performs https request
        String url = String.format("https://api.pwnedpasswords.com/range/%s", sha1.substring(0, 5));
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(url)).build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        //transforms respons-body to map
        Map<String,Integer> numberOfBreachesPerPassword = new TreeMap<>();
        String[] arrayOfStr = response.body().split("\n");
        for (String string: arrayOfStr) {
            String sha1part = string.substring(0,string.indexOf(":"));
            Integer numberOfBreaches = Integer.parseInt(string.substring(string.indexOf(":")+1).replace("\r",""));
            numberOfBreachesPerPassword.put(sha1.substring(0, 5) + sha1part,numberOfBreaches);
        }
        return numberOfBreachesPerPassword;
    }

    /**
     * Check whether password conforms to requirements
     * @param password      The password to be checked
     * @return              Boolean representing whether this password meets the requirements
     */
    private boolean isValidPassword(String password) {
        final int MIN_LENGTH = 8;
        final int MAX_LENGTH = 64;
        return password.length() >= MIN_LENGTH && password.length() <= MAX_LENGTH && !isRepetitive(password);
    }

    /**
     * Checks whether a password contains sequences of more than two identical characters (e.g. 'aaa'), sequences
     * of the same character group (e.g.'baba') or sequences of more than two natural numbers (e.g. '123')
     * @param password      The password to be checked
     * @return              True if the password contains one or more sequences; false if it does not
     */
    public boolean isRepetitive(String password) {
        //todo alleen zware eisen als het password te kort is
        //checkt herhalingen van 4 de zelfde tekens achter elkaar
        Pattern patChar = Pattern.compile("(.)\\1\\1\\1"); //hhhh mag niet

        //checkt of een sequence van 4 tot 7 tekens, direct achter elkaar herhaald wordt.
        Pattern patGroup1 = Pattern.compile("(.{4,7})\\1"); //hallohallo mag niet

        //checkt of een sequence van 3, direct achter elkaar 2 maal herhaald wordt.
        Pattern patGroup2 = Pattern.compile("(.{2,3})\\1\\1"); //halhalhal mag niet

        //5 getallen achterelkaar is niet toegestaan.
        Pattern patNum = Pattern.compile("\\d{5}"); //38132 mag niet

        // Use Pattern.matcher() and find() because we don't necessarily want to match the entire string
        return patChar.matcher(password).find() | patGroup1.matcher(password).find() | patGroup2.matcher(password).find()| patNum.matcher(password).find();
    }

    /**
     * Check whether date of birth conforms to requirements
     * @param date          The date of birth to be checked
     * @return              Boolean representing whether this dob meets the requirements
     */
    private boolean isValidDob(LocalDate date) {
        final int MIN_AGE = 18;
        return !LocalDate.now().minusYears(MIN_AGE).isBefore(date);
    }

    /**
     * Check whether bsn conforms to "11-proef"
     * @param bsn           The bsn to be checked
     * @return              Boolean representing whether this condition is met
     */
    public boolean isValidBsn(String bsn) {
        final int MIN_LENGTH = 8;
        final int MAX_LENGTH = 9;
        final int[] FACTORS = {9, 8, 7, 6, 5, 4, 3, 2, -1};
        final int DIVISOR = 11;

        if (bsn.length() < MIN_LENGTH || bsn.length() > MAX_LENGTH) return false; //bsn too short or too long
        if (!bsn.matches("\\d+")) return false; //bsn not consisting of only numbers

        if (bsn.length() == 8) bsn = "0" + bsn; //prepend 0 to ensure bsn consists of 9 numbers

        //Apply "11-proef"
        int sum = 0;
        for (int i = 0; i < bsn.length(); i++) {
            int digit = Integer.parseInt(bsn.substring(i, i + 1));
            sum += digit * FACTORS[i];
        }
        return sum % DIVISOR == 0;
    }
}
