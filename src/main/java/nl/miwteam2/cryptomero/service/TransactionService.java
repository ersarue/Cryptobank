package nl.miwteam2.cryptomero.service;

import nl.miwteam2.cryptomero.domain.*;
import nl.miwteam2.cryptomero.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * TransactionService
 *
 * @author Ercan Ersaru, studentnr: 500893336 - MIW Cohort 26
 */
@Service
public class TransactionService {
  private static final Logger LOGGER = LoggerFactory.getLogger(TransactionService.class);
  private final double TRANSACTION_FEE = 1.5;
  private final TransactionRepository transactionRepository;
  private final BankAccountService bankAccountService;

  public TransactionService(TransactionRepository transactionRepository, BankAccountService bankAccountService) {
	super();
	LOGGER.info("New TransactionService");
	this.transactionRepository = transactionRepository;
	this.bankAccountService = bankAccountService;
  }
  
  public Transaction tradeWithBank(TradeBankDto trade) throws Exception {
	// buy from bank
	if (trade.getAmountTrade() > 0) {
	  if (checkBalance(trade.getCustomer().getBankAccount().getBalanceEur(), trade.getAmountTrade())) {
		throw new Exception("Cannot buy from bank. Insufficient balance");
	  }
	  updateBankAccount(trade.getCustomer(), trade.getAmountTrade());
	  // TODO update wallet
	  // TODO call storeTransaction
	} else {
	  // sell to bank
		if (checkWallet(String.valueOf(trade.getCustomer().getWallet()), trade.getAmountTrade())) {
		  throw new Exception("Cannot sell to bank. Insufficient balance");
		}
	  // TODO update bank wallet
	  // TODO call storeTransaction
	}
    return null;
  }

  public void storeTransaction(Transaction transaction) {
	transactionRepository.storeOne(transaction);
  }


  public void updateBankAccount(Customer customer, double amount) {
	customer.getBankAccount().setBalanceEur(customer.getBankAccount().getBalanceEur() - amount);
	bankAccountService.updateOne(customer.getBankAccount());
  }

  public void updateWallet(Customer customer, double amount) {
  }

  public boolean checkBalance(double balance, double amount) {
	return !(balance > amount);
  }

  public boolean checkWallet(String type, double amount) {
	return true;
  }
}

