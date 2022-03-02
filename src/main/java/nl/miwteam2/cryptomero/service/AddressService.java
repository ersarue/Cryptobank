package nl.miwteam2.cryptomero.service;

import nl.miwteam2.cryptomero.domain.Address;
import nl.miwteam2.cryptomero.repository.JdbcAddressDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Petra Coenen
 * @version 1.2
 */

@Service
public class AddressService {

    private final JdbcAddressDao jdbcAddressDao;

    private final Logger logger = LoggerFactory.getLogger(AddressService.class);

    @Autowired
    public AddressService(JdbcAddressDao dao) {
        jdbcAddressDao = dao;
        logger.info("New AddressService");
    }

    public void storeAddress(Address address) {
        jdbcAddressDao.storeOne(address);
    }

    public Address getAddressById(int id) {
        return jdbcAddressDao.findById(id);
    }

    public List<Address> getAllAddresses() {
        return jdbcAddressDao.getAll();
    }

    public int updateAddress(Address address) { return jdbcAddressDao.updateOne(address); }

    public int deleteAddress(int id) { return jdbcAddressDao.deleteOne(id); }

    public boolean isValidFormat(String postalCode) {
        return postalCode.matches("[1-9][0-9]{3}\\s?[a-zA-Z]{2}");
    }
}
