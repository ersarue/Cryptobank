package nl.miwteam2.cryptomero.repository;

import nl.miwteam2.cryptomero.domain.Customer;
import nl.miwteam2.cryptomero.domain.CustomerDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class JdbcCustomerDtoDao implements GenericDao<CustomerDto>{

    private JdbcTemplate jdbcTemplate;

    public JdbcCustomerDtoDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<CustomerDto> customerRowMapper = (resultSet, rowNum) -> new CustomerDto(
            resultSet.getString("first_name"),
            resultSet.getString("name_prefix"),
            resultSet.getString("last_name"),
            LocalDate.parse(resultSet.getString("dob")),
            resultSet.getString("bsn"),
            resultSet.getString("telephone"));

    @Override
    public CustomerDto findById(int id) {
        String sql = "SELECT * FROM customer WHERE id_account = ?;";
        return this.jdbcTemplate.queryForObject(sql,customerRowMapper,id);
        //todo vind oplossing voor situatie als id niet bestaat en null terug geeft
    }

    @Override
    public void storeOne(CustomerDto customerDto) {
        //String sql = "INSERT INTO customer(first_name,name_prefix,last_name,dob,bsn,telephone) VALUES (?,?,?,?,?,?);";
        String sql = "INSERT INTO customer VALUES (?,?,?,?,?,?,?,?);";
        jdbcTemplate.update(sql,customerDto.getFirstName(),customerDto.getNamePrefix(),customerDto.getLastName(),
                customerDto.getDob(),customerDto.getBsn(),customerDto.getTelephone(),
                //todo customer.getAddress().getIdAddress);
                1); //en verwijder deze
    }

    @Override
    public List<CustomerDto> getAll() {
        String sql = "SELECT * FROM customer;";
        return jdbcTemplate.query(sql,customerRowMapper, null);
    }
}
