package nl.miwteam2.cryptomero.repository;

import nl.miwteam2.cryptomero.domain.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
* @author Petra Coenen
*/

public class TransactionRepository {

    private TransactionDao transactionDao;
    private JdbcCustomerDao customerDao;
    private JdbcAssetDao assetDao;

    @Autowired
    public TransactionRepository(TransactionDao transactionDao, JdbcCustomerDao customerDao, JdbcAssetDao assetDao) {
        this.transactionDao = transactionDao;
        this.customerDao = customerDao;
        this.assetDao = assetDao;
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
        Customer asset_giver = customerDao.findById(transactionDao.findAssetGiverId(transaction));
        Customer asset_recipient = customerDao.findById(transactionDao.findAssetRecipientId(transaction));
        Asset asset = assetDao.findByName(transactionDao.findAssetNameOfTransaction(transaction));

        transaction.setAssetGiver(asset_giver);
        transaction.setAssetRecipient(asset_recipient);
        transaction.setAsset(asset);

        return transaction;
    }
}
