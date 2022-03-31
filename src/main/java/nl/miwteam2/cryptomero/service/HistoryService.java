package nl.miwteam2.cryptomero.service;

import nl.miwteam2.cryptomero.domain.Transaction;
import nl.miwteam2.cryptomero.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
* @author Petra Coenen
*/

@Service
public class HistoryService {

    private TransactionRepository transactionRepository;

    @Autowired
    public HistoryService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**
     * Stores a transaction in the database.
     * @param  transaction    The transaction to be stored.
     * @return                The auto-generated id from the database if the transaction was stored successfully and 0
     *                        if the transaction could not be stored.
     */
    public int storeOne(Transaction transaction) { return transactionRepository.storeOne(transaction); }

    /**
     * Retrieves all transactions from the database in which the customer was involved either as seller or buyer of
     * a crypto asset.
     * @param id            Id of the customer user account.
     * @return              A list of all the transactions in which the customer was involved.
     */
    public List<Transaction> getAllHistory(int id) {
        List<Transaction> sellerHistory = getSellerHistory(id);
        List<Transaction> buyerHistory = getBuyerHistory(id);
        return Stream.concat(sellerHistory.stream(), buyerHistory.stream())
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all transactions where the customer acted as seller of a crypto asset.
     * @param id            Id of the customer user account.
     * @return              A list of all transactions where the customer acted as seller of a crypto asset.
     */
    public List<Transaction> getSellerHistory(int id) { return transactionRepository.getSellerHistory(id); }

    /**
     * Retrieves all transactions where the customer acted as buyer of a crypto asset.
     * @param id            Id of the customer user account.
     * @return              A list of all transactions where the customer acted as buyer of a crypto asset.
     */
    public List<Transaction> getBuyerHistory(int id) { return transactionRepository.getBuyerHistory(id); }
}
