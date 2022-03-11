package nl.miwteam2.cryptomero.service.Authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import nl.miwteam2.cryptomero.domain.UserAccount;
import nl.miwteam2.cryptomero.service.AddressService;
import nl.miwteam2.cryptomero.service.UserAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger logger = LoggerFactory.getLogger(AddressService.class);

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
        // MTK now going twice into database because else cannot handle unknown user/emailadres - todo find cleaner solution
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
     * @param jwtToken  A JSON Web Token (JWT)
     * @return          A boolean; true if the JWT is authentic, false if the JWT is not authentic
     */
    public boolean authenticate(String jwtToken) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretService.getSecret());
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build();
            DecodedJWT jwt = verifier.verify(jwtToken);
            return true;
        } catch (JWTVerificationException exception){
            return false;
        }
    }
}
