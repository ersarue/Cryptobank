package nl.miwteam2.cryptomero.service;

import nl.miwteam2.cryptomero.domain.Customer;
import nl.miwteam2.cryptomero.repository.GenericDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private GenericDao<Customer> customerDao;

    @Autowired
    public CustomerService(GenericDao<Customer> dao) {
        this.customerDao = dao;
    }

    public Customer findById(int id) {
        return customerDao.findById(id);
    }

}
