package nl.miwteam2.cryptomero.controller;

import nl.miwteam2.cryptomero.domain.Customer;
import nl.miwteam2.cryptomero.service.Authentication.AuthenticationService;
import nl.miwteam2.cryptomero.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileController {

    private CustomerService customerService;
    private AuthenticationService authenticationService;

    @Autowired
    public ProfileController(CustomerService customerService, AuthenticationService authenticationService) {
        this.customerService = customerService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/portfolio/assets")
    public ResponseEntity<?> getPortfolio(@RequestHeader ("Authorization") String jwt) {
        Customer authenticatedCustomer = authenticationService.authenticateCustomer(jwt);
        if (authenticatedCustomer != null) {
            return new ResponseEntity<> (authenticatedCustomer, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("niet ingelogd", HttpStatus.UNAUTHORIZED);
        }
    }
}
