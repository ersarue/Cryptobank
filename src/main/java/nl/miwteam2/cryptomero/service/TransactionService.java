package nl.miwteam2.cryptomero.service;

import nl.miwteam2.cryptomero.domain.*;
import nl.miwteam2.cryptomero.repository.TransactionRepository;
import nl.miwteam2.cryptomero.repository.WalletDao;
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
  private final CustomerService customerService;
  private final RateService rateService;
  private final WalletDao walletDao;
  final double TRANSACTION_FEE = 0.0165;

  public TransactionService(TransactionRepository transactionRepository,
							BankAccountService bankAccountService, AssetService assetService,
							CustomerService customerService, RateService rate, WalletDao walletDao) {
	super();
	LOGGER.info("New TransactionService");
	this.transactionRepository = transactionRepository;
	this.bankAccountService = bankAccountService;
	this.assetService = assetService;
	this.bank = customerService.findById(1);
	this.customerService = customerService;
	this.rateService = rate;
	this.walletDao = walletDao;
  }

  public Transaction tradeWithBank(TradeBankDto trade) throws Exception {
	Transaction transaction = new Transaction(LocalDateTime.now(),
			isCustomerBuying(trade.getAmountTrade()) ? bank : trade.getCustomer(),
			isCustomerBuying(trade.getAmountTrade()) ? trade.getCustomer() : bank,
			assetService.findByName(trade.getAssetNameTrade()), trade.getAmountTrade(),
			calculateEuroByAmountTrade(trade.getAmountTrade(), trade.getAssetNameTrade()),
			TRANSACTION_FEE);

	if (isCustomerBuying(trade.getAmountTrade())) { // buying from bank
	  if (!isBalanceEnoughToBuy(trade.getCustomer().getBankAccount().getBalanceEur(), trade.getAmountTrade())) {
		throw new Exception("Cannot buy from bank. Insufficient balance");
	  }
	} else {  // selling to bank
	  // check asset in wallet
	  if (!(isAssetInWallet(trade.getAssetNameTrade(), trade.getCustomer().getWallet()) && isAssetEnoughToSell(trade))) {
		throw new Exception("Insufficient asset");
	  }
	}
	updateBankAccount(trade); // update bank account
	storeTransaction(transaction); // store transaction
	return transaction;
  }

  public Transaction tradeWithUser(TransactionDto transactionDto) throws Exception {
	//todo cut method in pieces

	// make transaction from transactionDto
	Transaction transaction = new Transaction(transactionDto);
	// compute fee and add to transaction object
	double fee = transaction.totalPrice() * 0.0165; //todo use final
	transaction.setEurFee(fee);

	String assetName = transaction.getAsset().getAssetName();
	Map<String, Double> walletAssetGiver = transaction.getAssetGiver().getWallet();
	Map<String, Double> walletAssetRecipient = transaction.getAssetRecipient().getWallet();
	BankAccount bankAccountAssetGiver = transaction.getAssetGiver().getBankAccount();
	//userAccount is null because of JsonBackReference
	bankAccountAssetGiver.setUserAccount(transaction.getAssetGiver());
	BankAccount bankAccountAssetRecipient = transaction.getAssetRecipient().getBankAccount();
	//userAccount is null because of JsonBackReference
	bankAccountAssetRecipient.setUserAccount(transaction.getAssetRecipient());

	// check balance of both customers
	System.out.println(4);
	System.out.println("asset_amount: " + transaction.getAssetRecipient().getWallet().get(assetName));

	double assetAmountRecipient = walletAssetRecipient.get(assetName); //asset_amount assetRecipient
	double assetAmountGiver = walletAssetGiver.get(assetName); //asset_amount assetGiver
	double BalanceEurGiver = bankAccountAssetGiver.getBalanceEur(); //BalanceEur assetGiver
	double BalanceEurRecipient = bankAccountAssetRecipient.getBalanceEur(); //BalanceEur assetGiver

	if (!isBalanceEnoughToBuy(assetAmountGiver, transaction.getAssetAmount())) {
	  throw new Exception("Cannot trade Asset. Insufficient Asset balance");
	} else if (!isBalanceEnoughToBuy(BalanceEurGiver, transaction.getEurFee() / 2)) {
	  throw new Exception("Cannot trade Asset. Insufficient balance Giver to pay fee");
	} else if (!isBalanceEnoughToBuy(BalanceEurRecipient, transaction.totalPrice() + transaction.getEurFee() / 2)) {
	  throw new Exception("Cannot trade Asset. Insufficient balance Recipient");
	}
	System.out.println(5);

	// change wallet balance Giver and store
	walletAssetGiver.put(assetName, walletAssetGiver.get(assetName) - transaction.getAssetAmount());
	walletDao.updateOne(walletAssetGiver);
	// change wallet balance Recipient
	walletAssetRecipient.put(assetName, walletAssetRecipient.get(assetName) + transaction.getAssetAmount());
	walletDao.updateOne(walletAssetRecipient);

	System.out.println(6);
	// change saldo Giver
	bankAccountAssetGiver.setBalanceEur(bankAccountAssetGiver.getBalanceEur() + transaction.totalPrice() - fee / 2);
	bankAccountService.updateOne(bankAccountAssetGiver);
	// change saldo Recipient
	bankAccountAssetRecipient.setBalanceEur(bankAccountAssetRecipient.getBalanceEur() - (transaction.totalPrice() + fee / 2));
	bankAccountService.updateOne(bankAccountAssetRecipient);
	// store transaction
	int transactionId = transactionRepository.storeOne(transaction);

	return transaction;
  }

  private void storeTransaction(Transaction transaction) {
	transactionRepository.storeOne(transaction);
  }

  // calculates Euro amount with user's input for requested amount
  private double calculateEuroByAmountTrade(double amount, String assetName) {
	return amount * rateService.getLatestByName(assetName).getRate();
  }

  // update bank balance with user input
  private void updateBankAccount(TradeBankDto trade) {
	if (!isCustomerBuying(trade.getAmountTrade())) {
	  updateWallet(trade);
	  bankAccountService.updateOne(trade.getCustomer().getBankAccount());
	}
  }

  // loop over Wallet, if asset in wallet then update, if not then add
  private void updateWallet(TradeBankDto trade) {
	Map<String, Double> wallet = trade.getCustomer().getWallet();
	if (wallet.containsKey(trade.getAssetNameTrade())) {
	  wallet.put(trade.getAssetNameTrade(), (wallet.get(trade.getAssetNameTrade()) + trade.getAmountTrade()));
	} else {
	  wallet.put(trade.getAssetNameTrade(), trade.getAmountTrade());
	}
	customerService.updateOne(trade.getCustomer());
//	walletDao.updateOne(trade.getCustomer().getWallet());
  }

  private boolean isCustomerBuying(double amountTrade) { return amountTrade > 0; }

  private boolean isBalanceEnoughToBuy(double balance, double amount) { return (balance > amount); }

  private boolean isAssetEnoughToSell(TradeBankDto trade) {
	return trade.getCustomer().getWallet().get(trade.getAssetNameTrade()) > Math.abs(trade.getAmountTrade());
  }

  private boolean isAssetInWallet(String assetName, Map<String, Double> customerWallet) {
	return customerWallet.containsKey(assetName);
  }

}


