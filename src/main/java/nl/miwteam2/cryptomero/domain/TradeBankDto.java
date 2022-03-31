package nl.miwteam2.cryptomero.domain;

import java.sql.Timestamp;
/**
 * DTO Used for the endpoint "/trade/bank"
 * @author: Samuel Geurts
 * version 1.0
 */
public class TradeBankDto {

    private int idAccountTrade;
    private String assetNameTrade;
    private double amountTrade; //positief = kopen  3 , negatief = verkopen  -3

    public int getIdAccountTrade() {
        return idAccountTrade;
    }

    public void setIdAccountTrade(int idAccountTrade) {
        this.idAccountTrade = idAccountTrade;
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
}
