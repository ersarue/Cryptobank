package nl.miwteam2.cryptomero.service;

import nl.miwteam2.cryptomero.domain.*;
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
 * Version 1.1
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

        List<Offer> matchList = null;
        Transaction transaction;

        Offer offer = getOffer(tradeOfferDto);
        List<Offer> offerList = getAll();

        //bij positief amount wil je verkopen
        if (offer.getAmountOffer() > 0){
            matchList = findBuyers(offer);
        } else if (offer.getAmountOffer() < 0) {
            matchList = findSellers(offer);
        } else {
            //todo return iets als de amount nul is
        }

        //herhaal de function totdat offer.getmount leeg is of wanneer de matchlist leeg is.
        do {
            transaction = ComputeTransaction(matchList,offer);
        }
        while ((offer.getAmountOffer() != 0) || !matchList.isEmpty());



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

    public List<Offer> getAll() {
        //todo haal alle offers op uit de database
        return new ArrayList<>();
    }

    //deze functie moet waarschijnlijk nug in stukje opgeknipt worden.
    public Transaction ComputeTransaction(List<Offer> matchList, Offer offer) {
        // todo - maakt een transactie object tussen offer en het eerste aanbod in de matchlist.

        // todo - probeer transactie te doen (TransactionService.....)
        // todo - als het niet goed gaat return null
        // todo - als het wel goed gaat update matchlist (ofwel een entry verwijdern of updaten)
        // todo -     en update/delete OfferTable (OfferDao.update(...)) - van de opponent
        // todo - update offer (ofwel er is nog een deel van de aanvraag over of amountOffer wordt op nul gezet.

        // spreekt transactie service aan
        // als het goed is gegaan dan update of delete offer in offerDatabase.

        return null;
    }



    public Offer storeOne(TradeOfferDto tradeOfferDto) {
        return null;
    }

    public List<Offer> findSellers(Offer buyer) {
        List<Offer> sellers = getAll().stream().filter(e -> e.getAssetOffer().equals(buyer.getAssetOffer()) && e.getAmountOffer() > 0)
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
        List<Offer> buyers = getAll().stream().filter(e -> e.getAssetOffer().equals(seller.getAssetOffer()) && e.getAmountOffer() < 0)
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
}
