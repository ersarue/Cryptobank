package nl.miwteam2.cryptomero.service.Authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;

public class SaltMaker {
  private static final Logger logger = LoggerFactory.getLogger(SaltMaker.class);
  private static final int SALT_LENGTH = 8;
  private int saltLength;
  private SecureRandom secureRandom;

  public SaltMaker(int saltLength) {
	super();
	logger.info("New SaltMaker");
	this.saltLength = saltLength;
	secureRandom = new SecureRandom();
  }

}
