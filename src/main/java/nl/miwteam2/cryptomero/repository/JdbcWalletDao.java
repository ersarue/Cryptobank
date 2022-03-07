package nl.miwteam2.cryptomero.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcWalletDao implements GenericDao<Map<String, Double>> {

    private JdbcTemplate jdbcTemplate;

    public JdbcWalletDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Map<String, Double> findById(int id) {
        String sql = "SELECT asset_name, amount FROM wallet WHERE id_account = ?;";
        List<Object[]> list = jdbcTemplate.query(sql, new WalletRowMapper(), id);
        Map<String, Double> map = new HashMap<>();
        for (Object[] objects : list) {
            map.put((String)objects[0], (Double)objects[1]);
        }
        return map;
    }

    @Override
    public void storeOne(Map<String, Double> assetDoubleMap) {

    }

    @Override
    public List<Map<String, Double>> getAll() {
        return null;
    }

    private class WalletRowMapper implements RowMapper<Object[]> {
        @Override
        public Object[] mapRow(ResultSet rs, int rowNumber) throws SQLException {
            return new Object[]{rs.getString("asset_name"), rs.getDouble("amount")};
        }
    }
}