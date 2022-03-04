//package nl.miwteam2.cryptomero.service;
//
//import nl.miwteam2.cryptomero.domain.Customer;
//import nl.miwteam2.cryptomero.repository.GenericDao;
//import nl.miwteam2.cryptomero.repository.JdbcCustomerDao;
//import nl.miwteam2.cryptomero.repository.RootRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//
//import java.time.LocalDate;
//
//import static org.assertj.core.api.Assertions.*;
//
//class CustomerServiceTest {
//
//    private CustomerService serviceUnderTest;
//
//    private GenericDao<Customer> customerDaoMock = Mockito.mock(JdbcCustomerDao.class);
//    private RootRepository rootRepositoryMock = Mockito.mock(RootRepository.class);
//
//
//    CustomerServiceTest() {
//        this.serviceUnderTest = new CustomerService(customerDaoMock,rootRepositoryMock);
//    }
//
//    @BeforeEach
//    private void fillMocks() {
//        Customer customer1 = new Customer("a","b","c", LocalDate.now(),"e","f");
//        customer1.setIdAccount(1);
//
//        Mockito.when(customerDaoMock.findById(1)).thenReturn(customer1);
//        Mockito.when(rootRepositoryMock.findCustomerById(1)).thenReturn(customer1);
//    }
//
//    @Test
//    void findById() {
//        Customer expected =  new Customer("a","b","c", LocalDate.now(),"e","f");
//        expected.setIdAccount(1);
//        Customer actual = serviceUnderTest.findById(1);
//        System.out.println(actual.getIdAccount());
//        System.out.println(expected.getIdAccount());
//
//        assertThat(actual).isNotNull().isEqualTo(expected); //methode van AssertJ library
//    }
//
//    @Test
//    void storeOne() {
//    }
//}