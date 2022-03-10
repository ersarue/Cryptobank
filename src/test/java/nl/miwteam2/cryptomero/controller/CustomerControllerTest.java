package nl.miwteam2.cryptomero.controller;

import nl.miwteam2.cryptomero.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    private MockMvc mockServer;

    @MockBean
    private CustomerService customerService;

    @Autowired
    public CustomerControllerTest(MockMvc mockServer){
        super();
        this.mockServer=mockServer;
    }

    @Test
    void storeOne() {
        MockHttpServletRequestBuilder request;
        request = MockMvcRequestBuilders.post("/customer");
        //request.param("Authentication", "token");
        request.contentType(MediaType.APPLICATION_JSON);
        //request.content("{\"idAccount\":10,\"email\":\"stijnklijn@gmail10.com\",\"salt\":\"d43199d1\",\"firstName\":\"Stijn\",\"namePrefix\":null,\"lastName\":\"Klijn\",\"dob\":\"1985-11-13\",\"bsn\":\"123456782\",\"telephone\":\"0628328571\",\"address\":{\"idAddress\":1,\"streetName\":\"Schieweg\",\"houseNo\":210,\"houseAdd\":\"A\",\"postalCode\":\"3038BN\",\"city\":\"Rotterdam\"},\"bankAccount\":{\"iban\":\"NL54CRME2679050879\",\"balanceEur\":1000000.0},\"wallet\":{}}");
        request.content("{\"email\":\"stijnklijn@gmail24.com\",\"firstName\":\"Stijn\",\"lastName\":\"Klijn\",\"dob\":\"1985-11-13\",\"bsn\":\"123456782\",\"telephone\":\"0628328571\",\"address\":{\"houseNo\":210,\"postalCode\":\"3038BN\"}}");

        try {
            System.out.println("--------------------------------------------^^^-----------------------1");
            ResultActions response = mockServer.perform(request);
            System.out.println("--------------------------------------------^^^-----------------------2");
            //response.andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());
            response.andExpect(MockMvcResultMatchers.status().isOk());
            System.out.println("--------------------------------------------^^^-----------------------3");

            String contents = response.andReturn().getResponse().getContentAsString();
            System.out.println(contents.toString());
            System.out.println("--------------------------------------------^^^-----------------------4");

            assertThat(contents).startsWith("{\"klantnummer\":3").contains("Venendaal").endsWith("}");
        } catch (Exception e) {
            System.out.printf("Test mislukt.");
        }
    }



    @Test
    void findById() {
    }
}