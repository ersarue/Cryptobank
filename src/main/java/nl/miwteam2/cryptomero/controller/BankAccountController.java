package nl.miwteam2.cryptomero.controller;

import nl.miwteam2.cryptomero.domain.BankAccount;
import nl.miwteam2.cryptomero.domain.Customer;
import nl.miwteam2.cryptomero.service.BankAccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BankAccountController {

    private BankAccountService bankAccountService;

    public BankAccountController(BankAccountService bankAccountService){
        this.bankAccountService=bankAccountService;
    }
    @GetMapping(value="/accounts")
    public List<BankAccount> showAccounts(){
        return bankAccountService.getAllAccounts();
    }

    @GetMapping(value = "bankaccount/{id}") // http://localhost:8080/bankaccount/1
    public BankAccount findBankaccountById(@PathVariable int id) {
        //return new BankAccount
        return bankAccountService.findById(id);
    }
}
