package nl.miwteam2.cryptomero.controller;

import nl.miwteam2.cryptomero.domain.Transaction;
import nl.miwteam2.cryptomero.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
* @author Petra Coenen
*/

@RestController
@CrossOrigin
@RequestMapping("/history")
public class HistoryController {

    private HistoryService historyService;

    @Autowired
    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<Transaction>> getAllHistory(@PathVariable int id) {
        List<Transaction> history = historyService.getAllHistory(id);
        return new ResponseEntity<>(history, HttpStatus.OK);
    }
}
