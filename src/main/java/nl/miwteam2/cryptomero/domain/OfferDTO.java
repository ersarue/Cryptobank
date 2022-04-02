package nl.miwteam2.cryptomero.domain;

import java.sql.Timestamp;

public class OfferDTO {
    private int idOffer;
    private int idAccountOffer;
    private String assetNameOffer;
    private double amountOffer;
    private double priceOffer;
    private Timestamp timestampOffer;

    public OfferDTO(int idOffer, int idAccountOffer, String assetNameOffer, double amountOffer, double priceOffer, Timestamp timestampOffer) {
        this.idOffer = idOffer;
        this.idAccountOffer = idAccountOffer;
        this.assetNameOffer = assetNameOffer;
        this.amountOffer = amountOffer;
        this.priceOffer = priceOffer;
        this.timestampOffer = timestampOffer;
    }

    public int getIdOffer() {
        return idOffer;
    }

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
        return "OfferDTO{" +
                "idOffer=" + idOffer +
                ", idAccountOffer=" + idAccountOffer +
                ", assetNameOffer='" + assetNameOffer + '\'' +
                ", amountOffer=" + amountOffer +
                ", priceOffer=" + priceOffer +
                ", timestampOffer=" + timestampOffer +
                '}';
    }
}
