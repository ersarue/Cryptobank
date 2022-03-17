package nl.miwteam2.cryptomero.service.Authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import nl.miwteam2.cryptomero.domain.Customer;
import nl.miwteam2.cryptomero.domain.UserAccount;
import nl.miwteam2.cryptomero.service.CustomerService;
import nl.miwteam2.cryptomero.service.UserAccountService;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


/**
 * @author MinkTK
 * @version 1.4
 */

@Service
public class LoginService {
    private static final String EMPTY_HEADER = "Og==";
    private static final long SESSION_LENGTH = 600000;                  // max Session Length is now set to 10 minutes
    private final AuthenticationService authenticationService;
    private final SecretService secretService;
    private final CustomerService customerService;
    private final UserAccountService userAccountService;

    @Autowired
    public LoginService(AuthenticationService authenticationService, SecretService secretService, CustomerService customerService, UserAccountService userAccountService) {
        this.authenticationService = authenticationService;
        this.secretService = secretService;
        this.customerService = customerService;
        this.userAccountService = userAccountService;
    }

//    MTK: Cryptomero decided to implement a JSON Web Token (JWT) as an access token to minimize connection to the database.
//    In future release a combination of an opaque token (previous release) and a JWT (current release) could be an option.



//  MTK: ROUTE according Acceptance Criteria
    /**
     * Provides login for a userAccount
     * @param userAccount   The userAccount that is attempting login; providing credentials.
     * @return              Access token (JWT)
     * @throws Exception    When credentials are invalid
     */
    public String login(UserAccount userAccount) throws Exception {
        int idAccount;
        if (!authenticationService.authenticate(userAccount)) throw new Exception           // authenticate credentials
                ("Invalid combination username and password");
        else {
            idAccount = getAuthenticatedUserAccount(userAccount.getEmail()).getIdAccount();
        }
        return createToken(idAccount);                                                               // create and return accessToken
    }

    /**
     * Provides login based on credentials in authorization header
     * @param authorizationHeader   The authorizationheader provided in the HTTPRequest
     * @return                      Access token (JWT)
     * @throws Exception            When no credentials are provided
     */
    public String login(String authorizationHeader) throws Exception {
        String credentials = authenticationService.getHeaderContent(authorizationHeader);

        // check if credentials are provided
        if (credentials.equals(EMPTY_HEADER)) throw new Exception("No credentials found");

        // decode the relevant data
        byte[] decodedBytes
                = Base64.decodeBase64(credentials.getBytes());
        String pair = new String(decodedBytes);
        String[] userDetails = pair.split(":", 2);

        // login
        return login(new UserAccount(userDetails[0], userDetails[1]));
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
        System.out.println(idAccount);
        jwtBuilder.withClaim("exp", setExpiryDate());
        return jwtBuilder.sign(algorithm);
    }

    public Customer getAuthenticatedCustomer(String userName) {
        return customerService.findById(userAccountService.findByEmail(userName).getIdAccount());
    }

    public UserAccount getAuthenticatedUserAccount(String userName) {
        return userAccountService.findByEmail(userName);
    }
}
