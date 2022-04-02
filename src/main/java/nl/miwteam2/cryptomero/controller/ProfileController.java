package nl.miwteam2.cryptomero.controller;

import nl.miwteam2.cryptomero.domain.Customer;
import nl.miwteam2.cryptomero.service.Authentication.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProfileController {

    private AuthenticationService authenticationService;

    @Autowired
    public ProfileController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    // TODO: 25-03-22 change url when ready
    @GetMapping("/portfolio/assets")
    public ResponseEntity<?> getPortfolio(@RequestHeader ("Authorization") String authorizationHeader) {
        Customer authenticatedCustomer = authenticationService.getAuthenticatedCustomer(authorizationHeader);
        if (authenticatedCustomer != null) {
            return new ResponseEntity<> (authenticatedCustomer, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
