package nl.miwteam2.cryptomero.service;

import nl.miwteam2.cryptomero.domain.Asset;
import nl.miwteam2.cryptomero.repository.JdbcAssetDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Stijn Klijn
 */
@Service
public class AssetService {

    private JdbcAssetDao jdbcAssetDao;
    private RateService rateService;
    private List<Asset> assets;

    @Autowired
    public AssetService(JdbcAssetDao jdbcAssetDao, RateService rateService) {
        this.jdbcAssetDao = jdbcAssetDao;
        this.rateService = rateService;
        this.assets = jdbcAssetDao.getAll();
    }

    public List<Asset> getAll() {
        return assets;
    }

    public Asset findByName(String name) {
        return assets.stream().filter(a -> a.getAssetName().equals(name)).findAny().orElse(null);
    }

   public void updateRates() {
        Map<String, Double> rates = rateService.getLatestRates();
        for (Asset asset : assets) {
            asset.setRate(rates.get(asset.getAssetName()));
        }
    }
}
