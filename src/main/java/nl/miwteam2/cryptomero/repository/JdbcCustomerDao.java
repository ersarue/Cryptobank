package nl.miwteam2.cryptomero.repository;

import nl.miwteam2.cryptomero.domain.Address;
import nl.miwteam2.cryptomero.domain.Customer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class JdbcCustomerDao implements GenericDao<Customer>{

    private JdbcTemplate jdbcTemplate;

    public JdbcCustomerDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Customer> customerRowMapper = (resultSet, rowNum) -> new Customer(
            resultSet.getString("first_name"),
            resultSet.getString("name_prefix"),
            resultSet.getString("last_name"),
            LocalDate.parse(resultSet.getString("dob")),
            resultSet.getString("bsn"),
            resultSet.getString("telephone"));

    @Override
    public Customer findById(int id) {
        String sql = "SELECT * FROM customer WHERE id_account = ?;";
        return this.jdbcTemplate.queryForObject(sql,customerRowMapper,id);
        //todo vind oplossing voor situatie als id niet bestaat en null terug geeft
    }

    @Override
    public void storeOne(Customer customer) {
        String sql = "INSERT INTO customer VALUES (?,?,?,?,?,?,?,?);";
        jdbcTemplate.update(sql,customer.getFirstName(),customer.getNamePrefix(),customer.getLastName(),
                customer.getDob(),customer.getBsn(),customer.getTelephone(), customer.getIdAccount(),
                customer.getAddress().getIdAddress());
    }

    @Override
    public List<Customer> getAll() {
        String sql = "SELECT * FROM customer;";
        return jdbcTemplate.query(sql,customerRowMapper, null);
    }

    public void updateOne(Customer customer) {
        String sql = "UPDATE customer" +
                "SET first_name = ?, name_prefix = ?, last_name = ?, dob = ?, bsn = ?, telephone = ?, id_address = ?" +
                "WHERE id_account = ?;";

        jdbcTemplate.update(sql,customer.getFirstName(),customer.getNamePrefix(),customer.getLastName(),
                customer.getDob(),customer.getBsn(),customer.getTelephone(),
                customer.getAddress().getIdAddress(),customer.getIdAccount());
    }

    public void deleteOne(int id) {
        String sql = "DELETE FROM customer WHERE id_account = ?;";
        jdbcTemplate.update(sql);
    }

    public int findAddressIdOfCustomer(Customer customer) {
        String sql = "SELECT id_address FROM customer WHERE id_account = ?;";
        return jdbcTemplate.queryForObject(sql, Integer.class, customer.getIdAccount());
    }


}
