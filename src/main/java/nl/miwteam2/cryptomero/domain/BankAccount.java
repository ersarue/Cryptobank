package nl.miwteam2.cryptomero.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;
import java.util.Objects;

/**
 * Model UserAccount
 * @author Marcel Brachten, studentnr: 500893228 - MIW Cohort 26
 */
public class BankAccount {
    @JsonBackReference private UserAccount userAccount;
    private String iban;
    private double balanceEur;

    /** all args constructor
     * @param userAccount from class UserAccount
     */
    public BankAccount(UserAccount userAccount, String iban, double balanceEur) {
        this.userAccount = userAccount;
        this.iban = iban;
        this.balanceEur = balanceEur;
    }
    /** constructor chaining
     * geen parameteer voor userAccount
     */
    public BankAccount(String iban, double balanceEur){
        this.userAccount=null;
        this.iban=iban;
        this.balanceEur=balanceEur;
    }
    /** no args constructor
     * is used during the store bankaccount
     */
    public BankAccount(){
    }
    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public double getBalanceEur() {
        return balanceEur;
    }

    public void setBalanceEur(double balanceEur) {
        this.balanceEur = balanceEur;
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "bankAccount=" + userAccount +
                ", iban='" + iban + '\'' +
                ", balanceEur=" + balanceEur +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankAccount that = (BankAccount) o;
        return Double.compare(that.balanceEur, balanceEur) == 0 && userAccount.equals(that.userAccount) && iban.equals(that.iban);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userAccount, iban, balanceEur);
    }
}
