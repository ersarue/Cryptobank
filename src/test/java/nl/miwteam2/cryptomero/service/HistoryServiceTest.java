package nl.miwteam2.cryptomero.service;

import nl.miwteam2.cryptomero.domain.Asset;
import nl.miwteam2.cryptomero.domain.Customer;
import nl.miwteam2.cryptomero.domain.Transaction;
import nl.miwteam2.cryptomero.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
* @author Petra Coenen
*/

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HistoryServiceTest {

    private HistoryService serviceUnderTest;

    private TransactionRepository transactionRepositoryMock = Mockito.mock(TransactionRepository.class);

    private Customer jan;
    private Customer piet;
    private Asset bitcoin;
    private Asset ethereum;
    private Transaction transaction1;
    private Transaction transaction2;
    private List<Transaction> sellerHistoryJan;
    private List<Transaction> buyerHistoryJan;
    private List<Transaction> allTransactions;
    private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public HistoryServiceTest() {
        super();
        this.serviceUnderTest = new HistoryService(transactionRepositoryMock);
    }

    @BeforeAll
    public void fillMocks() {
        jan = new Customer("Jan", "de", "Vries", LocalDate.parse("1970-03-04"),
                "418809458", "06-12345678");
        piet = new Customer("Piet", null, "Jansen", LocalDate.parse("1959-10-23"),
                "457506064", "06-12345777");
        bitcoin = new Asset("Bitcoin", "BTC");
        ethereum = new Asset("Ethereum", "ETH");
        transaction1 = new Transaction(1, LocalDateTime.parse("2022-03-31 12:15:24", FORMATTER), jan,
                piet, bitcoin, 0.6, 25555.37, 16.50);
        transaction2 = new Transaction(2, LocalDateTime.parse("2022-03-31 12:20:45", FORMATTER), piet,
                jan, ethereum, 2.4, 7383.42, 3.75);

        sellerHistoryJan = new ArrayList<>();
        sellerHistoryJan.add(transaction1);
        buyerHistoryJan = new ArrayList<>();
        buyerHistoryJan.add(transaction2);
        allTransactions = new ArrayList<>();
        Collections.addAll(allTransactions, transaction1, transaction2);

        Mockito.when(transactionRepositoryMock.findById(1)).thenReturn(transaction1);
        Mockito.when(transactionRepositoryMock.findById(2)).thenReturn(transaction2);
        Mockito.when(transactionRepositoryMock.getSellerHistory(1)).thenReturn(sellerHistoryJan);
        Mockito.when(transactionRepositoryMock.getBuyerHistory(1)).thenReturn(buyerHistoryJan);
    }

    // TODO: Check return values! Test might not be correct. (PC)
    @Test
    void storeOne() {
        int expected = 0;
        int actual = serviceUnderTest.storeOne(new Transaction(LocalDateTime.parse("2022-03-31 13:04:10", FORMATTER), jan, piet,
                bitcoin, 4, 170341.34, 160.34));
        assertThat(actual).isNotNull().isEqualTo(expected);
    }

    @Test
    void getAllHistory() {
        List<Transaction> expected = allTransactions;
        List<Transaction> actual = serviceUnderTest.getAllHistory(1);
        assertThat(actual).isNotNull().isEqualTo(expected);
    }

    @Test
    void getSellerHistory() {
        List<Transaction> expected = sellerHistoryJan;
        List<Transaction> actual = serviceUnderTest.getSellerHistory(1);
        assertThat(actual).isNotNull().isEqualTo(expected);
    }

    @Test
    void getBuyerHistory() {
        List<Transaction> expected = buyerHistoryJan;
        List<Transaction> actual = serviceUnderTest.getBuyerHistory(1);
        assertThat(actual).isNotNull().isEqualTo(expected);
    }
}