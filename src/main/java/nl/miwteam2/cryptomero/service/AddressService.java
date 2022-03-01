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
 * @version 1.0
 */

@Service
public class AddressService {

    private final JdbcAddressDao jdbcAddressDao;

    private final Logger logger = LoggerFactory.getLogger(AddressService.class);

    @Autowired
    public AddressService(JdbcAddressDao dao) {
        super();
        jdbcAddressDao = dao;
        logger.info("New AddressService");
    }

    public void storeAddress(Address address) {
        jdbcAddressDao.storeOne(address);
    }

    public Address getById(int id) {
        return jdbcAddressDao.findById(id);
    }

    public List<Address> getAllAddresses() {
        return jdbcAddressDao.getAll();
    }
}
