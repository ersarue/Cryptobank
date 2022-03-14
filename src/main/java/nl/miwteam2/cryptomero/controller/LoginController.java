package nl.miwteam2.cryptomero.controller;

import com.auth0.jwt.exceptions.JWTCreationException;
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
import org.springframework.web.server.ResponseStatusException;

/**
 * @author MinkTK
 * @version 1.0
 */

@RestController
@RequestMapping("/login")
public class LoginController {

    private final LoginService loginService;
//    private final AuthenticationService authenticationService;

    private final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    public LoginController(LoginService loginService, AuthenticationService authenticationService) {
        this.loginService = loginService;
//        this.authenticationService = authenticationService;
    }

    @PostMapping
    // token is now returned in body todo return as header
    public ResponseEntity<String> loginUser(@RequestBody UserAccount userAccount) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("accessToken", loginService.login(userAccount));
            return new ResponseEntity<>("Login Succesful", headers, HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

//    // MTK test methode voor authentication purpose - to be continued
//    @GetMapping
//    public ResponseEntity<String> statusLogin(@RequestBody String jwt) {
//        if (authenticationService.authenticate(jwt)) {
//            return new ResponseEntity<>("ingelogd", HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>("niet ingelogd", HttpStatus.UNAUTHORIZED);
//        }
//    }

}
