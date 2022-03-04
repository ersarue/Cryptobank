package nl.miwteam2.cryptomero.repository;

import nl.miwteam2.cryptomero.domain.Asset;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcWalletDao implements GenericDao<Map<Asset, Double>> {

    private JdbcTemplate jdbcTemplate;

    public JdbcWalletDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Map<Asset, Double> findById(int id) {
        String sql = "SELECT asset.asset_name, asset_abbr, amount FROM asset " +
                "JOIN wallet ON asset.asset_name = wallet.asset_name " +
                "WHERE id_account = ?;";
        List<Object[]> list = jdbcTemplate.query(sql, new WalletRowMapper(), id);
        Map<Asset, Double> map = new HashMap<>();
        for (Object[] objects : list) {
            map.put(new Asset((String)objects[0], (String)objects[1]), (Double)objects[2]);
        }
        return map;
    }

    @Override
    public void storeOne(Map<Asset, Double> assetDoubleMap) {

    }

    @Override
    public List<Map<Asset, Double>> getAll() {
        return null;
    }

    private class WalletRowMapper implements RowMapper<Object[]> {
        @Override
        public Object[] mapRow(ResultSet rs, int rowNumber) throws SQLException {
            return new Object[]{rs.getString("asset_name"), rs.getString("asset_abbr"), rs.getDouble("amount")};
        }
    }
}
