package nl.miwteam2.cryptomero.repository;

import nl.miwteam2.cryptomero.domain.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
* @author Petra Coenen
*/

@Repository
public class TransactionDao {

    private final Logger logger = LoggerFactory.getLogger(TransactionDao.class);

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public TransactionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        logger.info("New TransactionDao");
    }

    // Creates a customer with an int id for the fields "asset giver", "asset recipient" and "asset"
    private RowMapper<Transaction> transactionRowMapper = (r, rowNum) -> new Transaction(
            r.getInt("id_transaction"),
            LocalDateTime.parse(r.getString("datetime"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
            r.getDouble("asset_amount"),
            r.getDouble("eur_amount"),
            r.getDouble("eur_fee"));

    public int storeOne(Transaction t) {
        String sql = "INSERT INTO transaction(datetime, asset_giver, asset_recipient, asset_name, asset_amount, " +
                "eur_amount, eur_fee) VALUES(?, ?, ?, ?, ?, ?, ?);";
        return jdbcTemplate.update(sql, t.getTransactionTime(), t.getAssetGiver().getIdAccount(),
                t.getAssetRecipient().getIdAccount(), t.getAsset().getAssetName(), t.getAssetAmount(),
                t.getEurAmount(), t.getEurFee());
    }

    public Transaction findById(int id) {
        String sql = "SELECT * FROM transaction WHERE id_transaction = ?;";
        return this.jdbcTemplate.queryForObject(sql,transactionRowMapper,id);
    }

    public List<Transaction> getSellerHistory(int id) {
        String sql = "SELECT * FROM transaction WHERE asset_giver = ?;";
        return jdbcTemplate.query(sql, transactionRowMapper, id);
    }

    public List<Transaction> getBuyerHistory(int id) {
        String sql = "SELECT * FROM transaction WHERE asset_recipient = ?;";
        return jdbcTemplate.query(sql, transactionRowMapper, id);
    }

    public int findAssetGiverId(Transaction t) {
        String sql = "SELECT asset_giver FROM transaction WHERE id_transaction = ?;";
        return jdbcTemplate.queryForObject(sql, Integer.class, t.getIdTransaction());
    }

    public int findAssetRecipientId(Transaction t) {
        String sql = "SELECT asset_recipient FROM transaction WHERE id_transaction = ?;";
        return jdbcTemplate.queryForObject(sql, Integer.class, t.getIdTransaction());
    }

    public String findAssetNameOfTransaction(Transaction t) {
        String sql = "SELECT asset_name FROM transaction WHERE id_transaction = ?;";
        return jdbcTemplate.queryForObject(sql, String.class, t.getIdTransaction());
    }
}
