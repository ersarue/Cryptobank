package nl.miwteam2.cryptomero.repository;

import nl.miwteam2.cryptomero.domain.Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Petra Coenen
 * @version 1.1
 */

@Repository
public class JdbcAddressDao implements GenericDao<Address> {

    private final Logger logger = LoggerFactory.getLogger(JdbcAddressDao.class);

    JdbcTemplate jdbcTemplate;

    public JdbcAddressDao(JdbcTemplate jdbcTemplate) {
        super();
        this.jdbcTemplate = jdbcTemplate;
        logger.info("New JdbcAddressDao");
    }

    public void storeOne(Address address) {
        String sql = "INSERT INTO address(street_name, house_no, house_add, postal_code, city)" +
                "VALUES (?,?,?,?,?); ";
        jdbcTemplate.update(sql, address.getStreetName(), address.getHouseNo(),
                address.getHouseAdd(), address.getPostalCode(), address.getCity());
    }

    public Address findById(int id) {
        String sql = "SELECT * FROM address WHERE id_address = ?;";
        return jdbcTemplate.queryForObject(sql, new AddressRowMapper(), id);
    }

    public List<Address> getAll() {
        String sql = "SELECT * FROM address;";
        return jdbcTemplate.query(sql, new AddressRowMapper(), null);
    }

    private static class AddressRowMapper implements RowMapper<Address> {
        @Override
        public Address mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
            return new Address(resultSet.getInt("id_address"), resultSet.getString("street_name"),
                    resultSet.getInt("house_no"), resultSet.getString("house_add"),
                    resultSet.getString("postal_code"), resultSet.getString("city"));
        }
    }

    public int updateOne(Address address) {
        String sql = "UPDATE address " +
                "SET street_name = ?, house_no = ?, house_add = ?, postal_code = ?, city = ? " +
                "WHERE id_address = ?;";
        return jdbcTemplate.update(sql, address.getStreetName(), address.getHouseNo(), address.getHouseAdd(),
                address.getPostalCode(), address.getCity(), address.getIdAddress());
    }

    public int deleteOne(int id) {
        String sql = "DELETE FROM address WHERE id_address = ?;";
        Object[] args = new Object[] {id};
        return jdbcTemplate.update(sql, args);
    }
}
