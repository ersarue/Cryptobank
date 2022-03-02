package nl.miwteam2.cryptomero.domain;

public class Asset {

     private String assetName;
     private String assetAbbr;
     private double rate;

     public Asset(String assetName, String assetAbbr) {
          this.assetName = assetName;
          this.assetAbbr = assetAbbr;
          this.rate = getCurrentRate();
     }

     private double getCurrentRate() {
          //TODO implementeren
          return 0.0;
     }

     public String getAssetName() {
          return assetName;
     }

     public void setAssetName(String assetName) {
          this.assetName = assetName;
     }

     public String getAssetAbbr() {
          return assetAbbr;
     }

     public void setAssetAbbr(String assetAbbr) {
          this.assetAbbr = assetAbbr;
     }

     public double getRate() {
          this.rate = getCurrentRate();
          return rate;
     }

     @Override
     public String toString() {
          return String.format("Asset %s (%s)", assetName, assetAbbr);
     }
}
