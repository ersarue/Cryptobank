package nl.miwteam2.cryptomero.service;

import nl.miwteam2.cryptomero.domain.UserAccount;
import nl.miwteam2.cryptomero.repository.GenericDao;
import nl.miwteam2.cryptomero.repository.JdbcUserAccountDao;
import nl.miwteam2.cryptomero.repository.RootRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service UserAccount
 *
 * @author Ercan Ersaru, studentnr: 500893336 - MIW Cohort 26
 */

@Service
public class UserAccountService {
  private static final Logger logger = LoggerFactory.getLogger(UserAccountService.class);
  //  private JdbcUserAccountDao jdbcUserAccountDao;
  private RootRepository rootRepository;
  private GenericDao<UserAccount> userAccountDao;

  @Autowired
  public UserAccountService(RootRepository repository, GenericDao<UserAccount> dao) {
	super();
	rootRepository = repository;
	userAccountDao = dao;
	//	jdbcUserAccountDao = dao;
	logger.info("New UserAccountService");
  }

  public UserAccount findById(int id) {
	return rootRepository.findUserAccountById(id);
  }

//  public List<UserAccount> getUserAccounts() {
//	return jdbcUserAccountDao.getAll();
//  }

  public void storeOne(UserAccount userAccount) {
	userAccountDao.storeOne(userAccount);
  }

}
