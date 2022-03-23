package nl.miwteam2.cryptomero.service.Authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import nl.miwteam2.cryptomero.domain.UserAccount;
import nl.miwteam2.cryptomero.domain.UserAccountDTO;
import nl.miwteam2.cryptomero.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


/**
 * @author MinkTK
 * @version 1.4
 */

@Service
public class LoginService {
    private static final long SESSION_LENGTH = 6000000;                  // max Session Length is now set to 100 minutes
    private final AuthenticationService authenticationService;
    private final SecretService secretService;
    private final UserAccountService userAccountService;

    @Autowired
    public LoginService(AuthenticationService authenticationService, SecretService secretService, UserAccountService userAccountService) {
        this.authenticationService = authenticationService;
        this.secretService = secretService;
        this.userAccountService = userAccountService;
    }

    /**
     * Provides login for a userAccount
     * @param loginCredentials      The user that is attempting login, providing credentials.
     * @return                      Access token (JWT) when login successful; null if credentials are not valid
     */
    public String login(UserAccountDTO loginCredentials) {
        if (authenticationService.authenticateLogin(loginCredentials)) {                                          // authenticate credentials
            UserAccount authenticatedUser = userAccountService.findByEmail(loginCredentials.getEmail());
            return createToken(authenticatedUser.getIdAccount());                                                                   // create and return accessToken
        } else {
            return null;
        }
    }

    /**
     * Creates an Access Token (JWT) with expiry date
     * @return      JSON Web Token (JWT)
     */
    public String createToken(int idAccount) {
        // todo test JWT exception handling
        Algorithm algorithm = Algorithm.HMAC256(secretService.getSecret());
        JWTCreator.Builder jwtBuilder = JWT.create();
        jwtBuilder.withIssuer("Cryptomero");
        jwtBuilder.withClaim("Account", idAccount);
        jwtBuilder.withClaim("exp", setExpiryDate());
        return jwtBuilder.sign(algorithm);
    }

    /**
     * Generates the expiry date based on current date and session length in this case for a JWT token
     * @return      Date of expiry token
     */
    public Date setExpiryDate() {
        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + SESSION_LENGTH;
        return new Date(expMillis);
    }
}
