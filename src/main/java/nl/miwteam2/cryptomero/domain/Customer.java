package nl.miwteam2.cryptomero.domain;

import java.time.LocalDate;
import java.util.Map;

public class Customer extends UserAccount {

    private String firstName;
    private String namePrefix;
    private String lastName;
    private LocalDate dob;
    private String bsn;
    private String telephone;
    private Address address;
    private BankAccount bankAccount;
    private Map<Asset,Double> wallet;

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
    public Customer(){
        //todo constructor chain of verwijder
    }

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

    public Map<Asset, Double> getWallet() {
        return wallet;
    }

    public void setWallet(Map<Asset, Double> wallet) {
        this.wallet = wallet;
    }

    @Override
    public String toString() {
        return String.format("Klant met BSN %s", bsn);
    }

}
