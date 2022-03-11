package nl.miwteam2.cryptomero.service.Authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import nl.miwteam2.cryptomero.domain.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


/**
 * @author MinkTK
 * @version 1.2
 */

@Service
public class LoginService {
    private static final long SESSION_LENGTH = 600000;                  // max Session Length is now set to 10 minutes
    private final AuthenticationService authenticationService;
    private final SecretService secretService;

    @Autowired
    public LoginService(AuthenticationService authenticationService, SecretService secretService) {
        this.authenticationService = authenticationService;
        this.secretService = secretService;
    }

//    MTK: Cryptomero decided to implement a JSON Web Token (JWT) as an access token to minimize connection to the database.
//    In future release a combination of an opaque token (previous release) and a JWT (current release) could be an option.

    /**
     * Provides an access token (JWT) with an expiry date for an authenticated user
     * @param userAccount   The userAccount that is attempting login; providing credentials.
     * @return              Access token (JWT)
     * @throws Exception    When credentials are invalid
     * @throws Exception    When unable to create access token
     */
    public String login(UserAccount userAccount) throws Exception {

        // check credentials
        if (!authenticationService.authenticate(userAccount)) throw new Exception("Invalid combination username and password");

        // generate access token with expiry date
        // todo test JWT exception handling
        Algorithm algorithm = Algorithm.HMAC256(secretService.getSecret());
        JWTCreator.Builder jwtBuilder = JWT.create();
        jwtBuilder.withIssuer("auth0");
        jwtBuilder.withClaim("exp", setExpiryDate());
        return jwtBuilder.sign(algorithm);
    }

    /**
     * Generates the expiry date based on current date and session length for a JWT token
     * @return      Date of expiry token
     */
    public Date setExpiryDate() {
        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + SESSION_LENGTH;
        return new Date(expMillis);
    }
}
