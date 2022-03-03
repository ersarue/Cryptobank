package nl.miwteam2.cryptomero.controller;

import nl.miwteam2.cryptomero.domain.Asset;
import nl.miwteam2.cryptomero.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/asset")
public class AssetController {

    private AssetService assetService;

    @Autowired
    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    @GetMapping
    public List<Asset> getAll() {
        return assetService.getAll();
    }

    @GetMapping("/{name}")
    public Asset findByName(@PathVariable String name) {
        return assetService.findByName(name);
    }

    @PostMapping
    public void storeOne(@RequestBody Asset asset) {
        assetService.storeOne(asset);
    }

    @PutMapping
    public void updateOne(@RequestBody Asset asset) {
        assetService.updateOne(asset);
    }

    @DeleteMapping("/{name}")
    public void deleteOne(@PathVariable String name) {
        assetService.deleteOne(name);
    }
}
