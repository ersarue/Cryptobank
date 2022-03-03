package nl.miwteam2.cryptomero.repository;

import nl.miwteam2.cryptomero.domain.Asset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JdbcAssetDao implements GenericDao<Asset> {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcAssetDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Asset> getAll() {
        String sql = "SELECT * FROM asset;";
        return jdbcTemplate.query(sql, new AssetRowMapper());
    }

    @Override
    public Asset findById(int id) {
        //This method will not be implemented because Asset has no id
        return null;
    }

    public Asset findByName(String name) {
        String sql = "SELECT * FROM asset WHERE asset_name = ?;";
        return jdbcTemplate.queryForObject(sql, new AssetRowMapper(), name);
    }

    @Override
    public void storeOne(Asset asset) {
        String sql = "INSERT INTO asset VALUES(?, ?);";
        jdbcTemplate.update(sql, asset.getAssetName(), asset.getAssetAbbr());
    }

    public void updateOne(Asset asset) {
        String sql = "UPDATE asset SET asset_abbr = ? WHERE asset_name = ?;";
        jdbcTemplate.update(sql, asset.getAssetAbbr(), asset.getAssetName());
    }

    public void deleteOne(String name) {
        String sql = "DELETE FROM asset WHERE asset_name = ?;";
        jdbcTemplate.update(sql, name);
    }

    private class AssetRowMapper implements RowMapper<Asset> {
        @Override
        public Asset mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Asset(rs.getString("asset_name"), rs.getString("asset_abbr"));
        }
    }
}
