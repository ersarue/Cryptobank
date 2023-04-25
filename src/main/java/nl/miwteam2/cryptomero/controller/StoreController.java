package nl.miwteam2.cryptomero.controller;

import nl.miwteam2.cryptomero.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * class responsible for storing 3000 customers
 * @author Marcel Brachten, studentnr: 500893228 - MIW Cohort 26
 */

@RestController
public class StoreController {
    private StoreService storeService;
    @Autowired
    public StoreController(StoreService service) {
        this.storeService = service;
    }
    @CrossOrigin
    @PostMapping("/CustomerBulkStore/{aantal}")
    public ResponseEntity<?> store3000Customers(@PathVariable int aantal) {
        try {
            return new ResponseEntity<>(storeService.storeExtraCustomers(aantal), HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
