package nl.miwteam2.cryptomero.repository;

import nl.miwteam2.cryptomero.domain.Address;
import nl.miwteam2.cryptomero.domain.BankAccount;
import nl.miwteam2.cryptomero.domain.Customer;
import nl.miwteam2.cryptomero.domain.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
/**
 * @author Marcel Brachten, studentnr: 500893228 - MIW Cohort 26
 */
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
        String sql = "INSERT INTO bank_account(id_account, iban, balance_eur) VALUES (?,?,?);";
        jdbcTemplate.update(sql,bankAccount.getUserAccount().getIdAccount(),bankAccount.getIban(),
                bankAccount.getBalanceEur());
    }
    public int updateOne(BankAccount bankAccount) {
        String sql = "UPDATE bank_account " +
                "SET iban = ?, balance_eur = ? " +
                "WHERE id_account = ?;";
        return jdbcTemplate.update(sql, bankAccount.getIban(),bankAccount.getBalanceEur(),
                bankAccount.getUserAccount().getIdAccount());
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
    public int deleteOne(int id) {
        String sql = "DELETE FROM bank_account WHERE id_account = ?;";
        return jdbcTemplate.update(sql, id);
    }


}
