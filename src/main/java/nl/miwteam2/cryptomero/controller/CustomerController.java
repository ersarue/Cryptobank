package nl.miwteam2.cryptomero.controller;

import nl.miwteam2.cryptomero.domain.Address;
import nl.miwteam2.cryptomero.domain.Customer;
import nl.miwteam2.cryptomero.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
//@RequestMapping(value = "/customer",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
@RequestMapping("/customer")

//@RequestMapping(value = "/customer")
public class CustomerController {

    private CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService service) {
        this.customerService = service;
    }

    @GetMapping(value = "/{id}") // http://localhost:8080/customer/1
    public Customer findById(@PathVariable int id) {
        //return new Customer();
        return customerService.findById(id);
    }

    @PostMapping(value = "/register")
    public void storeOne(@RequestBody Customer customer) {
        System.out.println(customer);
        customerService.storeOne(customer);
    }

}
