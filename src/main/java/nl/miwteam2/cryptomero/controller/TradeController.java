package nl.miwteam2.cryptomero.controller;

import nl.miwteam2.cryptomero.domain.Customer;
import nl.miwteam2.cryptomero.domain.TradeOfferDto;
import nl.miwteam2.cryptomero.domain.TradeBankDto;
import nl.miwteam2.cryptomero.service.Authentication.AuthenticationService;
import nl.miwteam2.cryptomero.service.OfferService;
import nl.miwteam2.cryptomero.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Endpoints for trade with the bank or with the marketplace
*/

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

    /**
     * takes al data to be able to place an offer in the marketplace
     * @param tradeOfferDto
     * @param jwt
     * @return
     */
    @PostMapping("/offer")
    public ResponseEntity<?> storeOneOffer(@RequestBody TradeOfferDto tradeOfferDto, @RequestHeader ("Authorization") String jwt) {
        Customer customer = authenticationService.getAuthenticatedCustomer(jwt);
        tradeOfferDto.setCustomer(customer);
        if (customer != null) {
            try {
                offerService.checkOfferValidity(tradeOfferDto);
                return new ResponseEntity<>(offerService.tradeOffer(tradeOfferDto), HttpStatus.CREATED);
            } catch (Exception e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * takes al data to be able to perform a transaction with the bank
     * @param tradeBankDto
     * @param jwt transparant token containing customerId
     * @return
     */
    @PostMapping("/bank")
    public ResponseEntity<?> tradeWithBank(@RequestBody TradeBankDto tradeBankDto, @RequestHeader ("Authorization") String jwt) {
        Customer customer = authenticationService.getAuthenticatedCustomer(jwt);
        tradeBankDto.setCustomer(customer);
        if (customer != null) {
            try {
                return new ResponseEntity<>(transactionService.tradeWithBank(tradeBankDto), HttpStatus.CREATED);
            } catch (Exception e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    /**
     * retrieves all offers performed by the customer that is logged in
     * @param jwt transparant token containing customerId
     * @return
     */
    @GetMapping("/getoffers")
    public ResponseEntity<?> getAll(@RequestHeader ("Authorization") String jwt) {
        int accountId = authenticationService.getAuthenticatedIdAccount(jwt);

        if (accountId != 0) {
            try {
                return new ResponseEntity<>(offerService.findAllByIdAccount(accountId), HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * delete an offer clicked by the logged in customer
     * @param offerId
     * @param jwt transparant token containing customerId
     * @return
     */
    // todo delete /trade/deleteoffer (persoonlijk offer deleten)
    @CrossOrigin
    @DeleteMapping("/deleteoffer/{offerId}")
    public ResponseEntity<?> deleteOffers(@PathVariable int offerId, @RequestHeader ("Authorization") String jwt) {
        int accountId = authenticationService.getAuthenticatedIdAccount(jwt);

        if (accountId != 0) {
            try {
                return new ResponseEntity<>(offerService.deleteOne(accountId,offerId), HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
