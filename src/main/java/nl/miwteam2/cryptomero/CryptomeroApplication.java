package nl.miwteam2.cryptomero;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
}
)

public class CryptomeroApplication {

  public static void main(String[] args) {
    SpringApplication.run(CryptomeroApplication.class, args);
  }

}
