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
@RequestMapping("/history")
public class HistoryController {

    private HistoryService historyService;
    private AuthenticationService authService;

    @Autowired
    public HistoryController(HistoryService historyService, AuthenticationService authService) {
        this.historyService = historyService;
        this.authService = authService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<Transaction>> getAllHistory(@PathVariable int id, @RequestHeader("Authorization") String token) {
        if (authService.isValidToken(token)) {
            List<Transaction> history = historyService.getAllHistory(id);
            return new ResponseEntity<>(history, HttpStatus.OK);
        } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
}
