package nl.miwteam2.cryptomero.controller;

import nl.miwteam2.cryptomero.domain.Address;
import nl.miwteam2.cryptomero.service.AddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Petra Coenen
 * @version 1.0
 */

@RestController
@RequestMapping("/address")
public class AddressController {

    private final AddressService addressService;

    private final Logger logger = LoggerFactory.getLogger(AddressController.class);

    @Autowired
    public AddressController(AddressService service) {
        super();
        addressService = service;
        logger.info("New AddressController");
    }

    @PostMapping(value = "/register")
    public int storeDog(@RequestBody Address address) {
        addressService.storeAddress(address);
        return address.getIdAddress();
    }

    @GetMapping(value = "/get/{id}")
    public Address getAddressById(@PathVariable int id) {
        return addressService.getById(id);
    }

    @GetMapping (value = "/all")
    public List<Address> getAllAddresses() {
        return addressService.getAllAddresses();
    }
}