package nl.miwteam2.cryptomero.domain;

import java.sql.Timestamp;
/**
 * DTO Used for the endpoint "/trade/offer"
 * @author: Samuel Geurts en Mink Tielenius Kruijthoff
*/
public class TradeOfferDto {

    private int idAccountOffer;
    private String assetNameOffer;
    private double amountOffer;
    private double rateOffer;

    public int getIdAccountOffer() {
        return idAccountOffer;
    }

    public void setIdAccountOffer(int idAccountOffer) {
        this.idAccountOffer = idAccountOffer;
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
                "idAccountOffer=" + idAccountOffer +
                ", assetNameOffer='" + assetNameOffer + '\'' +
                ", amountOffer=" + amountOffer +
                ", rateOffer=" + rateOffer +
                '}';
    }
}
