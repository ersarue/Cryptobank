package nl.miwteam2.cryptomero.controller;

import nl.miwteam2.cryptomero.domain.Customer;
import nl.miwteam2.cryptomero.domain.UserAccount;
import nl.miwteam2.cryptomero.service.Authentication.AuthenticationService;
import nl.miwteam2.cryptomero.service.Authentication.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author MinkTK
 * @version 1.4
 */

@CrossOrigin
@RestController
public class LoginController {
    private final LoginService loginService;
    private final AuthenticationService authenticationService;

    private final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    public LoginController(LoginService loginService, AuthenticationService authenticationService) {
        this.loginService = loginService;
        this.authenticationService = authenticationService;
    }

//  MTK: ROUTE according Acceptance Criteria - return token in header
    @PostMapping("/users/headerAuthentication")
    public ResponseEntity<?> loginHeader(@RequestBody UserAccount userAccount) {
        logger.info("new login attempt");
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", loginService.login(userAccount));
            Customer currentCustomer = loginService.getAuthenticatedCustomer(userAccount.getEmail());
            return new ResponseEntity<>(currentCustomer, headers, HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

//  MTK: ROUTE with credentials in AUTHORIZATION HEADER (basic authentication)
    @GetMapping("/users/basicAuthentication")
    public ResponseEntity<String> login(@RequestHeader ("Authorization") String credentials) throws Exception {
        logger.info("new login Basic Authentication attempt");
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", loginService.login(credentials));
            return new ResponseEntity<>("Login Succesful", headers, HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

//  MTK: TEST methode for frontend - to be continued
    @GetMapping("/users/loginStatus")
    public ResponseEntity<String> statusLogin(@RequestHeader ("Authorization") String jwt) {
        if (authenticationService.authenticateToken(jwt)) {
            return new ResponseEntity<>("ingelogd", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("niet ingelogd", HttpStatus.UNAUTHORIZED);
        }
    }

}
