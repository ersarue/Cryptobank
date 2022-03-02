package nl.miwteam2.cryptomero.repository;

import nl.miwteam2.cryptomero.domain.BankAccount;
import nl.miwteam2.cryptomero.domain.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
@Repository
public class JdbcBankAccountDao implements GenericDao<BankAccount> {
    private JdbcTemplate jdbcTemplate;
    @Autowired
    public JdbcBankAccountDao(JdbcTemplate jdbcTemplate) {
        super();
        this.jdbcTemplate=jdbcTemplate;

    }



    private class BankAccountRowMapper implements RowMapper<BankAccount> {
        @Override
        public BankAccount mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
            String ib = resultSet.getString("iban");
            double be = resultSet.getDouble("balance_eur");
            BankAccount bankAccount = new BankAccount(ib,be);
            bankAccount.setUserAccount(null);
            return bankAccount;
        }
    }
    @Override
    public BankAccount findById(int id) {

        String sql = "SELECT * FROM bank_account WHERE id_account = ?;";
        return this.jdbcTemplate.queryForObject(sql,new BankAccountRowMapper(),id);
    }

    @Override
    public void storeOne(BankAccount bankAccount) {

    }

    @Override
    public List<BankAccount> getAll(){
        List<BankAccount> bankAccountList = jdbcTemplate.query("select * from bank_account",
                new BankAccountRowMapper(), null);
        if (bankAccountList.size()==0){
            return null;
        }else {
            return bankAccountList;
        }
    }


}
