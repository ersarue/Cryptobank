package nl.miwteam2.cryptomero.repository;

import nl.miwteam2.cryptomero.domain.Rate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author Stijn Klijn
 */

@Repository
public class RateDao {

    private JdbcTemplate jdbcTemplate;
    private AssetDao assetDao;

    @Autowired
    public RateDao(JdbcTemplate jdbcTemplate, AssetDao assetDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.assetDao = assetDao;
    }

    public List<Rate> getLatest() {
        String sql = "SELECT * FROM rate " +
                "WHERE `datetime` IN " +
                "(SELECT MAX(`datetime`) FROM rate " +
                "GROUP BY asset);";
        return jdbcTemplate.query(sql, new RateDao.RateRowMapper());
    }

    public Rate getLatestByName(String name) {
        String sql = "SELECT * FROM rate " +
                "WHERE `datetime` IN " +
                "(SELECT MAX(`datetime`) FROM rate " +
                "GROUP BY asset) " +
                "AND asset = ?;";
        return jdbcTemplate.queryForObject(sql, new RateDao.RateRowMapper(), name);
    }

    public List<Rate> getHistory(String name, LocalDateTime startTimepoint) {
        String sql = "SELECT * FROM rate " +
                "WHERE asset = ? AND `datetime` >= ?;";
        return jdbcTemplate.query(sql, new RateDao.RateRowMapper(), name, startTimepoint);
    }

    public int storeOne(Rate rate) {
        String sql = "INSERT INTO rate VALUES(?, ?, ?);";
        return jdbcTemplate.update(sql, rate.getAsset().getAssetName(), rate.getTimepoint(), rate.getRate());
    }

    private class RateRowMapper implements RowMapper<Rate> {
        @Override
        public Rate mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Rate(assetDao.findByName(rs.getString("asset")),
                    LocalDateTime.parse(rs.getString("datetime"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    rs.getDouble("rate")
            );
        }
    }
}
