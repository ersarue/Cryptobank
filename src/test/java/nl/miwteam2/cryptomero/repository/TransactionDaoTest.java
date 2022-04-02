package nl.miwteam2.cryptomero.repository;

import nl.miwteam2.cryptomero.domain.Transaction;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TransactionDaoTest {

    private TransactionDao daoUnderTest;

    private Transaction transaction1;
    private Transaction transaction2;
    private List<Transaction> sellerHistoryCustomer1;
    private List<Transaction> buyerHistoryCustomer1;
    private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public TransactionDaoTest(TransactionDao daoUnderTest) {
        super();
        this.daoUnderTest = daoUnderTest;
    }

    @BeforeAll
    void setupTest() {
        transaction1 = new Transaction(1, LocalDateTime.parse("2022-03-30 11:20:34", FORMATTER), null,
                null, null, 0.5, 21409.61, 30.50);
        transaction2 = new Transaction(2, LocalDateTime.parse("2022-03-30 11:21:50", FORMATTER), null,
                null, null, 2.0, 6099.62, 4.25);
        sellerHistoryCustomer1 = new ArrayList<>();
        sellerHistoryCustomer1.add(transaction1);
        buyerHistoryCustomer1 = new ArrayList<>();
        buyerHistoryCustomer1.add(transaction2);
    }

    @Test
    void storeOne() {
//        TODO: Implement test (PC)
    }

    @Test
    void findById() {
        Transaction actual = daoUnderTest.findById(1);
        Transaction expected = transaction1;
        assertThat(actual).isNotNull().isEqualTo(expected);
    }

    @Test
    void getSellerHistory() {
        List<Transaction> actual = daoUnderTest.getSellerHistory(1);
        List<Transaction> expected = sellerHistoryCustomer1;
        assertThat(actual).isNotNull().isEqualTo(expected);
    }

    @Test
    void getBuyerHistory() {
        List<Transaction> actual = daoUnderTest.getBuyerHistory(1);
        List<Transaction> expected = buyerHistoryCustomer1;
        assertThat(actual).isNotNull().isEqualTo(expected);
    }

    @Test
    void findAssetGiverId() {
        int actual = daoUnderTest.findAssetGiverId(transaction1);
        int expected = 1;
        assertThat(actual).isNotNull().isEqualTo(expected);

        actual = daoUnderTest.findAssetGiverId(transaction2);
        expected = 2;
        assertThat(actual).isNotNull().isEqualTo(expected);
    }

    @Test
    void findAssetRecipientId() {
        int actual = daoUnderTest.findAssetRecipientId(transaction1);
        int expected = 2;
        assertThat(actual).isNotNull().isEqualTo(expected);

        actual = daoUnderTest.findAssetRecipientId(transaction2);
        expected = 1;
        assertThat(actual).isNotNull().isEqualTo(expected);
    }

    @Test
    void findAssetNameOfTransaction() {
        String actual = daoUnderTest.findAssetNameOfTransaction(transaction1);
        String expected = "Bitcoin";
        assertThat(actual).isNotNull().isEqualTo(expected);

        actual = daoUnderTest.findAssetNameOfTransaction(transaction2);
        expected = "Ethereum";
        assertThat(actual).isNotNull().isEqualTo(expected);
    }
}