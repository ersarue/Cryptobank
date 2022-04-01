package nl.miwteam2.cryptomero.controller;

import nl.miwteam2.cryptomero.domain.Transaction;
import nl.miwteam2.cryptomero.service.Authentication.AuthenticationService;
import nl.miwteam2.cryptomero.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
* @author Petra Coenen
*/

@RestController
@CrossOrigin
public class HistoryController {

    private HistoryService historyService;
    private AuthenticationService authService;

    @Autowired
    public HistoryController(HistoryService historyService, AuthenticationService authService) {
        this.historyService = historyService;
        this.authService = authService;
    }

    @GetMapping("/history")
    public ResponseEntity<List<Transaction>> getAllHistory(@RequestHeader("Authorization") String token) {
        int idUserAccount = authService.getAuthenticatedIdAccount(token);
        if (idUserAccount != 0) {
            List<Transaction> history = historyService.getAllHistory(idUserAccount);
            return new ResponseEntity<>(history, HttpStatus.OK);
        } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
}