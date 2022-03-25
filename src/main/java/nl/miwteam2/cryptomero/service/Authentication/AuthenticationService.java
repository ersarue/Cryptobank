package nl.miwteam2.cryptomero.service.Authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import nl.miwteam2.cryptomero.domain.Customer;
import nl.miwteam2.cryptomero.domain.UserAccount;
import nl.miwteam2.cryptomero.domain.UserAccountDTO;
import nl.miwteam2.cryptomero.service.CustomerService;
import nl.miwteam2.cryptomero.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author MinkTK
 * @version 1.4
 */

@Service
public class AuthenticationService {
    private final HashService hashService;
    private final SecretService secretService;
    private final UserAccountService userAccountService;
    private final CustomerService customerService;

    @Autowired
    public AuthenticationService(HashService hashService, SecretService secretService, UserAccountService userAccountService,
                                 CustomerService customerService) {
        this.hashService = hashService;
        this.secretService = secretService;
        this.userAccountService = userAccountService;
        this.customerService = customerService;
    }


    /**
     * Authenticates a login attempt
     * @param userAccount   The userAccount that is attempting login; providing credentials.
     * @return              A boolean; true if the credentials  match the database, false if the credentials do not match
     */
    public boolean authenticateLogin(UserAccountDTO userAccount) {
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
     * Checks if a token is valid
     * @param authorizationHeader   An authorization header (string) containing a JSON Web Token (JWT)
     * @return                      A boolean; true if the JWT is authentic, false if the JWT is not authentic
     */
    public boolean isValidToken(String authorizationHeader) {
        try {
            verifyToken(authorizationHeader);
            return true;
        } catch (JWTVerificationException exception){
            return false;
        }
    }

    /**
     * Verifies a token
     * @param authorizationHeader           An authorization header (string) containing a JSON Web Token (JWT)
     * @return                              A decoded JWT token from which other information can be extracted
     * @throws JWTVerificationException     If the JWT token is not legit or expired an exception is thrown
     */
    public DecodedJWT verifyToken(String authorizationHeader) throws JWTVerificationException {
        String token = getHeaderContent(authorizationHeader);
        Algorithm algorithm = Algorithm.HMAC256(secretService.getSecret());
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("Cryptomero")
                .build();
        return verifier.verify(token);
    }

    /**
     * Extracts userAccountId from authorizationHeader and returns the data of the customer corresponding with the id
     * @param authorizationHeader       An authorization header (string) containing a JSON Web Token (JWT)
     * @return                          An authenticated customer if token is valid, null if not
     */
    public Customer getAuthenticatedCustomer(String authorizationHeader) {
        try {
            DecodedJWT jwt = verifyToken(authorizationHeader);
            int Account = jwt.getClaim("Account").asInt();
            return customerService.findById(Account);
        } catch (JWTVerificationException exception){
            return null;
        }
    }

    /**
     * Extracts the token from the authorizationHeader
     * @param header        An authorization header (string) containing a JSON Web Token (JWT)
     * @return              An unverified JWT token
     */
    public String getHeaderContent(String header) {
        String[] headerArray = header.split(" ", 2);
        return headerArray[1];
    }
}
