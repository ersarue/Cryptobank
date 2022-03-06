package nl.miwteam2.cryptomero.domain;

import java.time.LocalDate;
import java.util.Map;

/**
 * @author Samuël Geurts & Stijn Klijn
 */

public class Customer extends UserAccount {

    private String firstName;
    private String namePrefix;
    private String lastName;
    private LocalDate dob;
    private String bsn;
    private String telephone;
    private Address address;
    private BankAccount bankAccount;
    private Map<String,Double> wallet;

    // in de constructor alleen primitieve variabelen
    // de rootrepo gebruikt setters om de verwijzingen naar andere objecten te setten
    //public Customer(String email, String password, String firstName, String namePrefix, String lastName, LocalDate dob, String bsn, String telephone) {
    public Customer(String firstName, String namePrefix, String lastName, LocalDate dob, String bsn, String telephone) {

        this.firstName = firstName;
        this.namePrefix = namePrefix;
        this.lastName = lastName;
        this.dob = dob;
        this.bsn = bsn;
        this.telephone = telephone;
    }
//    public Customer(){
//        //todo constructor chain of verwijder
//    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getNamePrefix() {
        return namePrefix;
    }

    public void setNamePrefix(String namePrefix) {
        this.namePrefix = namePrefix;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getBsn() {
        return bsn;
    }

    public void setBsn(String bsn) {
        this.bsn = bsn;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public Map<String, Double> getWallet() {
        return wallet;
    }

    public void setWallet(Map<String, Double> wallet) {
        this.wallet = wallet;
    }

    @Override
    public String toString() {
        return String.format("%s \n Klant met BSN %s", super.toString(), bsn);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Customer customer = (Customer) o;

        if (!firstName.equals(customer.firstName)) return false;
        if (namePrefix != null ? !namePrefix.equals(customer.namePrefix) : customer.namePrefix != null) return false;
        if (!lastName.equals(customer.lastName)) return false;
        if (!dob.equals(customer.dob)) return false;
        if (!bsn.equals(customer.bsn)) return false;
        if (!telephone.equals(customer.telephone)) return false;
        if (address != null ? !address.equals(customer.address) : customer.address != null) return false;
        if (bankAccount != null ? !bankAccount.equals(customer.bankAccount) : customer.bankAccount != null)
            return false;
        return wallet != null ? wallet.equals(customer.wallet) : customer.wallet == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + firstName.hashCode();
        result = 31 * result + (namePrefix != null ? namePrefix.hashCode() : 0);
        result = 31 * result + lastName.hashCode();
        result = 31 * result + dob.hashCode();
        result = 31 * result + bsn.hashCode();
        result = 31 * result + telephone.hashCode();
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (bankAccount != null ? bankAccount.hashCode() : 0);
        result = 31 * result + (wallet != null ? wallet.hashCode() : 0);
        return result;
    }
}
