package nl.miwteam2.cryptomero.service;

import nl.miwteam2.cryptomero.domain.Asset;
import nl.miwteam2.cryptomero.domain.Customer;
import nl.miwteam2.cryptomero.domain.Offer;
import nl.miwteam2.cryptomero.domain.TradeOfferDto;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * checked offer op match en vraagt eventueel transactie aan
 * @author: Samuel Geurts en Stijn Klijn
 * Version 1.0
*/
@Service
public class OfferService {

    private CustomerService customerService;
    private AssetService assetService;


    public OfferService(CustomerService customerService,AssetService assetService) {
        this.customerService = customerService;
        this.assetService = assetService;
    }

    //methode die alles afhandelt
    public Offer tradeOffer(TradeOfferDto tradeOfferDto) {
        //todo handel offer af
        Offer offer = getOffer(tradeOfferDto);
        List<Offer> offerList = getListOfOffers();


        return null;
    }

    //maakt een offer van trade Offer Dto
    public Offer getOffer(TradeOfferDto tradeOfferDto) {
        Offer offer = new Offer(tradeOfferDto);
        Customer customer = customerService.findById(tradeOfferDto.getIdAccountOffer());
        Asset asset = assetService.findByName(tradeOfferDto.getAssetNameOffer());
        offer.setUserOffer(customer);
        offer.setAssetOffer(asset);
        offer.setTimestampOffer(new Timestamp(System.currentTimeMillis()));
        return null;
    }

    //haalt offerlijst uit database
    public List<Offer> getListOfOffers() {
        return null;
    }

    //haalt matches op tov offer
    public void getMatches(List<Offer> offerList, Offer offer) {

        //if niks opslaan

        //if matches
    }
}
