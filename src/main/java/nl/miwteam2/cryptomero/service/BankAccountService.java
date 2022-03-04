package nl.miwteam2.cryptomero.service;

import nl.miwteam2.cryptomero.domain.Address;
import nl.miwteam2.cryptomero.domain.BankAccount;
import nl.miwteam2.cryptomero.domain.Customer;
import nl.miwteam2.cryptomero.repository.GenericDao;
import nl.miwteam2.cryptomero.repository.JdbcBankAccountDao;
import nl.miwteam2.cryptomero.repository.RootRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * @author Marcel Brachten, studentnr: 500893228 - MIW Cohort 26
 */
@Service
public class BankAccountService {
    private JdbcBankAccountDao jdbcBankAccountDao;
    private RootRepository rootRepository;
    @Autowired
    public BankAccountService(JdbcBankAccountDao jdbcBankAccountDao, RootRepository rootRepository){
        this.jdbcBankAccountDao=jdbcBankAccountDao;
        this.rootRepository=rootRepository;
    }
    public List<BankAccount> getAllAccounts(){
        return rootRepository.getAll();
    }

    public BankAccount findById(int id) {
        return jdbcBankAccountDao.findById(id);
    }
    public void storeOne(BankAccount bankAccount) {
        jdbcBankAccountDao.storeOne(bankAccount);

    }
    public int updateBankAccount(BankAccount bankAccount) { return jdbcBankAccountDao.updateOne(bankAccount); }

    public void deleteOne(int id) {
        jdbcBankAccountDao.deleteOne(id);
    }
}
