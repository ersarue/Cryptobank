package nl.miwteam2.cryptomero.service.Authentication;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Service
public class HashService {

    private static final String PEPPER = "SSMMEP4Life";
    private static final Logger logger = LoggerFactory.getLogger(HashService.class);

    public HashService() {
        super();
        logger.info("New HashService");
    }

    public String hash(String password, String salt) {
        return hash(password, salt, PEPPER);
    }

    public String hash(String password, String salt, String pepper) {
        //TODO pepper nog toevoegen aan de hash
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
