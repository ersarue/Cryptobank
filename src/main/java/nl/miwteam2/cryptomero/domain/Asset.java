package nl.miwteam2.cryptomero.domain;

import java.util.Objects;

/**
 * @author Samuël Geurts & Stijn Klijn
 */

public class Asset {

     private String assetName;
     private String assetAbbr;

     public Asset(String assetName, String assetAbbr) {
          this.assetName = assetName;
          this.assetAbbr = assetAbbr;
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

     @Override
     public String toString() {
          return String.format("Asset %s (%s)", assetName, assetAbbr);
     }

     @Override
     public boolean equals(Object o) {
          if (this == o) return true;
          if (o == null || getClass() != o.getClass()) return false;
          Asset asset = (Asset) o;
          return assetName.equals(asset.assetName);
     }

     @Override
     public int hashCode() {
          return Objects.hash(assetName);
     }
}
