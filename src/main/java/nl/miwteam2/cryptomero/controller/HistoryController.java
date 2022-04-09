package nl.miwteam2.cryptomero.controller;

import nl.miwteam2.cryptomero.domain.Transaction;
import nl.miwteam2.cryptomero.service.Authentication.AuthenticationService;
import nl.miwteam2.cryptomero.service.HistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
* Controller contains endpoint for requesting all transactions in which the current user was involved as either buyer
* or seller of crypto assets.
* @author Petra Coenen
*/

@RestController
@CrossOrigin
public class HistoryController {

    private final Logger logger = LoggerFactory.getLogger(HistoryController.class);

    private HistoryService historyService;
    private AuthenticationService authService;

    @Autowired
    public HistoryController(HistoryService historyService, AuthenticationService authService) {
        this.historyService = historyService;
        this.authService = authService;
        logger.info("New HistoryController");
    }

    /**
    * Returns all transactions in which the current user was involved as either buyer or seller of a crypto asset.
    * @param  token    The token created when the user logged in to the application.
    * @return          A list of all the transactions in which the user was involved.
    * @throws ResponseStatusException
    */
    @GetMapping("/history")
    public ResponseEntity<List<Transaction>> getAllHistory(@RequestHeader("Authorization") String token) {
        int idUserAccount = authService.getAuthenticatedIdAccount(token);
        if (idUserAccount != 0) {
            List<Transaction> history = historyService.getAllHistory(idUserAccount);
            return new ResponseEntity<>(history, HttpStatus.OK);
        } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

//    DO NOT USE IN CRYPTOMERO PROJECT! Method included for testing purposes (portfolio) only!
    @GetMapping("/testHistory/{id}")
    public ResponseEntity<List<Transaction>> testGetAllHistory(@PathVariable int id) {
            List<Transaction> history = historyService.getAllHistory(id);
            return new ResponseEntity<>(history, HttpStatus.OK);
    }
}