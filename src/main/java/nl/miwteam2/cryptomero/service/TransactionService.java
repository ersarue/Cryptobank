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

  public String tradeWithBank(TradeBankDto trade) throws Exception {
	Transaction transaction = new Transaction(LocalDateTime.now(),
			isCustomerBuying(trade.getAmountTrade()) ? bank : trade.getCustomer(),
			isCustomerBuying(trade.getAmountTrade()) ? trade.getCustomer() : bank,
			assetService.findByName(trade.getAssetNameTrade()), Math.abs(trade.getAmountTrade()),
			calculateEuroByAmountTrade(Math.abs(trade.getAmountTrade()), trade.getAssetNameTrade()),
			calculateEuroByAmountTrade(Math.abs(trade.getAmountTrade()*TRANSACTION_FEE), trade.getAssetNameTrade()));
	if (bank == trade.getCustomer()) { // making sure that client makes trade with bank
	  throw new Exception("U kunt alleen handelen met de bank of met een andere klant");
	} else {
	  if (isCustomerBuying(trade.getAmountTrade())) { // buying from bank
		if (!isBalanceEnoughToBuy(trade.getCustomer().getBankAccount().getBalanceEur(),
				calculateEuroByAmountTrade(Math.abs(trade.getAmountTrade()), trade.getAssetNameTrade()))) {
		  throw new Exception("U kunt niet kopen van de bank wegens onvoldoende saldo");
		}
	  } else {  // selling to bank
		if (!(isAssetInWallet(trade.getAssetNameTrade(), trade.getCustomer().getWallet()) && isAssetEnoughToSell(trade))) {
		  throw new Exception("U heeft onvoldoende cryptomunten in portefeuille om te verkopen aan de bank");
		}
	  }
	  updateBankAccount(transaction); // update bank account
	  updateWallet(trade);            // update wallet
	  storeTransaction(transaction); // store transaction
		return "Transactie verwerkt";
	}
  }

  public String tradeWithUser(TransactionDto transactionDto) throws Exception {
	//todo cut method in pieces

	// make transaction from transactionDto
	Transaction transaction = new Transaction(transactionDto);
	// compute fee and add to transaction object
	double fee = transaction.getEurAmount() * TRANSACTION_FEE;
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

	//get asset balance asset Giver
	  double assetAmountGiver = 0;
	  if (walletAssetGiver.containsKey(assetName)) {
		  assetAmountGiver = walletAssetGiver.get(assetName);
	  }
	double BalanceEurGiver = bankAccountAssetGiver.getBalanceEur(); //BalanceEur assetGiver
	double BalanceEurRecipient = bankAccountAssetRecipient.getBalanceEur(); //BalanceEur assetGiver

	  if (assetAmountGiver < transaction.getAssetAmount()) {
		  throw new Exception("De verkoper heeft onvoldoende cryptomunten in portefeuille");
	  } else if (BalanceEurGiver < transaction.getEurFee() / 2) {
		  throw new Exception("De verkoper heeft onvoldoende saldo om de transactiekosten te betalen");
	  } else if (BalanceEurRecipient < transaction.getEurAmount() + transaction.getEurFee() / 2) {
		  throw new Exception("De koper heeft onvoldoende saldo");
	  }

	System.out.println(5);

	// change wallet balance Giver and store
	walletAssetGiver.put(assetName, walletAssetGiver.get(assetName) - transaction.getAssetAmount());
	walletDao.update(transaction.getAssetGiver().getIdAccount(), walletAssetGiver);
	// change wallet balance Recipient
	// check of asset al bestaat in wallet van recipient
	if (walletAssetRecipient.containsKey(assetName)) {
		walletAssetRecipient.put(assetName, walletAssetRecipient.get(assetName) + transaction.getAssetAmount());
	} else {
		walletAssetRecipient.put(assetName, transaction.getAssetAmount());
	}



	walletDao.update(transaction.getAssetRecipient().getIdAccount(), walletAssetRecipient);

	System.out.println(6);
	// change saldo Giver
	bankAccountAssetGiver.setBalanceEur(bankAccountAssetGiver.getBalanceEur() + transaction.getEurAmount() - fee / 2);
	bankAccountService.updateOne(bankAccountAssetGiver);
	// change saldo Recipient
	bankAccountAssetRecipient.setBalanceEur(bankAccountAssetRecipient.getBalanceEur() - (transaction.getEurAmount() + fee / 2));
	bankAccountService.updateOne(bankAccountAssetRecipient);
	// store transaction
	int transactionId = transactionRepository.storeOne(transaction);

	return "Transactie verwerkt";
  }

  private void storeTransaction(Transaction transaction) {
	transactionRepository.storeOne(transaction);
  }

  // calculates Euro amount with user's input for requested amount
  private double calculateEuroByAmountTrade(double amount, String assetName) {
	return amount * rateService.getLatestByName(assetName).getRate();
  }

  // update bank balance with user input
  private void updateBankAccount(Transaction transaction) {
	BankAccount bankAccount;
	if (transaction.getAssetGiver().getIdAccount() != 1) { //IdAccount = 1 is de bank, client verkoopt assets aan de bank en betaalt fee
	  bankAccount = transaction.getAssetGiver().getBankAccount();
	  bankAccount.setBalanceEur(bankAccount.getBalanceEur() + transaction.getEurAmount() - transaction.getEurFee());
	  bankAccount.setUserAccount(transaction.getAssetGiver());
	} else { //client koopt assets van de bank en betaald assets prijs + fee
	  bankAccount = transaction.getAssetRecipient().getBankAccount();
	  bankAccount.setBalanceEur(bankAccount.getBalanceEur() - transaction.getEurAmount() - transaction.getEurFee());
	  bankAccount.setUserAccount(transaction.getAssetRecipient());
	}
	bankAccountService.updateOne(bankAccount);
  }


  // loop over Wallet, if asset in wallet then update, if not then add
  private void updateWallet(TradeBankDto trade) {
	Map<String, Double> wallet = trade.getCustomer().getWallet();
	if (wallet.containsKey(trade.getAssetNameTrade())) {
	  wallet.put(trade.getAssetNameTrade(),
			  (wallet.get(trade.getAssetNameTrade()) - trade.getAmountTrade()));
	  if (wallet.get(trade.getAssetNameTrade()) == 0) {
		  wallet.remove(trade.getAssetNameTrade());
	  }
	} else {
	  wallet.put(trade.getAssetNameTrade(), Math.abs(trade.getAmountTrade()));
	}
	walletDao.update(trade.getCustomer().getIdAccount(), wallet);
  }

  private boolean isCustomerBuying(double amountTrade) { return amountTrade < 0; }

  private boolean isBalanceEnoughToBuy(double balance, double amount) {
	return (balance >= (amount+TRANSACTION_FEE));
  }

  private boolean isAssetEnoughToSell(TradeBankDto trade) {
	return trade.getCustomer().getWallet().get(trade.getAssetNameTrade()) >= Math.abs(trade.getAmountTrade());
  }

  private boolean isAssetInWallet(String assetName, Map<String, Double> customerWallet) {
	return customerWallet.containsKey(assetName);
  }

}


