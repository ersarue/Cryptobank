package nl.miwteam2.cryptomero.controller;

import nl.miwteam2.cryptomero.service.Authentication.AuthenticationService;
import nl.miwteam2.cryptomero.service.HistoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.*;

/**
* @author Petra Coenen
*/

@WebMvcTest(HistoryController.class)
class HistoryControllerTest {

    private MockMvc mockServer;

    @MockBean
    private HistoryService mockHistoryService;
    @MockBean
    private AuthenticationService mockAuthService;

    @Autowired
    public HistoryControllerTest(MockMvc mockServer) {
        super();
        this.mockServer = mockServer;
    }

//    TODO: Check test setup (PC)
    @Test
    void testGetAllHistory() {
        MockHttpServletRequestBuilder request;
        request = MockMvcRequestBuilders.get("/testHistory/1");
        try {
            MockHttpServletResponse response = mockServer.perform(request).andReturn().getResponse();
            assertThat((response).getStatus()).isEqualTo(HttpStatus.OK.value());
            assertThat(response.getContentAsString()).startsWith("[]");
        } catch (Exception e) {
            System.out.println("Test failed. \n" + e.getMessage());
        }
    }
}