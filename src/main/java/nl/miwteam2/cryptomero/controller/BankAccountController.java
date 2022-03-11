package nl.miwteam2.cryptomero.controller;

import nl.miwteam2.cryptomero.domain.Address;
import nl.miwteam2.cryptomero.domain.BankAccount;
import nl.miwteam2.cryptomero.domain.Customer;
import nl.miwteam2.cryptomero.service.BankAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * class responsible for all end point requests
 * @author Marcel Brachten, studentnr: 500893228 - MIW Cohort 26
 */
@RestController
@RequestMapping("/bankaccount")
public class BankAccountController {

    private BankAccountService bankAccountService;

    /** constructor
     * @param bankAccountService injection
     */
    public BankAccountController(BankAccountService bankAccountService){
        this.bankAccountService=bankAccountService;
    }
    @GetMapping// http://localhost:8080/bankaccount
    public List<BankAccount> getAll(){
        return bankAccountService.getAll();
    }

    @GetMapping("{id}") // http://localhost:8080/bankaccount/1
        public ResponseEntity<?> findById(@PathVariable int id) {
        try {
            return new ResponseEntity<>(bankAccountService.findById(id), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping()// http://localhost:8080/bankaccount
    public ResponseEntity<?> storeOne(@RequestBody BankAccount bankAccount) {
        try {
            return new ResponseEntity<>(bankAccountService.storeOne(bankAccount), HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping// http://localhost:8080/bankaccount
    public ResponseEntity<?> updateOne(@RequestBody BankAccount bankAccount) {
        try {
            return new ResponseEntity<>(bankAccountService.updateOne(bankAccount), HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")// http://localhost:8080/bankaccount/1
    public ResponseEntity<?> deleteOne(@PathVariable int id) {
        try {
            return new ResponseEntity<>(bankAccountService.deleteOne(id),HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }
}
