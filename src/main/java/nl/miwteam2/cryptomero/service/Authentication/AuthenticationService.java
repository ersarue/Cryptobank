package nl.miwteam2.cryptomero.service.Authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import nl.miwteam2.cryptomero.domain.UserAccount;
import nl.miwteam2.cryptomero.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author MinkTK
 * @version 1.2
 */

@Service
public class AuthenticationService {
    private final HashService hashService;
    private final SecretService secretService;
    private final UserAccountService userAccountService;

    @Autowired
    public AuthenticationService(HashService hashService, SecretService secretService, UserAccountService userAccountService) {
        this.hashService = hashService;
        this.secretService = secretService;
        this.userAccountService = userAccountService;
    }


    /**
     * Authenticates a login attempt
     * @param userAccount   The userAccount that is attempting login; providing credentials.
     * @return              A boolean; true if the credentials  match the database, false if the credentials do not match
     */
    public boolean authenticate(UserAccount userAccount) {
        // MTK: now going twice into database because else cannot handle unknown user/emailadres - todo find cleaner solution
        if (userAccountService.isEmailAlreadyInUse(userAccount.getEmail())) {
            UserAccount userDb = userAccountService.findByEmail(userAccount.getEmail());
            String dbPasswordHash = userDb.getPassword();
            String inputPasswordHash = hashService.hashPassword(userAccount.getPassword(), userDb.getSalt());
            return dbPasswordHash.equals(inputPasswordHash);
        }
        else { return false; }
    }

    /**
     * Authenticates an active login
     * @param authorizationHeader   An authorization header containing a JSON Web Token (JWT)
     * @return                      A boolean; true if the JWT is authentic, false if the JWT is not authentic
     */
    public boolean authenticateToken(String authorizationHeader) {
        String token = getHeaderContent(authorizationHeader);
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretService.getSecret());
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (JWTVerificationException exception){
            return false;
        }
    }

    public String getHeaderContent(String header) {
        String[] headerArray = header.split(" ", 2);
        return headerArray[1];
    }
}
