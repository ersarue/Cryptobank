package nl.miwteam2.cryptomero.controller;

import nl.miwteam2.cryptomero.domain.CustomerDto;
import nl.miwteam2.cryptomero.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Samuël Geurts & Stijn Klijn
 */

@CrossOrigin
@RestController
@RequestMapping("/users")
public class CustomerController {

    private CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService service) {
        this.customerService = service;
    }
    @CrossOrigin
    @PostMapping("/register")
    public ResponseEntity<?> storeOne(@RequestBody CustomerDto customerDTO) {
        try {
            return new ResponseEntity<>(customerService.storeOne(customerDTO), HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //TODO remove this method?
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable int id) {
        try {
            return new ResponseEntity<>(customerService.findById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
