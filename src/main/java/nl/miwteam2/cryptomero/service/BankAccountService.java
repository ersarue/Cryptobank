package nl.miwteam2.cryptomero.service;

import nl.miwteam2.cryptomero.domain.BankAccount;
import nl.miwteam2.cryptomero.repository.JdbcBankAccountDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankAccountService {
    private JdbcBankAccountDao jdbcBankAccountDao;

    @Autowired
    public BankAccountService(JdbcBankAccountDao jdbcBankAccountDao){
        this.jdbcBankAccountDao=jdbcBankAccountDao;
    }
    public List<BankAccount> getAllAccounts(){
        return jdbcBankAccountDao.getAll();
    }

}
