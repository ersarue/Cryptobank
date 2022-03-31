package nl.miwteam2.cryptomero.repository;

import nl.miwteam2.cryptomero.domain.Offer;
import nl.miwteam2.cryptomero.domain.OfferDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Repository
public class JdbcOfferDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcOfferDao(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    private final RowMapper<OfferDTO> offerRowMapper = (resultSet, rowNum) -> new OfferDTO(
            resultSet.getInt("id_offer"),
            resultSet.getInt("id_account"),
            resultSet.getString("asset_name"),
            resultSet.getDouble("amount"),
            resultSet.getDouble("rate_offer"),
            Timestamp.valueOf(resultSet.getString("timestamp")));

    public OfferDTO findById(int id) {
        String sql = "SELECT * FROM offer WHERE id_offer = ?;";
        return jdbcTemplate.queryForObject(sql, offerRowMapper, id);
    }

    public int storeOne(Offer offer) {
        String sql = "INSERT INTO offer(id_account, asset_name, amount, rate_offer, timestamp)" + "VALUES (?, ?, ?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, offer.getUserOffer().getIdAccount());
            preparedStatement.setString(2, offer.getAssetOffer().getAssetName());
            preparedStatement.setDouble(3, offer.getAmountOffer());
            preparedStatement.setDouble(4, offer.getPriceOffer());
            preparedStatement.setString(5, offer.getTimestampOffer().toString());
            return preparedStatement;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    public List<OfferDTO> getAll() {
        String sql = "SELECT * FROM offer;";
        return jdbcTemplate.query(sql, offerRowMapper);
    }

    public List<OfferDTO> getAllByIdAccount(int idAccount) {
        String sql = "SELECT * FROM offer WHERE id_account = ?;";
        return jdbcTemplate.query(sql, offerRowMapper, idAccount);
    }

    // MTK: you can only update amount and timestamp of an offer
    public int updateOne(Offer offer) {
        String sql = "UPDATE offer" + "SET amount = ?, timestamp = ?" +
                "WHERE id_offer = ?";

        return jdbcTemplate.update(sql, offer.getAmountOffer(), offer.getTimestampOffer().toString(), offer.getIdOffer());
    }

    public int deleteOne(int id) {
        String sql = "DELETE FROM offer WHERE id_offer = ?;";
        return jdbcTemplate.update(sql, id);
    }
}
