package nl.miwteam2.cryptomero.service;

import nl.miwteam2.cryptomero.domain.Customer;
import nl.miwteam2.cryptomero.repository.CustomerRepository;
import nl.miwteam2.cryptomero.repository.RootRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

/**
 * @author Samuël Geurts & Stijn Klijn
 */

class CustomerServiceTest {

    private CustomerService serviceUnderTest;

    private CustomerRepository customerRepository = Mockito.mock(CustomerRepository.class);
    private AddressService addressService = Mockito.mock(AddressService.class);
    private BankAccountService bankAccountService = Mockito.mock(BankAccountService.class);
    private UserAccountService userAccountService = Mockito.mock(UserAccountService.class);

    CustomerServiceTest() {
        this.serviceUnderTest = new CustomerService(customerRepository, addressService, bankAccountService, userAccountService);
    }

    @BeforeEach
    private void fillMocks() {
        Customer customer1 = new Customer("a","b","c", LocalDate.now(),"e","f");
        customer1.setIdAccount(1);
        Mockito.when(customerRepository.findById(1)).thenReturn(customer1);
    }

    @Test
    void storeOne() {
        fail("To be implemented");
    }

    @Test
    void findById() {
        Customer expected =  new Customer("a","b","c", LocalDate.now(),"e","f");
        expected.setIdAccount(1);
        Customer actual = serviceUnderTest.findById(1);
        assertThat(actual).isNotNull().isEqualTo(expected); //methode van AssertJ library
    }



}