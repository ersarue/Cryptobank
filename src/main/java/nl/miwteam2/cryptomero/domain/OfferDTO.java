package nl.miwteam2.cryptomero.domain;

import java.sql.Timestamp;

public class OfferDTO {
    private int idOffer;
    private int idAccountOffer;
    private String assetNameOffer;
    private double amountOffer;
    private double rateOffer;
    private Timestamp timestampOffer;

    public OfferDTO(int idOffer, int idAccountOffer, String assetNameOffer, double amountOffer, double rateOffer, Timestamp timestampOffer) {
        this.idOffer = idOffer;
        this.idAccountOffer = idAccountOffer;
        this.assetNameOffer = assetNameOffer;
        this.amountOffer = amountOffer;
        this.rateOffer = rateOffer;
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
        return "OfferDTO{" +
                "idOffer=" + idOffer +
                ", idAccountOffer=" + idAccountOffer +
                ", assetNameOffer='" + assetNameOffer + '\'' +
                ", amountOffer=" + amountOffer +
                ", rateOffer=" + rateOffer +
                ", timestampOffer=" + timestampOffer +
                '}';
    }
}
