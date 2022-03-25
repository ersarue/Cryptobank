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
import java.util.Locale;

/**
 * @author Stijn Klijn
 */

@Repository
public class JdbcRateDao {

    private JdbcTemplate jdbcTemplate;
    private JdbcAssetDao jdbcAssetDao;

    @Autowired
    public JdbcRateDao(JdbcTemplate jdbcTemplate, JdbcAssetDao jdbcAssetDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcAssetDao = jdbcAssetDao;
    }

    public List<Rate> getLatest() {
        String sql = "SELECT * FROM rate " +
                "WHERE `datetime` IN " +
                "(SELECT MAX(`datetime`) FROM rate " +
                "GROUP BY asset);";
        return jdbcTemplate.query(sql, new JdbcRateDao.RateRowMapper());
    }

    public Rate getLatestByName(String name) {
        String sql = "SELECT * FROM rate " +
                "WHERE `datetime` IN " +
                "(SELECT MAX(`datetime`) FROM rate " +
                "GROUP BY asset) " +
                "AND asset = ?;";
        return jdbcTemplate.queryForObject(sql, new JdbcRateDao.RateRowMapper(), name);
    }

    public List<Rate> getHistory(String name, LocalDateTime startTimepoint) {
        String sql = "SELECT * FROM rate " +
                "WHERE asset = ? AND `datetime` >= ?;";
        return jdbcTemplate.query(sql, new JdbcRateDao.RateRowMapper(), name, startTimepoint);
    }

    public int storeOne(Rate rate) {
        String sql = "INSERT INTO rate VALUES(?, ?, ?);";
        return jdbcTemplate.update(sql, rate.getAsset().getAssetName(), rate.getTimepoint(), rate.getRate());
    }

    private class RateRowMapper implements RowMapper<Rate> {
        @Override
        public Rate mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Rate(jdbcAssetDao.findByName(rs.getString("asset")),
                    LocalDateTime.parse(rs.getString("datetime"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    rs.getDouble("rate")
            );
        }
    }
}
