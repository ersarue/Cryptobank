package nl.miwteam2.cryptomero.domain;

import java.sql.Timestamp;

public class Offer {
    private int idOffer;
    private UserAccount userOffer;
    private Asset assetOffer;
    private double amountOffer;
    private double priceOffer;
    private Timestamp timestampOffer;

    // all args constructor
    public Offer(int idOffer, UserAccount userOffer, Asset assetOffer, double amountOffer, double priceOffer, Timestamp timestampOffer) {
        this.idOffer = idOffer;
        this.userOffer = userOffer;
        this.assetOffer = assetOffer;
        this.amountOffer = amountOffer;
        this.priceOffer = priceOffer;
        this.timestampOffer = timestampOffer;
    }

    // constructor DTO
    public Offer(UserAccount userOffer, Asset assetOffer, OfferDTO offerDTO) {
        this.setIdOffer(offerDTO.getIdOffer());
        this.userOffer = userOffer;
        this.assetOffer = assetOffer;
        this.setAmountOffer(offerDTO.getAmountOffer());
        this.setPriceOffer(offerDTO.getPriceOffer());
        this.setTimestampOffer(offerDTO.getTimestampOffer());
    }

    public Offer(TradeOfferDto tradeOfferDto) {
        this.setAmountOffer(tradeOfferDto.getAmountOffer());
        this.setPriceOffer(tradeOfferDto.getRateOffer());
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

    public double getPriceOffer() {
        return priceOffer;
    }

    public void setPriceOffer(double priceOffer) {
        this.priceOffer = priceOffer;
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
                ", priceOffer=" + priceOffer +
                ", timestampOffer=" + timestampOffer +
                '}';
    }
}


