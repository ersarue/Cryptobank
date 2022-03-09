package nl.miwteam2.cryptomero.service;

import nl.miwteam2.cryptomero.domain.UserAccount;
import nl.miwteam2.cryptomero.repository.JdbcUserAccountDao;
import nl.miwteam2.cryptomero.repository.RootRepository;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

/**
 * Service UserAccount
 *
 * @author Ercan Ersaru, studentnr: 500893336 - MIW Cohort 26
 */

@Service
public class UserAccountService {
  private static final Logger logger = LoggerFactory.getLogger(UserAccountService.class);
  private RootRepository rootRepository;
  private JdbcUserAccountDao userAccountDao;

  @Autowired
  public UserAccountService(RootRepository repository, JdbcUserAccountDao dao) {
	super();
	rootRepository = repository;
	userAccountDao = dao;
	logger.info("New UserAccountService");
  }

  public UserAccount findById(int id) {
	return rootRepository.findUserAccountById(id);
  }

  public UserAccount findByEmail(String email){
	return rootRepository.findUserAccountByEmail(email);
  }

  public UserAccount storeOne(UserAccount userAccount) {
	userAccountDao.storeOne(userAccount);
	userAccount.setPassword(hashPassword(userAccount.getPassword()));
	return userAccount;
  }

  public List<UserAccount> getAll() {
	return userAccountDao.getAll();
  }

  public UserAccount updateOne(UserAccount userAccount){
	userAccountDao.updateOne((userAccount));
	return userAccount;
  }

  public void deleteOne(int id) {
	userAccountDao.deleteOne(id);
  }

  public boolean isEmailAlreadyInUse(String email) {
	return userAccountDao.isEmailAlreadyInUse(email);
  }

  public String hashPassword(final String password) {
	String salt = "12345";  // salt TODO add random number using generator SecureRandom()
	int iterations = 1000; // number of times the password is hashed during the derivation of the symmetric key
	int keyLength = 256; // length in bits of the derived symmetric key
	try {
	  SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512"); // creating
	  // hashed key using PDBKDF2WithHmacSHA2
	  PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), iterations, keyLength); // (PBE) password-based encryption, defining its params
	  SecretKey key = skf.generateSecret(spec); // creating secret key from PBE
	  byte[] res = key.getEncoded(); // encoding the secret key into byte
	  String hashedString = Hex.encodeHexString(res); //
	  return hashedString;
	} catch (NoSuchAlgorithmException | InvalidKeySpecException exception) {
	  throw new RuntimeException(exception);
	}
  }

}
