package nl.miwteam2.cryptomero.controller;

import nl.miwteam2.cryptomero.domain.Asset;
import nl.miwteam2.cryptomero.domain.Rate;
import nl.miwteam2.cryptomero.service.AssetService;
import nl.miwteam2.cryptomero.service.RateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Stijn Klijn
 */

@RestController
@RequestMapping("/rates")
public class RateController {

    private RateService rateService;

    @Autowired
    public RateController(RateService rateService) {
        this.rateService = rateService;
    }

    @GetMapping
    public List<Rate> getAll() {
        return null;
    }

}
