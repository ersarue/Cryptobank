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

    @Autowired
    public AssetService(JdbcAssetDao jdbcAssetDao, RateService rateService) {
        this.jdbcAssetDao = jdbcAssetDao;
    }

    public List<Asset> getAll() {
        return jdbcAssetDao.getAll();
    }

    public Asset findByName(String name) {
        return jdbcAssetDao.findByName(name);
    }
}
