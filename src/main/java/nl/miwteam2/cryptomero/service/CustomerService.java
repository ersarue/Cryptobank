package nl.miwteam2.cryptomero.service;

import nl.miwteam2.cryptomero.domain.Customer;
import nl.miwteam2.cryptomero.repository.GenericDao;
import nl.miwteam2.cryptomero.repository.RootRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        //TODO void houden of id teruggeven als int?

        userAccountService.storeOne(customer);
        int value = addressService.storeAddress(customer.getAddress());
        customerDao.storeOne(customer);
        return customer.getIdAccount();
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


}
