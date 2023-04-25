package nl.miwteam2.cryptomero.repository;

import nl.miwteam2.cryptomero.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* @author Petra Coenen
*/

@Repository
public class TransactionRepository {

    private final Logger logger = LoggerFactory.getLogger(TransactionRepository.class);

    private TransactionDao transactionDao;
    private CustomerRepository customerRepository;
    private AssetDao assetDao;

    @Autowired
    public TransactionRepository(TransactionDao transactionDao, CustomerRepository customerRepository, AssetDao assetDao) {
        this.transactionDao = transactionDao;
        this.customerRepository = customerRepository;
        this.assetDao = assetDao;
        logger.info("New TransactionRepository");
    }

    public int storeOne(Transaction transaction) {
        return transactionDao.storeOne(transaction);
    }

    /**
     * Retrieves a transaction from the database by id
     * @param id            Id of the transaction to be retrieved
     * @return              The transaction object
     */
    public Transaction findById(int id) {
        Transaction transaction = transactionDao.findById(id);
        return completeTransactionObject(transaction);
    }

    /**
     * Retrieves all transactions where the customer acted as seller of a crypto asset.
     * @param id            Id of the customer user account.
     * @return              A list of all transactions.
     */
    public List<Transaction> getSellerHistory(int id) {
        List<Transaction> transactions = transactionDao.getSellerHistory(id);
        transactions.forEach(this::completeTransactionObject);
        return transactions;
    }

    /**
     * Retrieves all transactions where the customer acted as buyer of a crypto asset.
     * @param id            Id of the customer user account.
     * @return              A list of all transactions.
     */
    public List<Transaction> getBuyerHistory(int id) {
        List<Transaction> transactions = transactionDao.getBuyerHistory(id);
        transactions.forEach(this::completeTransactionObject);
        return transactions;
    }

    /**
     * Adds the corresponding customer and asset objects to a 'naked' transaction object that has only primitive values.
     * @param transaction   The transaction with only primitive values.
     * @return              The transaction with its fields set with the corresponding customer and asset objects.
     */
    private Transaction completeTransactionObject(Transaction transaction) {
        Customer asset_giver = customerRepository.findById(transactionDao.findAssetGiverId(transaction));
        Customer asset_recipient = customerRepository.findById(transactionDao.findAssetRecipientId(transaction));
        Asset asset = assetDao.findByName(transactionDao.findAssetNameOfTransaction(transaction));

        transaction.setAssetGiver(asset_giver);
        transaction.setAssetRecipient(asset_recipient);
        transaction.setAsset(asset);

        return transaction;
    }
}
