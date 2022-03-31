package nl.miwteam2.cryptomero.repository;

import nl.miwteam2.cryptomero.domain.Asset;
import nl.miwteam2.cryptomero.domain.Offer;
import nl.miwteam2.cryptomero.domain.OfferDTO;
import nl.miwteam2.cryptomero.domain.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class OfferRepository {

    private JdbcUserAccountDao jdbcUserAccountDao;
    private JdbcAssetDao jdbcAssetDao;
    private JdbcOfferDao jdbcOfferDao;

    @Autowired
    public OfferRepository(JdbcUserAccountDao jdbcUserAccountDao, JdbcAssetDao jdbcAssetDao, JdbcOfferDao jdbcOfferDao) {
        this.jdbcUserAccountDao = jdbcUserAccountDao;
        this.jdbcAssetDao = jdbcAssetDao;
        this.jdbcOfferDao = jdbcOfferDao;
    }

    public Offer findById(int id) {
        OfferDTO offer = jdbcOfferDao.findById(id);
        UserAccount userOffer = jdbcUserAccountDao.findById(offer.getIdAccountOffer());
        Asset assetOffer = jdbcAssetDao.findByName(offer.getAssetNameOffer());
        return new Offer(userOffer, assetOffer, offer);
    }

    public List<Offer> findAllByIdAccount(int idAccount) {
        List<OfferDTO> offerDTOs = jdbcOfferDao.getAllByIdAccount(idAccount);
        return fillOfferList(offerDTOs);
    }

    public List<Offer> getAll() {
        List<OfferDTO> offerDTOs = jdbcOfferDao.getAll();
        return fillOfferList(offerDTOs);
    }

    public List<Offer> fillOfferList(List<OfferDTO> offerDTOs) {
        List<Offer> offers = new ArrayList<Offer>();
        for (OfferDTO offer:offerDTOs) {
            UserAccount userOffer = jdbcUserAccountDao.findById(offer.getIdAccountOffer());
            Asset assetOffer = jdbcAssetDao.findByName(offer.getAssetNameOffer());
            offers.add(new Offer(userOffer, assetOffer, offer));
        }
        return offers;
    }

    public int storeOne(Offer offer) {
        return jdbcOfferDao.storeOne(offer);
    }

    public int updateOne(Offer offer) {
        return jdbcOfferDao.updateOne(offer);
    }

    public int deleteOne(int idOffer) {
        return jdbcOfferDao.deleteOne(idOffer);
    }
}
