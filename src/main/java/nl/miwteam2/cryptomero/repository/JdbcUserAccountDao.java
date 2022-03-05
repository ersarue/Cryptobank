package nl.miwteam2.cryptomero.repository;

import nl.miwteam2.cryptomero.domain.UserAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

/**
 * JDBC UserAccountDao
 *
 * @author Ercan Ersaru, studentnr: 500893336 - MIW Cohort 26
 */

@Repository
public class JdbcUserAccountDao implements GenericDao<UserAccount> {
  private static final Logger logger = LoggerFactory.getLogger(JdbcUserAccountDao.class);
  private List<UserAccount> userList;
  JdbcTemplate jdbcTemplate;

  @Autowired
  public JdbcUserAccountDao(JdbcTemplate jdbcTemplate) {
	this.jdbcTemplate = jdbcTemplate;
	//	userList = new ArrayList<>();
	//	fillUserList();
	logger.info("New JdbcUserAccountDao");
  }

  // handmatig testen
  //  private void fillUserList() {
  //	userList.add(new UserAccount(0, "test@test.com", "test"));
  //  }

  @Override
  public UserAccount findById(int id) {
	String sql = "SELECT * FROM user_account WHERE id_account = ?;";
	return jdbcTemplate.queryForObject(sql, new JdbcUserAccountDao.UserAccountRowMapper(), id);
  }

  @Override
  public void storeOne(UserAccount userAccount) {
	//Omitted because the store method has to return the generated key
  }

  public int storeUserAccount(UserAccount userAccount) {
      String sql = "INSERT INTO user_account(email, password) VALUES (?,?);";

      KeyHolder keyHolder = new GeneratedKeyHolder();

      jdbcTemplate.update(new PreparedStatementCreator() {
          @Override
          public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
              PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
              ps.setString(1, userAccount.getEmail());
              ps.setString(2, userAccount.getPassword());
              return ps;
          }
      }, keyHolder);

      return keyHolder.getKey().intValue();
  }

    @Override
  public List<UserAccount> getAll() {
	String sql = "SELECT * FROM user_account;";
	return jdbcTemplate.query(sql, new JdbcUserAccountDao.UserAccountRowMapper(), null);
  }


  private class UserAccountRowMapper implements RowMapper<UserAccount> {
	@Override
	public UserAccount mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
	  return new UserAccount(resultSet.getInt("id_account"),
			  resultSet.getString("email"),
			  resultSet.getString("password"));
	}
  }

}
