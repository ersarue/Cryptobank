package nl.miwteam2.cryptomero.controller;

import nl.miwteam2.cryptomero.domain.UserAccount;
import nl.miwteam2.cryptomero.service.UserAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller UserAccount
 *
 * @author Ercan Ersaru, studentnr: 500893336 - MIW Cohort 26
 */
@RequestMapping(value = "/useraccount")

@RestController
public class UserAccountController {
  private static final Logger logger = LoggerFactory.getLogger(UserAccountController.class);
  private UserAccountService userAccountService;

  @Autowired
  public UserAccountController(UserAccountService service) {
	super();
	userAccountService = service;
	logger.info("New UserAccountController");
  }

  @GetMapping(value = "/{id}")
  public UserAccount findById(@PathVariable int id){
	return userAccountService.findById(id);
  }
  public UserAccount getUserById(@PathVariable int id){
	return userAccountService.getById(id);
  }

  @GetMapping(value = "/useraccounts")
  public List<UserAccount> getUserAccounts(){return userAccountService.getUserAccounts();}

  @PutMapping(value = "/store_user")
  public int storeUser(@RequestBody UserAccount userAccount){
	userAccountService.storeOne(userAccount);
	return userAccount.getIdAccount();
  }


}
