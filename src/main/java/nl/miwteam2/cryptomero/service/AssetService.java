package nl.miwteam2.cryptomero.service;

import nl.miwteam2.cryptomero.domain.Asset;
import nl.miwteam2.cryptomero.repository.AssetDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Stijn Klijn
 */
@Service
public class AssetService {

    private AssetDao assetDao;

    @Autowired
    public AssetService(AssetDao assetDao, RateService rateService) {
        this.assetDao = assetDao;
    }

    /**
     * Return information of all assets (except rates)
     * @return                      Information of all assets (except rates)
     */
    public List<Asset> getAll() {
        return assetDao.getAll();
    }

    /**
     * Return information of specific asset (except rates)
     * @param name                  The specified asset
     * @return                      Information of specific asset (except rates)
     */
    public Asset findByName(String name) {
        return assetDao.findByName(name);
    }
}
