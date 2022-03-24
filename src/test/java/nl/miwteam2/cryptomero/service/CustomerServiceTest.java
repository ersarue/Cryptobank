package nl.miwteam2.cryptomero.service;

import nl.miwteam2.cryptomero.domain.Address;
import nl.miwteam2.cryptomero.domain.Customer;
import nl.miwteam2.cryptomero.domain.CustomerDto;
import nl.miwteam2.cryptomero.domain.UserAccount;
import nl.miwteam2.cryptomero.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

/**
 * @author Samuël Geurts & Stijn Klijn
 */

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomerServiceTest {

    private CustomerService serviceUnderTest;

    private CustomerRepository customerRepository = Mockito.mock(CustomerRepository.class);
    private AddressService addressService = Mockito.mock(AddressService.class);
    private BankAccountService bankAccountService = Mockito.mock(BankAccountService.class);
    private UserAccountService userAccountService = Mockito.mock(UserAccountService.class);

    private CustomerDto customerDtoToStore;
    private Customer customerToStore;
    private Address validAddress;
    private int customerDtoToStoreAge;

    CustomerServiceTest() {
        this.serviceUnderTest = new CustomerService(customerRepository, addressService, bankAccountService, userAccountService);
        this.validAddress = new Address(1, "", 1, "", "", "");
    }

    @BeforeAll
    private void activateMocks() {
        //Mocks must be activated only once since they don't change as a result of testing, hence @BeforeAll
        Customer customer1 = new Customer("a","b","c", LocalDate.now(),"e","f");
        customer1.setIdAccount(1);
        Mockito.when(customerRepository.findById(1)).thenReturn(customer1);
        Mockito.when(addressService.storeAddress(validAddress)).thenReturn(1);
        Mockito.when(bankAccountService.generateIban()).thenReturn("NL12CRME6357980432");
    }

    @BeforeEach
    private void createValidCustomer() {
        //customerDtoToStore changes with each test and thus has to be recreated between tests, hence @BeforeEach
        customerDtoToStoreAge = 25;
        customerDtoToStore = new CustomerDto();
        customerDtoToStore.setFirstName("firstName");
        customerDtoToStore.setNamePrefix("namePrefix");
        customerDtoToStore.setLastName("lastName");
        customerDtoToStore.setDob(LocalDate.now().minusYears(customerDtoToStoreAge));
        customerDtoToStore.setBsn("182358197");
        customerDtoToStore.setTelephone("0612345678");
        customerDtoToStore.setEmail("example@domain.com");
        customerDtoToStore.setPassword("validPassword");
        customerDtoToStore.setAddress(validAddress);

        customerToStore = new Customer(customerDtoToStore);

        Mockito.when(userAccountService.storeOne(customerToStore)).thenReturn(new UserAccount(1, "example@domain.com", "validPassword"));
    }

    @Test
    void storeValidCustomer() {
        Customer actual = null;
        try {
            actual = serviceUnderTest.storeOne(customerDtoToStore);
        } catch (Exception e) {
            fail("Storage of valid customer yielded exception: " + e.getMessage());
        }
        assertThat(actual).isNotNull();
        assertThat(actual.getBankAccount()).isNotNull();
        assertThat(actual.getBankAccount().getIban()).isEqualTo("NL12CRME6357980432");
    }

    @Test
    void storeCustomerWithInvalidFieldLength() {
        Exception exception = null;
        try {
            customerDtoToStore.setFirstName("");
            serviceUnderTest.storeOne(customerDtoToStore);
            fail("Storage of invalid customer succeeded");
        } catch (Exception e) {
            exception = e;
        }
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("Missing fields: first name");
    }

    @Test
    void storeCustomerWithInvalidEmail() {
        Exception exception = null;
        try {
            customerDtoToStore.setEmail("exampledomain.com");
            serviceUnderTest.storeOne(customerDtoToStore);
            fail("Storage of invalid customer succeeded");
        } catch (Exception e) {
            exception = e;
        }
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("Invalid e-mail");
    }

    @Test
    void storeCustomerWithIllegalAge() {
        final int ILLEGAL_AGE = 17;
        Exception exception = null;
        try {
            customerDtoToStore.setDob(LocalDate.now().minusYears(ILLEGAL_AGE));
            serviceUnderTest.storeOne(customerDtoToStore);
            fail("Storage of invalid customer succeeded");
        } catch (Exception e) {
            exception = e;
        }
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("Invalid date of birth (customer too young)");
    }

    @Test
    void storeCustomerWithInvalidBsn() {
        Exception exception = null;
        try {
            customerDtoToStore.setBsn("182358196");
            serviceUnderTest.storeOne(customerDtoToStore);
            fail("Storage of invalid customer succeeded");
        } catch (Exception e) {
            exception = e;
        }
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("Invalid bsn");
    }

    @Test
    void findById() {
        Customer expected =  new Customer("a","b","c", LocalDate.now(),"e","f");
        expected.setIdAccount(1);
        Customer actual = serviceUnderTest.findById(1);
        assertThat(actual).isNotNull().isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(strings = {"aaaaaaaa", "kdjsfuv19752", "jjjjaaAnnN", "worstworst", "2Be2Be2Be", "12345678", "catcatcat",
                            "12123"})
    void testRepetitivePasswords(String password) {
        assertThat(serviceUnderTest.isRepetitive(password)).isNotNull().isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = { "jfiKCidf", "kdjsfuv44", "WachtMot", "hallo", "zomerVakantie12","hallo1hallo"})
    void testNonRepetitivePasswords(String password) {
        assertThat(serviceUnderTest.isRepetitive(password)).isNotNull().isFalse();
    }
}