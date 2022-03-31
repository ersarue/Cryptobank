package nl.miwteam2.cryptomero.service;

import nl.miwteam2.cryptomero.domain.Asset;
import nl.miwteam2.cryptomero.domain.Customer;
import nl.miwteam2.cryptomero.domain.Offer;
import nl.miwteam2.cryptomero.domain.TradeOfferDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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


    public List<Offer> findSellers(Offer buyer) {
        List<Offer> sellers = getAllOffers().stream().filter(e -> e.getAssetOffer().equals(buyer.getAssetOffer()) && e.getAmountOffer() > 0)
                .sorted(Comparator.comparing(Offer::getPriceOffer)).toList();
        buyer.setAmountOffer(-1 * buyer.getAmountOffer());

        List<Offer> matches = new ArrayList<>();

        for (Offer seller : sellers) {
            if (buyer.getAmountOffer() > 0 && seller.getAmountOffer() > 0 && seller.getPriceOffer() <= buyer.getPriceOffer()) {
                double number = Math.min(seller.getAmountOffer(), buyer.getAmountOffer());
                matches.add(new Offer(seller.getIdOffer(), seller.getUserOffer(), seller.getAssetOffer(), number, seller.getPriceOffer(), seller.getTimestampOffer()));
                seller.setAmountOffer(seller.getAmountOffer() - number);
                buyer.setAmountOffer(buyer.getAmountOffer() - number);
            }
        }
        return matches;
    }

    public List<Offer> findBuyers(Offer seller) {
        List<Offer> buyers = getAllOffers().stream().filter(e -> e.getAssetOffer().equals(seller.getAssetOffer()) && e.getAmountOffer() < 0)
                .sorted(Comparator.comparing(Offer::getPriceOffer).reversed()).toList();
        buyers.forEach(e -> e.setAmountOffer(-1 * e.getAmountOffer()));

        List<Offer> matches = new ArrayList<>();

        for (Offer buyer : buyers) {
            if (buyer.getAmountOffer() > 0 && seller.getAmountOffer() > 0 && seller.getPriceOffer() <= buyer.getPriceOffer()) {
                double number = Math.min(seller.getAmountOffer(), buyer.getAmountOffer());
                matches.add(new Offer(buyer.getIdOffer(), buyer.getUserOffer(), buyer.getAssetOffer(), number, buyer.getPriceOffer(), buyer.getTimestampOffer()));
                seller.setAmountOffer(seller.getAmountOffer() - number);
                buyer.setAmountOffer(buyer.getAmountOffer() - number);
            }
        }
        return matches;
    }

    public List<Offer> getAllOffers() {
        return new ArrayList<>();
    }

    public Offer storeOne(TradeOfferDto tradeOfferDto) {
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
