package nl.miwteam2.cryptomero.controller;

import nl.miwteam2.cryptomero.domain.BankAccount;
import nl.miwteam2.cryptomero.service.BankAccountService;
import org.springframework.web.bind.annotation.GetMapping;
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
}
