package nl.miwteam2.cryptomero.controller;

import nl.miwteam2.cryptomero.domain.Address;
import nl.miwteam2.cryptomero.service.AddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * @author Petra Coenen
 * @version 1.3
 */

@RestController
@RequestMapping("/address")
public class AddressController {

    private final AddressService addressService;

    private final Logger logger = LoggerFactory.getLogger(AddressController.class);

    @Autowired
    public AddressController(AddressService service) {
        addressService = service;
        logger.info("New AddressController");
    }

    @PostMapping
    public int storeAddress(@RequestBody Address address) { return addressService.storeAddress(address); }

    @GetMapping("/{id}")
    public ResponseEntity<Address> getAddressById(@PathVariable int id) {
        Address addressById = addressService.getAddressById(id);
        if (addressById != null) {
            return new ResponseEntity<>(addressById, HttpStatus.OK);
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public List<Address> getAllAddresses() { return addressService.getAllAddresses(); }

    @PutMapping
    public void updateAddress(@RequestBody Address address) { addressService.updateAddress(address); }

    @DeleteMapping("/{id}")
    public void deleteAddress(@PathVariable int id) { addressService.deleteAddress(id); }
}
