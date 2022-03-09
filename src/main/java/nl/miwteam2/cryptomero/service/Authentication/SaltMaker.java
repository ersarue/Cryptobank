package nl.miwteam2.cryptomero.service.Authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class SaltMaker {
  private static final Logger logger = LoggerFactory.getLogger(SaltMaker.class);
  private static final int SALT_LENGTH = 8;
  private int saltLength;
  private SecureRandom secureRandom;

  @Autowired
  public SaltMaker() {
	super();
	logger.info("New SaltMaker");
	this.saltLength = saltLength;
	secureRandom = new SecureRandom();
  }

    public String getSalt() {
        return "12345";
    }
}
