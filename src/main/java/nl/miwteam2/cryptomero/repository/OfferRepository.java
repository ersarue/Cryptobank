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

    private UserAccountDao jdbcUserAccountDao;
    private AssetDao jdbcAssetDao;
    private OfferDao offerDao;

    @Autowired
    public OfferRepository(UserAccountDao UserAccountDao, AssetDao AssetDao, OfferDao offerDao) {
        this.jdbcUserAccountDao = jdbcUserAccountDao;
        this.jdbcAssetDao = jdbcAssetDao;
        this.offerDao = offerDao;
    }

    public Offer findById(int id) {
        OfferDTO offer = offerDao.findById(id);
        UserAccount userOffer = jdbcUserAccountDao.findById(offer.getIdAccountOffer());
        Asset assetOffer = jdbcAssetDao.findByName(offer.getAssetNameOffer());
        return new Offer(userOffer, assetOffer, offer);
    }

    public List<Offer> findAllByIdAccount(int idAccount) {
        List<OfferDTO> offerDTOs = offerDao.getAllByIdAccount(idAccount);
        return fillOfferList(offerDTOs);
    }

    public List<Offer> getAll() {
        List<OfferDTO> offerDTOs = offerDao.getAll();
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
        return offerDao.storeOne(offer);
    }

    public int updateOne(Offer offer) {
        return offerDao.updateOne(offer);
    }

    public int deleteOne(int idOffer) {
        return offerDao.deleteOne(idOffer);
    }
}
