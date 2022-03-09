package nl.miwteam2.cryptomero.controller;

import nl.miwteam2.cryptomero.domain.UserAccount;
import nl.miwteam2.cryptomero.service.UserAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller UserAccount
 *
 * @author Ercan Ersaru, studentnr: 500893336 - MIW Cohort 26
 */

@RestController
@RequestMapping(value = "/useraccount")
public class UserAccountController implements GenericController<UserAccount> {
  private static final Logger logger = LoggerFactory.getLogger(UserAccountController.class);
  private final UserAccountService userAccountService;

  @Autowired
  public UserAccountController(UserAccountService service) {
	super();
	userAccountService = service;
	logger.info("New UserAccountController");
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> findById(@PathVariable int id) {
	try {
	  return new ResponseEntity<>(userAccountService.findById(id), HttpStatus.OK);
	} catch (Exception exception) {
	  return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
	}
  }

  @GetMapping("/{email}")
  public ResponseEntity<?> findByEmail(@PathVariable String email) {
	try {
	  return new ResponseEntity<>(userAccountService.findByEmail(email), HttpStatus.OK);
	} catch (Exception exception) {
	  return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
	}
  }

  @PostMapping
  public ResponseEntity<?> storeOne(@RequestBody UserAccount userAccount) {
	try {
	  return new ResponseEntity<>(userAccountService.storeOne(userAccount), HttpStatus.CREATED);
	} catch (Exception exception) {
	  return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
	}
  }

  @GetMapping
  public List<UserAccount> getAll() {
	return userAccountService.getAll();
  }

  @PutMapping
  public ResponseEntity<?> updateOne(@RequestBody UserAccount userAccount) {
	try {
	  return new ResponseEntity<>(userAccountService.updateOne(userAccount), HttpStatus.OK);
	} catch (Exception exception) {
	  return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
	}
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteOne(@PathVariable int id) {
	try {
	  return new ResponseEntity<>(userAccountService.deleteOne(id), HttpStatus.OK);
	} catch (Exception exception) {
	  return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
	}
  }

}
