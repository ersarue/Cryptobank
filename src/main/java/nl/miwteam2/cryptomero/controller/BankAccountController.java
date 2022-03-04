package nl.miwteam2.cryptomero.controller;

import nl.miwteam2.cryptomero.domain.BankAccount;
import nl.miwteam2.cryptomero.domain.Customer;
import nl.miwteam2.cryptomero.service.BankAccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bankaccount")
public class BankAccountController {

    private BankAccountService bankAccountService;

    public BankAccountController(BankAccountService bankAccountService){
        this.bankAccountService=bankAccountService;
    }
    @GetMapping(value="")// http://localhost:8080/bankaccount
    public List<BankAccount> showAccounts(){
        return bankAccountService.getAllAccounts();
    }

    @GetMapping(value = "{id}") // http://localhost:8080/bankaccount/1
    public BankAccount findBankaccountById(@PathVariable int id) {
        //return new BankAccount
        return bankAccountService.findById(id);
    }
    @PostMapping(value = "")// http://localhost:8080/bankaccount
    public void storeOne(@RequestBody BankAccount bankAccount) {
        System.out.println(bankAccount);
        bankAccountService.storeOne(bankAccount);
    }
}
