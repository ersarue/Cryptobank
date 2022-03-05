package nl.miwteam2.cryptomero.service;

import nl.miwteam2.cryptomero.domain.Customer;
import nl.miwteam2.cryptomero.repository.GenericDao;
import nl.miwteam2.cryptomero.repository.RootRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class CustomerService implements GenericService<Customer> {

    private GenericDao<Customer> customerDao;
    private RootRepository rootRepository;
    private AddressService addressService;
    private UserAccountService userAccountService;

    @Autowired
    public CustomerService(GenericDao<Customer> dao, RootRepository rootRepository,AddressService addressService,UserAccountService userAccountService) {
        this.rootRepository = rootRepository;
        this.customerDao = dao;
        this.addressService = addressService;
        this.userAccountService = userAccountService;
    }

    @Override
    public Customer findById(int id) {
        return rootRepository.findCustomerById(id);
    }

    @Override
    public int storeOne(Customer customer) {

        if (!isValidEmail(customer.getEmail())) {
            //Invalid email, customer cannot be stored
            return 0;
        }

        if (!isValidPassword(customer.getPassword())) {
            //Invalid password, customer cannot be stored
            return 0;
        }

        if (!isValidBsn(customer.getBsn())) {
            //Invalid bsn, customer cannot be stored
            return 0;
        }

        int addressId = addressService.storeAddress(customer.getAddress());
        customer.getAddress().setIdAddress(addressId);

        if (addressId == 0) {
            //Invalid address, customer cannot be stored
            return 0;
        }

        else {
            //All fields are valid, customer may be stored
            int userId = userAccountService.storeOne(customer);
            customer.setIdAccount(userId);
            customerDao.storeOne(customer);

            //TODO: Bank account genereren?

            return userId;
        }
    }

    @Override
    public List<Customer> getAll() {
        return null;
    }

    @Override
    public int updateOne(Customer customer) {
        return 0;
    }

    @Override
    public int deleteOne(int id) {
        return 0;
    }

    private boolean isValidEmail(String email) {
        //RFC 5322 email validation according to Baeldung
        return email.matches("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
    }

    private boolean isValidPassword(String password) {
        //TODO eisen aan wachtwoord formuleren
        return true;
    }

    private boolean isValidBsn(String bsn) {
        //TODO: Als dit via een externe API kan heeft dat de voorkeur, maar die kan ik vooralsnog niet vinden
        final int MIN_LENGTH = 8;
        final int MAX_LENGTH = 9;
        final int[] FACTORS = {9, 8, 7, 6, 5, 4, 3, 2, -1};
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
        return sum % 11 == 0;
    }
}
