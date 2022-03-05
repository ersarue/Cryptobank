package nl.miwteam2.cryptomero.controller;

import nl.miwteam2.cryptomero.domain.Customer;
import nl.miwteam2.cryptomero.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService service) {
        this.customerService = service;
    }

    @PostMapping
    public int storeOne(@RequestBody Customer customer) {
        return customerService.storeOne(customer);
    }

    @GetMapping("/{id}")
    public Customer findById(@PathVariable int id) {
        //return new Customer();
        return customerService.findById(id);
    }

    @GetMapping
    public List<Customer> getAll(){
        //Omitted until required
        return null;
    }

    @PutMapping
    public int updateOne(@RequestBody Customer customer){
        //todo customerService.updateCustomer();
        return customer.getIdAccount();
    }

    @DeleteMapping
    public int deleteOne(@RequestBody Customer customer) {
        //todo customerService.deleteCustomer();
        return customer.getIdAccount();
    }


}
