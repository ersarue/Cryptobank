package nl.miwteam2.cryptomero.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Transaction {
    private int idTransaction;
    private LocalDateTime transactionTime;
    private Customer assetGiver;
    private Customer assetRecipient;
    private Asset asset;
    private double assetAmount;
    private double eurAmount;
    private double eurFee;

    public Transaction(int idTransaction, LocalDateTime transactionTime, Customer assetGiver, Customer assetRecipient,
                       Asset asset, double assetAmount, double eurAmount, double eurFee) {
        this.idTransaction = idTransaction;
        this.transactionTime = transactionTime;
        this.assetGiver = assetGiver;
        this.assetRecipient = assetRecipient;
        this.asset = asset;
        this.assetAmount = assetAmount;
        this.eurAmount = eurAmount;
        this.eurFee = eurAmount;
    }

    public Transaction(LocalDateTime transactionTime, Customer assetGiver, Customer assetRecipient, Asset asset,
                       double assetAmount, double eurAmount, double eurFee) {
        this(0, transactionTime, assetGiver, assetRecipient, asset, assetAmount, eurAmount, eurFee);
    }

    public Transaction(int idTransaction, LocalDateTime transactionTime, double assetAmount, double eurAmount, double eurFee) {
        this(idTransaction, transactionTime, null, null, null, assetAmount, eurAmount, eurFee);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return idTransaction == that.idTransaction && Double.compare(that.assetAmount, assetAmount) == 0 &&
                Double.compare(that.eurAmount, eurAmount) == 0 && Double.compare(that.eurFee, eurFee) == 0 &&
                transactionTime.equals(that.transactionTime) && assetGiver.equals(that.assetGiver) &&
                assetRecipient.equals(that.assetRecipient) && asset.equals(that.asset);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTransaction, transactionTime, assetGiver, assetRecipient, asset, assetAmount, eurAmount, eurFee);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "idTransaction=" + idTransaction +
                ", transactionTime=" + transactionTime +
                ", assetGiver=" + assetGiver +
                ", assetRecipient=" + assetRecipient +
                ", asset=" + asset +
                ", assetAmount=" + assetAmount +
                ", eurAmount=" + eurAmount +
                ", eurFee=" + eurFee +
                '}';
    }

    public int getIdTransaction() { return idTransaction; }

    public LocalDateTime getTransactionTime() { return transactionTime; }

    public Customer getAssetGiver() { return assetGiver; }

    public Customer getAssetRecipient() { return assetRecipient; }

    public Asset getAsset() { return asset; }

    public double getAssetAmount() { return assetAmount; }

    public double getEurAmount() { return eurAmount; }

    public double getEurFee() { return eurFee; }

    public void setIdTransaction(int idTransaction) { this.idTransaction = idTransaction; }

    public void setAssetGiver(Customer assetGiver) { this.assetGiver = assetGiver; }

    public void setAssetRecipient(Customer assetRecipient) { this.assetRecipient = assetRecipient; }

    public void setAsset(Asset asset) { this.asset = asset; }
}
