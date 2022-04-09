package nl.miwteam2.cryptomero.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Rate {

    private Asset asset;
    private LocalDateTime timepoint;
    private double rate;

    public Rate(Asset asset, LocalDateTime timepoint, double rate) {
        this.asset = asset;
        this.timepoint = timepoint;
        this.rate = rate;
    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public LocalDateTime getTimepoint() {
        return timepoint;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return String.format("Rate of %s at %s was %.2f", asset.toString(), timepoint.toString(), rate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rate rate1 = (Rate) o;
        return Double.compare(rate1.rate, rate) == 0 && asset.equals(rate1.asset) && timepoint.equals(rate1.timepoint);
    }

    @Override
    public int hashCode() {
        return Objects.hash(asset, timepoint, rate);
    }
}
