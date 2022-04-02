package nl.miwteam2.cryptomero.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class TransactionDto {

    private LocalDateTime transactionTime;
    private Customer assetGiver;
    private Customer assetRecipient;
    private Asset asset;
    private double assetAmount;
    private double eurAmount;

    public TransactionDto(LocalDateTime transactionTime, Customer assetGiver, Customer assetRecipient, Asset asset, double assetAmount, double eurAmount) {
        this.transactionTime = transactionTime;
        this.assetGiver = assetGiver;
        this.assetRecipient = assetRecipient;
        this.asset = asset;
        this.assetAmount = assetAmount;
        this.eurAmount = eurAmount;
    }

    public LocalDateTime getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(LocalDateTime transactionTime) {
        this.transactionTime = transactionTime;
    }

    public Customer getAssetGiver() {
        return assetGiver;
    }

    public void setAssetGiver(Customer assetGiver) {
        this.assetGiver = assetGiver;
    }

    public Customer getAssetRecipient() {
        return assetRecipient;
    }

    public void setAssetRecipient(Customer assetRecipient) {
        this.assetRecipient = assetRecipient;
    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public double getAssetAmount() {
        return assetAmount;
    }

    public void setAssetAmount(double assetAmount) {
        this.assetAmount = assetAmount;
    }

    public double getEurAmount() {
        return eurAmount;
    }

    public void setEurAmount(double eurAmount) {
        this.eurAmount = eurAmount;
    }
}


