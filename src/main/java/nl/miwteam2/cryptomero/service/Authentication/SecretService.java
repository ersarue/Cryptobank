package nl.miwteam2.cryptomero.service.Authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author MinkTK
 * @version 1.1
 */

@Service
public class SecretService {
    private static final String SECRET = "SowiesoJeWeetSMMEP";

    @Autowired
    public SecretService() {
    }

    /**
     * Provides secret string for signing tokens
     * @return  Secret string
     */
    public String getSecret() {
        return SECRET;
    }
}

