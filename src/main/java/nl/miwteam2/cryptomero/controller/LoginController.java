package nl.miwteam2.cryptomero.controller;

import nl.miwteam2.cryptomero.domain.UserAccount;
import nl.miwteam2.cryptomero.service.Authentication.AuthenticationService;
import nl.miwteam2.cryptomero.service.Authentication.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * @author MinkTK
 * @version 1.0
 */

@RestController
@RequestMapping("/login")
public class LoginController {

    private final LoginService loginService;
    private final AuthenticationService authenticationService;

    private final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    public LoginController(LoginService service, AuthenticationService service2) {
        loginService = service;
        authenticationService = service2;
        logger.info("New LoginController");
    }

    @PostMapping
    // using a POST method for authentication for security reasons
    // token is now returned as a string todo return as header?
    public ResponseEntity<String> loginUser(@RequestBody UserAccount userAccount) {
        String token = loginService.login(userAccount);
        if (token != null) {
            return new ResponseEntity<>(token, HttpStatus.OK);
        } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    // MTK test methode voor authentication purpose - to be continued
    @GetMapping
    public ResponseEntity<String> statusLogin(@RequestBody String jwt) {
        if (authenticationService.authenticate(jwt)) {
            return new ResponseEntity<>("ingelogd", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("niet ingelogd", HttpStatus.UNAUTHORIZED);

        }
    }

}
