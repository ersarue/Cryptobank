package nl.miwteam2.cryptomero.controller;

import nl.miwteam2.cryptomero.service.BankAccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@WebMvcTest(BankAccountController.class)
class BankAccountControllerTest {
    private MockMvc mockServer;

    @MockBean
    private BankAccountService mockBankaccountService;
    @Autowired
    public BankAccountControllerTest(MockMvc mockServer){
        super();
        this.mockServer=mockServer;
    }
    @Test
    void updateBankAccount() {


    }
}