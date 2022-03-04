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
    public List<BankAccount> showAccounts(){
        return bankAccountService.getAllAccounts();
    }

    @GetMapping("{id}") // http://localhost:8080/bankaccount/1
        public ResponseEntity<String> findBankaccountById(@PathVariable int id) {
        BankAccount bankAccountById = bankAccountService.findById(id);
        if (bankAccountById != null){
            return new ResponseEntity<>("dit bankaccount bestaat", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("bankaccount met deze id niet gevonden", HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping()// http://localhost:8080/bankaccount
    public void storeOne(@RequestBody BankAccount bankAccount) {
        bankAccountService.storeOne(bankAccount);
    }

    @PutMapping// http://localhost:8080/bankaccount
    public void updateBankAccount(@RequestBody BankAccount bankAccount) { bankAccountService.updateBankAccount(bankAccount); }

    @DeleteMapping("/{id}")// http://localhost:8080/bankaccount/1
    public void deleteOne(@PathVariable int id) {
        bankAccountService.deleteOne(id);
    }
}
