package nl.miwteam2.cryptomero.controller;

import nl.miwteam2.cryptomero.domain.CustomerDto;
import nl.miwteam2.cryptomero.service.CustomerService;
import nl.miwteam2.cryptomero.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//controller voor het store van 3000 klanten

@RestController
public class StoreController {
    private StoreService storeService;
    @Autowired
    public StoreController(StoreService service) {
        this.storeService = service;
    }
    @CrossOrigin
    @PostMapping("/oefenen")
    public ResponseEntity<?> oefenOne(CustomerDto customerDTO) {
        try {
            return new ResponseEntity<>(storeService.storePractise(customerDTO), HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
