package nl.miwteam2.cryptomero.repository;

import nl.miwteam2.cryptomero.domain.Asset;
import nl.miwteam2.cryptomero.domain.Customer;
import nl.miwteam2.cryptomero.domain.Transaction;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
* @author Petra Coenen
*/

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TransactionRepositoryTest {

    private TransactionRepository repositoryUnderTest;

    private TransactionDao transactionDaoMock = Mockito.mock(TransactionDao.class);
    private CustomerRepository customerRepoMock = Mockito.mock(CustomerRepository.class);
    private AssetDao assetDaoMock = Mockito.mock(AssetDao.class);

    private Transaction transaction1;
    private Transaction transaction2;
    private List<Transaction> sellerHistoryJan;
    private List<Transaction> buyerHistoryJan;
    private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public TransactionRepositoryTest() {
        super();
        this.repositoryUnderTest = new TransactionRepository(transactionDaoMock, customerRepoMock, assetDaoMock);
    }

    @BeforeAll
    void setup() {
        Customer jan = new Customer("Jan", "de", "Vries", LocalDate.parse("1970-03-04"),
                "418809458", "06-12345678");
        Customer piet = new Customer("Piet", null, "Jansen", LocalDate.parse("1959-10-23"),
                "457506064", "06-12345777");
        Asset bitcoin = new Asset("Bitcoin", "BTC");
        Asset ethereum = new Asset("Ethereum", "ETH");
        transaction1 = new Transaction(1, LocalDateTime.parse("2022-03-31 12:15:24", FORMATTER), jan,
                piet, bitcoin, 0.6, 25555.37, 16.50);
        transaction2 = new Transaction(2, LocalDateTime.parse("2022-03-31 12:20:45", FORMATTER), piet,
                jan, ethereum, 2.4, 7383.42, 3.75);

        sellerHistoryJan = new ArrayList<>();
        sellerHistoryJan.add(transaction1);
        buyerHistoryJan = new ArrayList<>();
        buyerHistoryJan.add(transaction2);

        Mockito.when(transactionDaoMock.findById(1)).thenReturn(transaction1);
        Mockito.when(transactionDaoMock.findById(2)).thenReturn(transaction2);
        Mockito.when(transactionDaoMock.getSellerHistory(1)).thenReturn(sellerHistoryJan);
        Mockito.when(transactionDaoMock.getBuyerHistory(1)).thenReturn(buyerHistoryJan);
        Mockito.when(customerRepoMock.findById(1)).thenReturn(jan);
        Mockito.when(customerRepoMock.findById(2)).thenReturn(jan);
        Mockito.when(assetDaoMock.findByName("bitcoin")).thenReturn(bitcoin);
    }

    @Test
    void storeOne() {
//        TODO: Double check returns of the storeOne() method (PC)
        int actual = repositoryUnderTest.storeOne(transaction1);
        int expected = 0;
        assertThat(actual).isNotNull().isEqualTo(expected);
    }

    @Test
    void findById() {
        Transaction actual = repositoryUnderTest.findById(1);
        Transaction expected = transaction1;
        assertThat(actual).isNotNull().isEqualTo(expected);

        actual = repositoryUnderTest.findById(2);
        expected = transaction2;
        assertThat(actual).isNotNull().isEqualTo(expected);
    }

    @Test
    void getSellerHistory() {
        List<Transaction> actual = repositoryUnderTest.getSellerHistory(1);
        List<Transaction> expected = sellerHistoryJan;
        assertThat(actual).isNotNull().isEqualTo(expected);
    }

    @Test
    void getBuyerHistory() {
        List<Transaction> actual = repositoryUnderTest.getBuyerHistory(1);
        List<Transaction> expected = buyerHistoryJan;
        assertThat(actual).isNotNull().isEqualTo(expected);
    }
}