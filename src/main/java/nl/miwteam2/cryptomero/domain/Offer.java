package nl.miwteam2.cryptomero.domain;

import java.sql.Timestamp;

public class Offer {
    private int idOffer;
    private UserAccount userOffer;
    private Asset assetOffer;
    private double amountOffer;
    private double rateOffer;
    private Timestamp timestampOffer;

    // all args constructor
    public Offer(int idOffer, UserAccount userOffer, Asset assetOffer, double amountOffer, double rateOffer, Timestamp timestampOffer) {
        this.idOffer = idOffer;
        this.userOffer = userOffer;
        this.assetOffer = assetOffer;
        this.amountOffer = amountOffer;
        this.rateOffer = rateOffer;
        this.timestampOffer = timestampOffer;
    }

    // constructor DTO
    public Offer(UserAccount userOffer, Asset assetOffer, OfferDTO offerDTO) {
        this.setIdOffer(offerDTO.getIdOffer());
        this.userOffer = userOffer;
        this.assetOffer = assetOffer;
        this.setAmountOffer(offerDTO.getAmountOffer());
        this.setRateOffer(offerDTO.getRateOffer());
        this.setTimestampOffer(offerDTO.getTimestampOffer());
    }

    public int getIdOffer() {
        return idOffer;
    }

    public void setIdOffer(int idOffer) {
        this.idOffer = idOffer;
    }

    public UserAccount getUserOffer() {
        return userOffer;
    }

    public void setUserOffer(UserAccount userOffer) {
        this.userOffer = userOffer;
    }

    public Asset getAssetOffer() {
        return assetOffer;
    }

    public void setAssetOffer(Asset assetOffer) {
        this.assetOffer = assetOffer;
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

    public Timestamp getTimestampOffer() {
        return timestampOffer;
    }

    public void setTimestampOffer(Timestamp timestampOffer) {
        this.timestampOffer = timestampOffer;
    }

    @Override
    public String toString() {
        return "Offer{" +
                "idOffer=" + idOffer +
                ", userOffer=" + userOffer.getIdAccount() +
                ", assetOffer=" + assetOffer.getAssetName() +
                ", amountOffer=" + amountOffer +
                ", rateOffer=" + rateOffer +
                ", timestampOffer=" + timestampOffer +
                '}';
    }
}


