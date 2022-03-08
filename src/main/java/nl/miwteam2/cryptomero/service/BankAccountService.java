package nl.miwteam2.cryptomero.service;

import nl.miwteam2.cryptomero.domain.Address;
import nl.miwteam2.cryptomero.domain.BankAccount;
import nl.miwteam2.cryptomero.domain.Customer;
import nl.miwteam2.cryptomero.repository.GenericDao;
import nl.miwteam2.cryptomero.repository.JdbcBankAccountDao;
import nl.miwteam2.cryptomero.repository.RootRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

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

    /**
     * Generates valid IBAN number
     * @author          Stijn Klijn
     * @return          Valid IBAN number
     */
    public String generateIban() {
        final BigDecimal DIVISOR = new BigDecimal(97), SUBTRACT_FROM = new BigDecimal(98);
        final String countryCode = "NL", bankCode = "CRME";

        String iban = "";

        do {
            Random random = new Random();
            String accountNumber = String.format("%010d", random.nextLong(10000000000L));
            StringBuilder interimString = new StringBuilder(bankCode).append(accountNumber).append(countryCode);
            StringBuilder interimString2 = new StringBuilder();

            for (int i = 0; i < interimString.length(); i++) {
                interimString2.append(Character.isAlphabetic(interimString.charAt(i)) ?
                        interimString.charAt(i) - 'A' + 10 :
                        Character.toString(interimString.charAt(i)));
            }

            interimString2.append("00");
            String controlNumber = String.format("%02d",
                    SUBTRACT_FROM.subtract(new BigDecimal(interimString2.toString()).remainder(DIVISOR)).intValue());
            iban = countryCode + controlNumber + bankCode + accountNumber;

        } while (isIbanAlreadyInUse(iban));

        return iban;
    }

    /**
     * Check whether IBAN is already in use
     * @author          Stijn Klijn
     * @param iban      IBAN to check
     * @return          Boolean representing whether IBAN is already in use
     */
    private boolean isIbanAlreadyInUse(String iban) {
        for (BankAccount bankAccount : rootRepository.getAll()) {
            if (iban.equals(bankAccount.getIban())) {
                return true;
            }
        }
        return false;
    }
}
