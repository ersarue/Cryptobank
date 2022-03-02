package nl.miwteam2.cryptomero.service;

import nl.miwteam2.cryptomero.domain.Customer;
import nl.miwteam2.cryptomero.repository.GenericDao;
import nl.miwteam2.cryptomero.repository.RootRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private GenericDao<Customer> customerDao;
    private RootRepository rootRepository;

    @Autowired
    public CustomerService(GenericDao<Customer> dao, RootRepository rootRepository) {
        this.rootRepository = rootRepository;
        this.customerDao = dao;
    }

    public Customer findById(int id) {
        return rootRepository.findCustomerById(id);
    }

}
