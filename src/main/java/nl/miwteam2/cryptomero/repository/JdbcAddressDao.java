package nl.miwteam2.cryptomero.repository;

import nl.miwteam2.cryptomero.domain.Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;


import java.sql.*;
import java.util.List;
import java.util.Objects;

/**
 * @author Petra Coenen
 * @version 1.2
 */

@Repository
public class JdbcAddressDao {

    private final Logger logger = LoggerFactory.getLogger(JdbcAddressDao.class);

    JdbcTemplate jdbcTemplate;

    public JdbcAddressDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        logger.info("New JdbcAddressDao");
    }

    public int storeOne(Address address) {
        String sql = "INSERT INTO address(street_name, house_no, house_add, postal_code, city)" +
                "VALUES (?,?,?,?,?); ";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, address.getStreetName());
                ps.setInt(2, address.getHouseNo());
                ps.setString(3, address.getHouseAdd());
                ps.setString(4, address.getPostalCode());
                ps.setString(5, address.getCity());
                return ps;
            }
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    public Address findById(int id) {
        String sql = "SELECT * FROM address WHERE id_address = ?;";
        try {
            return jdbcTemplate.queryForObject(sql, new AddressRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            e.getMessage();
            return null;
        }
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
