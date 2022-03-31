package nl.miwteam2.cryptomero.domain;

import java.sql.Timestamp;
/**
 * DTO Used for the endpoint "/trade/bank"
 * @author: Samuel Geurts
 * version 1.0
 */
public class TradeBankDto {

    private Customer customer;
    private String assetNameTrade;
    private double amountTrade;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getAssetNameTrade() {
        return assetNameTrade;
    }

    public void setAssetNameTrade(String assetNameTrade) {
        this.assetNameTrade = assetNameTrade;
    }

    public double getAmountTrade() {
        return amountTrade;
    }

    public void setAmountTrade(double amountTrade) {
        this.amountTrade = amountTrade;
    }

    @Override
    public String toString() {
        return "TradeBankDto{" +
                "customer=" + customer +
                ", assetNameTrade='" + assetNameTrade + '\'' +
                ", amountTrade=" + amountTrade + '}';
    }
}
