package nl.miwteam2.cryptomero.domain;

import java.sql.Timestamp;
/**
 * DTO Used for the endpoint "/trade/offer"
 * @author: Samuel Geurts en Mink Tielenius Kruijthoff
*/
public class TradeOfferDto {

    private Customer customer;
    private String assetNameOffer;
    private double amountOffer;
    private double rateOffer;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getAssetNameOffer() {
        return assetNameOffer;
    }

    public void setAssetNameOffer(String assetNameOffer) {
        this.assetNameOffer = assetNameOffer;
    }

    public double getAmountOffer() {
        return amountOffer;
    }

    public void setAmountOffer(double amountOffer) {
        this.amountOffer = amountOffer;
    }

    public double getRateOffer() {
        return rateOffer;
    }

    public void setRateOffer(double rateOffer) {
        this.rateOffer = rateOffer;
    }

    @Override
    public String toString() {
        return "TradeOfferDto{" +
                "customer=" + customer +
                ", assetNameOffer='" + assetNameOffer + '\'' +
                ", amountOffer=" + amountOffer +
                ", rateOffer=" + rateOffer +
                '}';
    }
}
