package nl.miwteam2.cryptomero.service.Authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class SaltMaker {
  private static final Logger LOGGER = LoggerFactory.getLogger(SaltMaker.class);
  private static final int SALT_LENGTH = 8;
  private final int saltLength;
  private final SecureRandom secureRandom;

  public SaltMaker(int saltLength) {
	super();
	LOGGER.info("New SaltMaker");
	this.saltLength = saltLength;
	this.secureRandom = new SecureRandom();
  }

  public SaltMaker() {
	this(SALT_LENGTH);
  }

  public String generateSalt() {
	int tempSaltLengte = saltLength / 2;
	byte[] array = new byte[saltLength % 2 == 0 ? tempSaltLengte : tempSaltLengte + 1];
	secureRandom.nextBytes(array);
	String salt = encodeHexString(array);
	return saltLength % 2 == 0 ? salt : salt.substring(1);
  }

  public static String encodeHexString(byte[] byteArray) {
	StringBuffer hexStringBuffer = new StringBuffer();
	for (byte b : byteArray) {
	  hexStringBuffer.append(byteToHex(b));
	}
	return hexStringBuffer.toString();
  }

  private static String byteToHex(byte num) {
	char[] hexDigits = new char[2];
	hexDigits[0] = Character.forDigit((num >> 4) & 0xF, 16);
	hexDigits[1] = Character.forDigit((num & 0xF), 16);
	return new String(hexDigits);
  }

}
