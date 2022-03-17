package nl.miwteam2.cryptomero.repository;

import nl.miwteam2.cryptomero.domain.Rate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Stijn Klijn
 */

@Repository
public class JdbcRateDao implements GenericDao<Rate> {

    private JdbcTemplate jdbcTemplate;
    private JdbcAssetDao jdbcAssetDao;

    @Autowired
    public JdbcRateDao(JdbcTemplate jdbcTemplate, JdbcAssetDao jdbcAssetDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcAssetDao = jdbcAssetDao;
    }

    @Override
    public List<Rate> getAll() {
        //Omitted until required
        return null;
    }

    @Override
    public Rate findById(int id) {
        //This method will not be implemented because Rate has no id
        return null;
    }

    @Override
    public int storeOne(Rate rate) {
        String sql = "INSERT INTO rate VALUES(?, ?, ?);";
        return jdbcTemplate.update(sql, rate.getAsset().getAssetName(), rate.getTimepoint(), rate.getRate());
    }

    @Override
    public int updateOne(Rate rate) {
        //Omitted until required
        return 0;
    }

    @Override
    public int deleteOne(int id) {
        //Omitted until required
        return 0;
    }

    private class RateRowMapper implements RowMapper<Rate> {
        @Override
        public Rate mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Rate(jdbcAssetDao.findByName(rs.getString("asset")),
                    LocalDateTime.parse(rs.getString("datetime")),
                    rs.getDouble("rate")
            );
        }
    }
}
