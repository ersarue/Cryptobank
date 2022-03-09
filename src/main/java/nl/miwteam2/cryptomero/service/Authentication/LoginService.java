package nl.miwteam2.cryptomero.service.Authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import nl.miwteam2.cryptomero.domain.UserAccount;
import nl.miwteam2.cryptomero.service.Authentication.AuthenticationService;
import nl.miwteam2.cryptomero.service.authentication.MapDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

/**
 * @author MinkTK
 * @version 1.0
 */

@Service
public class LoginService {
    private static final long SESSION_LENGTH = 600000;                  // 10 minutes
    private final AuthenticationService authenticationService;
    private final SecretService secretService;
//    private final MapDatabase tokenDatabase;                          // voor codelab opdracht - WORKS

    @Autowired
    public LoginService(AuthenticationService authenticationService, SecretService secretService) {
//        this.tokenDatabase = tokenDatabase;                           // voor codelab opdracht - WORKS
        this.authenticationService = authenticationService;
        this.secretService = secretService;
    }

//    //  MTK opaak token - need to store in database                         // uit codelab opdracht - WORKS
//    public String login(UserAccount userAccount) {
//        String token = null;
//        if (authenticationService.authenticate(userAccount)) {
//            token = UUID.randomUUID().toString();
//            tokenDatabase.inserIdAccountWithToken(userAccount.getIdAccount(), token); // store token
//        }
//        return token;
//    }

//  MTK generate JWT- token
    public String login(UserAccount userAccount) {
        String jwtToken = null;
        Algorithm algorithm = Algorithm.HMAC256(secretService.getSecret());

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        long expMillis = nowMillis + SESSION_LENGTH;
        Date exp = new Date(expMillis);

        if (authenticationService.authenticate(userAccount)) {
            try {
                JWTCreator.Builder jwtBuilder = JWT.create();
                jwtBuilder.withIssuer("auth0");                         // hoe veilig
                jwtBuilder.withClaim("sub", "loginTest");
                jwtBuilder.withClaim("iat", now);
                jwtBuilder.withClaim("exp", exp);
                jwtToken = jwtBuilder.sign(algorithm);
            } catch (JWTCreationException exception){
                System.out.println("jammer joh");
            }
        }
    return jwtToken;
    }
}
