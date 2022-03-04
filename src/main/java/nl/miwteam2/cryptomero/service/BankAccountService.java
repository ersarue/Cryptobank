package nl.miwteam2.cryptomero.service;

import nl.miwteam2.cryptomero.domain.BankAccount;
import nl.miwteam2.cryptomero.domain.Customer;
import nl.miwteam2.cryptomero.repository.GenericDao;
import nl.miwteam2.cryptomero.repository.JdbcBankAccountDao;
import nl.miwteam2.cryptomero.repository.RootRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return jdbcBankAccountDao.getAll();
    }

    public BankAccount findById(int id) {
        return rootRepository.findBankaccountById(id);
    }
    public void storeOne(BankAccount bankAccount) {
        //TODO void houden of id teruggeven als int?
        jdbcBankAccountDao.storeOne(bankAccount);

    }

}
