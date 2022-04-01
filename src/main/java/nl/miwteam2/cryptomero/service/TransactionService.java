package nl.miwteam2.cryptomero.service;

import nl.miwteam2.cryptomero.domain.*;
import nl.miwteam2.cryptomero.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * TransactionService
 *
 * @author Ercan Ersaru, studentnr: 500893336 - MIW Cohort 26
 */
@Service
public class TransactionService {
  private static final Logger LOGGER = LoggerFactory.getLogger(TransactionService.class);
  private final TransactionRepository transactionRepository;
  private final BankAccountService bankAccountService;
  private final AssetService assetService;
  private final Customer bank;
  private final RateService rate;

  public TransactionService(TransactionRepository transactionRepository,
							BankAccountService bankAccountService, AssetService assetService,
							CustomerService customerService, RateService rate) {
	super();
	LOGGER.info("New TransactionService");
	this.transactionRepository = transactionRepository;
	this.bankAccountService = bankAccountService;
	this.assetService = assetService;
	this.bank = customerService.findById(1);
	this.rate = rate;
  }

  public Transaction tradeWithBank(TradeBankDto trade) throws Exception {
	if (trade.getAmountTrade() > 0) { // buy from bank
	  if (checkBalance(trade.getCustomer().getBankAccount().getBalanceEur(), trade.getAmountTrade())) {
		throw new Exception("Cannot buy from bank. Insufficient balance");
	  }
	  // call completeTradeBankDto
	  completeTradeBankDto(trade);
	  // update wallet
	  updateWallet(trade);
	  // update bank account
	  updateBankAccount(trade);
	  // TODO call storeTransaction
	} else {  // sell to bank
	  // check asset in wallet
	  if (!(trade.getCustomer().getWallet().containsKey(trade.getAssetNameTrade()))) {
		throw new Exception("Cannot sell to bank. Insufficient balance");
	  }
	  // call completeTradeBankDto
	  completeTradeBankDto(trade);
	  // update bank wallet
	  updateBankAccount(trade);
	  // TODO call storeTransaction
	}
	// TODO finish return
	return null;
  }

  public void storeTransaction(Transaction transaction) {
	transactionRepository.storeOne(transaction);
  }

  public boolean IsBuyFromBank(double amount) {
	return amount > 0;
  }

  // completes the trade with calculated Euro amount and transaction fee
  public void completeTradeBankDto(TradeBankDto tradeBankDto) {
	final double TRANSACTION_FEE = 1.5;
	new Transaction(LocalDateTime.now(), bank, tradeBankDto.getCustomer(),
			assetService.findByName(tradeBankDto.getAssetNameTrade()), tradeBankDto.getAmountTrade(),
			calculateEuroByAmountTrade(tradeBankDto.getAmountTrade(),
					rate.getLatestByName(tradeBankDto.getAssetNameTrade()).getRate()), TRANSACTION_FEE);
  }

  // calculates Euro amount with user's input for requested amount
  public double calculateEuroByAmountTrade(double amount, double rate) {
	return amount * rate;
  }


  // update bank balance with user input
  public void updateBankAccount(TradeBankDto trade) {
	BankAccount bankAccount = trade.getCustomer().getBankAccount();
	bankAccount.setBalanceEur(bankAccount.getBalanceEur() - trade.getAmountTrade());
	bankAccountService.updateOne(bankAccount);
  }

  // loop over Wallet, if asset in wallet then update, if not then add
  public void updateWallet(TradeBankDto trade) {
	Map<String, Double> wallet = trade.getCustomer().getWallet();
	if (wallet.containsKey(trade.getAssetNameTrade())) {
	  wallet.put(trade.getAssetNameTrade(), wallet.get(trade.getAssetNameTrade()) + trade.getAmountTrade());
	} else {
	  wallet.put(trade.getAssetNameTrade(), trade.getAmountTrade());
	}
  }

  public boolean checkBalance(double balance, double amount) {
	return !(balance > amount);
  }

}


