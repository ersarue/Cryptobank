package nl.miwteam2.cryptomero.service.Authentication;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

@Service
public class HashService {

    private static final String PEPPER = "SSMMEP4Life";
    private static final Logger LOGGER = LoggerFactory.getLogger(HashService.class);

    public HashService() {
        super();
        LOGGER.info("New HashService");
    }

    public String hashPassword(String password, String salt) {
        return getPasswordHashed(password, salt, PEPPER);
    }

    public String getPasswordHashed(String password, String salt, String pepper) {
        int iterations = 1000; // number of times the password is hashed
        int keyLength = 512; // hashed password length
        try {
            // creating hashed key using PDBKDF2WithHmacSHA2
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            // (PBE) password-based encryption
            KeySpec spec = new PBEKeySpec(password.toCharArray(), (salt+pepper).getBytes(),
                    iterations, keyLength);
            // creating secret key from PBEencoding the secret key into byte
            byte[] hash = factory.generateSecret(spec).getEncoded();
            return Hex.encodeHexString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException exception) {
            throw new RuntimeException(exception);
        }

    }
}
