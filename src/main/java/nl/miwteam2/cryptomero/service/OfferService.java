package nl.miwteam2.cryptomero.service;

import nl.miwteam2.cryptomero.domain.Offer;
import nl.miwteam2.cryptomero.domain.TradeOfferDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class OfferService {

    public OfferService() {
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
}
