package nl.miwteam2.cryptomero.controller;

import nl.miwteam2.cryptomero.service.Authentication.AuthenticationService;
import nl.miwteam2.cryptomero.service.RateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Stijn Klijn
 */

@RestController
@RequestMapping("/rates")
public class RateController {

    private RateService rateService;
    private AuthenticationService authenticationService;

    @Autowired
    public RateController(RateService rateService, AuthenticationService authenticationService) {
        this.rateService = rateService;
        this.authenticationService = authenticationService;
    }
    @CrossOrigin
    @GetMapping("/latest")
    public ResponseEntity<?> getLatest() {
        try {
            return new ResponseEntity<>(rateService.getLatest(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    @CrossOrigin
    @GetMapping("/latest/{name}")
    public ResponseEntity<?> getLatestByName(@PathVariable String name) {
        try {
            return new ResponseEntity<>(rateService.getLatestByName(name), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    @CrossOrigin
    @GetMapping("/history")
    public ResponseEntity<?> getHistory(@RequestParam String name, @RequestParam String interval, @RequestParam int datapoints) {
        try {
            return new ResponseEntity<>(rateService.getHistory(name, interval, datapoints), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // MTK overview endpoint voor ingelogde gebruikers
    @CrossOrigin
    @GetMapping("/latest/overview")
    public ResponseEntity<?> getOverview(@RequestHeader ("Authorization") String authorizationHeader) {
        if (authenticationService.isValidToken(authorizationHeader)) {
            return new ResponseEntity<>(rateService.getLatest(), HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
