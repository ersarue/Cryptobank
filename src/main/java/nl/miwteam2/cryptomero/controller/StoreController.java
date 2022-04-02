package nl.miwteam2.cryptomero.controller;

import nl.miwteam2.cryptomero.domain.CustomerDto;
import nl.miwteam2.cryptomero.service.CustomerService;
import nl.miwteam2.cryptomero.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//controller voor het store van 3000 klanten

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
