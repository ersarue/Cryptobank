package nl.miwteam2.cryptomero.domain;


import java.util.Objects;

/**
 * Model UserAccount
 * @author Marcel Brachten, studentnr: 500893228 - MIW Cohort 26
 */
public class BankAccount {
    private UserAccount userAccount;
    private String iban;
    private double balanceEur;

    public BankAccount(UserAccount userAccount, String iban, double balanceEur) {
        this.userAccount = userAccount;
        this.iban = iban;
        this.balanceEur = balanceEur;
    }
    public BankAccount(String iban, double balanceEur){
        this.userAccount=null;
        this.iban=iban;
        this.balanceEur=balanceEur;
    }
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
