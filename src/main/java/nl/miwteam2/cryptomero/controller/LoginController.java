package nl.miwteam2.cryptomero.controller;

import nl.miwteam2.cryptomero.domain.UserAccountDTO;
import nl.miwteam2.cryptomero.service.Authentication.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author MinkTK
 * @version 1.5
 */

@CrossOrigin
@RestController
public class LoginController {
    private final LoginService loginService;
    private final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/users/authenticate")
    public ResponseEntity<?> login(@RequestBody UserAccountDTO credentials) {
        logger.info("new login attempt");
        String token = loginService.login(credentials);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {
            return new ResponseEntity<>(token, HttpStatus.OK);
        }
    }
}
