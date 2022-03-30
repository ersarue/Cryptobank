package nl.miwteam2.cryptomero.controller;

import nl.miwteam2.cryptomero.domain.Customer;
import nl.miwteam2.cryptomero.domain.OfferDto;
import nl.miwteam2.cryptomero.domain.TradeBankDto;
import nl.miwteam2.cryptomero.service.Authentication.AuthenticationService;
import nl.miwteam2.cryptomero.service.OfferService;
import nl.miwteam2.cryptomero.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Endpoints for trade with the bank of with the marketplace
 * @author: Samuel Geurts, studentnr: 500893275 - MIW Cohort 26
 * version 1.0
*/

@CrossOrigin
@RestController
@RequestMapping("/trade")
public class TradeController {

    private TransactionService transactionService;
    private OfferService offerService;
    private AuthenticationService authenticationService;

    @Autowired
    public TradeController(TransactionService transactionService, OfferService offerService, AuthenticationService authenticationService) {
        this.transactionService = transactionService;
        this.offerService = offerService;
        this.authenticationService = authenticationService;
    }

    @CrossOrigin
    @PostMapping("/offer")
    public ResponseEntity<?> storeOneOffer(@RequestBody OfferDto offerDto, @RequestHeader ("Authorization") String jwt) {
        Customer authenticatedCustomer = authenticationService.getAuthenticatedCustomer(jwt);
        offerDto.setIdAccountOffer(authenticatedCustomer.getIdAccount());
        //offerDto.setIdAccountOffer(1);
        //todo vervang bovenstaande regels door als Mink de methode getAuthenticatedAccountId heeft geimplementeerd
        // offerDto.setIdAccountOffer(authenticationService.getAuthenticatedAccountId(jwt);

        if (authenticatedCustomer != null) {
            try {
                return new ResponseEntity<>(offerService.storeOne(offerDto), HttpStatus.CREATED);
            } catch (Exception e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @CrossOrigin
    @PostMapping("/bank")
    public ResponseEntity<?> tradeWithBank(@RequestBody TradeBankDto tradeBankDto, @RequestHeader ("Authorization") String jwt) {
        Customer authenticatedCustomer = authenticationService.getAuthenticatedCustomer(jwt);
        tradeBankDto.setIdAccountTrade(authenticatedCustomer.getIdAccount());
        //todo vervang bovenstaande regels door als Mink de methode getAuthenticatedAccountId heeft geimplementeerd
        // offerDto.setIdAccountOffer(authenticationService.getAuthenticatedAccountId(jwt);

        if (authenticatedCustomer != null) {
            try {
                return new ResponseEntity<>(TransactionService.tradeWithBank(tradeBankDto), HttpStatus.CREATED);
            } catch (Exception e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }



}
