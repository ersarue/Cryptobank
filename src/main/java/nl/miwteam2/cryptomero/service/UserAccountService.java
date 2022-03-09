package nl.miwteam2.cryptomero.service;

import nl.miwteam2.cryptomero.domain.UserAccount;
import nl.miwteam2.cryptomero.repository.JdbcUserAccountDao;
import nl.miwteam2.cryptomero.repository.RootRepository;
import nl.miwteam2.cryptomero.service.Authentication.HashService;
import nl.miwteam2.cryptomero.service.Authentication.SaltMaker;
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
    private HashService hashService;
    private SaltMaker saltMaker;
    private RootRepository rootRepository;
    private JdbcUserAccountDao userAccountDao;

    @Autowired
    public UserAccountService(HashService hash, SaltMaker salt, RootRepository repository, JdbcUserAccountDao dao) {
        super();
        hashService = hash;
        saltMaker = salt;
        rootRepository = repository;
        userAccountDao = dao;
        logger.info("New UserAccountService");
    }

    public UserAccount findById(int id) {
        return rootRepository.findUserAccountById(id);
    }

    public UserAccount findByEmail(String email) {
        return rootRepository.findUserAccountByEmail(email);
    }

    public UserAccount storeOne(UserAccount userAccount) {
        String salt = saltMaker.generateSalt();
        userAccount.setSalt(salt);
        userAccount.setPassword(hashService.hash(userAccount.getPassword(), salt));
        int idAccount = userAccountDao.storeOne(userAccount);
        userAccount.setIdAccount(idAccount);
        return userAccount;
    }

    public List<UserAccount> getAll() {
        return userAccountDao.getAll();
    }

    public UserAccount updateOne(UserAccount userAccount) {
        userAccountDao.updateOne((userAccount));
        return userAccount;
    }

    public int deleteOne(int id) {
        return userAccountDao.deleteOne(id);
    }

    public boolean isEmailAlreadyInUse(String email) {
        return userAccountDao.isEmailAlreadyInUse(email);
    }
}
