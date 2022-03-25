package nl.miwteam2.cryptomero.service;

import nl.miwteam2.cryptomero.domain.BankAccount;
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
    public List<BankAccount> getAll(){
        return rootRepository.getAll();
    }

    public BankAccount findById(int id) {
        return rootRepository.findBankaccountById(id);
    }
    public BankAccount storeOne(BankAccount bankAccount) {
        jdbcBankAccountDao.storeOne(bankAccount);
        return bankAccount;
    }
    public BankAccount updateOne(BankAccount bankAccount) {
        jdbcBankAccountDao.updateOne(bankAccount);
        return bankAccount;
    }

    public int deleteOne(int id) {
        return jdbcBankAccountDao.deleteOne(id);

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
            String accountNumber = generateAccountNumber();
            StringBuilder interimString = new StringBuilder(bankCode).append(accountNumber).append(countryCode);
            interimString = convertToIntegers(interimString);
            interimString.append("00");
            String controlNumber = String.format("%02d",
                    SUBTRACT_FROM.subtract(new BigDecimal(interimString.toString()).remainder(DIVISOR)).intValue());
            iban = countryCode + controlNumber + bankCode + accountNumber;
        } while (isIbanAlreadyInUse(iban));
        return iban;
    }

    private String generateAccountNumber() {
        Random random = new Random();
        StringBuilder accountNumber = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            accountNumber.append(random.nextInt(10));
        }
        return accountNumber.toString();
    }

    private StringBuilder convertToIntegers(StringBuilder interimString) {
        StringBuilder interimString2 = new StringBuilder();
        for (int i = 0; i < interimString.length(); i++) {
            interimString2.append(Character.isAlphabetic(interimString.charAt(i)) ?
                    interimString.charAt(i) - 'A' + 10 :
                    Character.toString(interimString.charAt(i)));
        }
        return interimString2;
    }

    private boolean isIbanAlreadyInUse(String iban) {
        for (BankAccount bankAccount : rootRepository.getAll()) {
            if (iban.equals(bankAccount.getIban())) {
                return true;
            }
        }
        return false;
    }
}
