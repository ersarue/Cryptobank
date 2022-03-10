package nl.miwteam2.cryptomero.service.Authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecretService {
    private static final String SECRET = "SowiesoJeWeetSMMEP";

    @Autowired
    public SecretService() {
    }

    public String getSecret() {
        return SECRET;
    }
}

