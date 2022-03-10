package nl.miwteam2.cryptomero.service.Authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import nl.miwteam2.cryptomero.domain.UserAccount;
import nl.miwteam2.cryptomero.service.UserAccountService;
import org.springframework.stereotype.Service;

/**
 * @author MinkTK
 * @version 1.1
 */

@Service
public class AuthenticationService {
    private final HashService hashService;
    private final SecretService secretService;
    private final UserAccountService userAccountService;
//    private final MapDatabase tokenDatabase;                                        // for codeLab - WORKS
//    public static final String REGEX_UUID = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}";     // for codeLab - WORKS

    public AuthenticationService(HashService hashService, SecretService secretService, UserAccountService userAccountService) {
//        this.tokenDatabase = tokenDatabase;                                       // for codeLab - WORKS
        this.hashService = hashService;
        this.secretService = secretService;
        this.userAccountService = userAccountService;
    }

    public boolean authenticate(UserAccount userAccount) {
        // MTK now going twice into database because else cannot handle unknown user/emailadres - todo find cleaner solution
        if (userAccountService.isEmailAlreadyInUse(userAccount.getEmail())) {
            UserAccount userDb = userAccountService.findByEmail(userAccount.getEmail());
            String dbPasswordHash = hashService.hashPassword(userDb.getPassword(), userDb.getSalt());
            String inputPasswordHash = hashService.hashPassword(userAccount.getPassword(), userDb.getSalt());
            System.out.println(dbPasswordHash);
            System.out.println(inputPasswordHash);
            return dbPasswordHash.equals(inputPasswordHash);
        }
        else { return false; }
    }

//    public boolean authenticate(String token) {                                 // for codeLab - WORKS
//        if (!token.matches(REGEX_UUID)) {
//            throw new IllegalArgumentException("token not of correct format!");
//        }
//        // hardcoded to account 0 since userAccount constructor default value is 0
//        String tokenDb = tokenDatabase.findTokenByIdAccount(0);
//        System.out.println(tokenDb);
//        return tokenDb.equals(token);
//    }

//    JWT - authenticate
    public boolean authenticate(String token) {
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
}
