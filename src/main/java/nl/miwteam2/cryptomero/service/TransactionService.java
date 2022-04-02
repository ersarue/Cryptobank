package nl.miwteam2.cryptomero.service;

import nl.miwteam2.cryptomero.domain.*;
import nl.miwteam2.cryptomero.repository.TransactionRepository;
import nl.miwteam2.cryptomero.repository.WalletDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

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
  private WalletDao walletDao;

  public TransactionService(TransactionRepository transactionRepository, BankAccountService bankAccountService, WalletDao walletDao) {
	super();
	LOGGER.info("New TransactionService");
	this.transactionRepository = transactionRepository;
	this.bankAccountService = bankAccountService;
    this.walletDao = walletDao;
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

  public Transaction tradeWithUser(TransactionDto transactionDto) throws Exception {
      //todo cut method in pieces

      // make transaction from transactionDto
      Transaction transaction = new Transaction(transactionDto);
      // compute fee and add to transaction object
      double fee = transaction.totalPrice() * 0.0165; //todo use final
      transaction.setEurFee(fee);

      String assetName = transaction.getAsset().getAssetName();
      Map<String,Double> walletAssetGiver = transaction.getAssetGiver().getWallet();
      Map<String,Double> walletAssetRecipient = transaction.getAssetRecipient().getWallet();
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

      if (checkBalance(assetAmountGiver, transaction.getAssetAmount())) {
          throw new Exception("Cannot trade Asset. Insufficient Asset balance");
      } else if (checkBalance(BalanceEurGiver, transaction.getEurFee() / 2)) {
          throw new Exception("Cannot trade Asset. Insufficient balance Giver to pay fee");
      } else if (checkBalance(BalanceEurRecipient, transaction.totalPrice() + transaction.getEurFee() / 2)) {
          throw new Exception("Cannot trade Asset. Insufficient balance Recipient");
      }
      System.out.println(5);

      // change wallet balance Giver and store
      walletAssetGiver.put(assetName,walletAssetGiver.get(assetName) - transaction.getAssetAmount());
      walletDao.updateOne(walletAssetGiver);
      // change wallet balance Recipient
      walletAssetRecipient.put(assetName,walletAssetRecipient.get(assetName) + transaction.getAssetAmount());
      walletDao.updateOne(walletAssetRecipient);

      System.out.println(6);
      // change saldo Giver
      bankAccountAssetGiver.setBalanceEur(bankAccountAssetGiver.getBalanceEur() + transaction.totalPrice() - fee/2);
      bankAccountService.updateOne(bankAccountAssetGiver);
      // change saldo Recipient
      bankAccountAssetRecipient.setBalanceEur(bankAccountAssetRecipient.getBalanceEur() - (transaction.totalPrice() + fee/2));
      bankAccountService.updateOne(bankAccountAssetRecipient);
      // store transaction
      int transactionId = transactionRepository.storeOne(transaction);

      return transaction;
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

