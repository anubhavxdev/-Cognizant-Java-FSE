package com.cognizant.springlearn;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.MvcResult;

import com.cognizant.springlearn.controller.CountryController;

import java.util.Base64;

@SpringBootTest
@AutoConfigureMockMvc
class SpringLearnApplicationTests {

    @Autowired
    private CountryController countryController;

    @Autowired
    private MockMvc mvc;

    private String token;

    @BeforeEach
    public void setup() throws Exception {
        String authHeader = "Basic " + Base64.getEncoder().encodeToString("user:pwd".getBytes());
        MvcResult result = mvc.perform(get("/authenticate").header("Authorization", authHeader))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        // Extract token value from JSON format {"token":"JWT_TOKEN_VALUE"}
        token = response.split("\"")[3];
    }

    @Test
    void contextLoads() {
        assertNotNull(countryController);
    }

    @Test
    public void testGetCountry() throws Exception {
        ResultActions actions = mvc.perform(get("/countries/IN").header("Authorization", "Bearer " + token));
        actions.andExpect(status().isOk());
        actions.andExpect(jsonPath("$.code").exists());
        actions.andExpect(jsonPath("$.code").value("IN"));
        actions.andExpect(jsonPath("$.name").exists());
        actions.andExpect(jsonPath("$.name").value("India"));
    }

    @Test
    public void testGetCountryException() throws Exception {
        ResultActions actions = mvc.perform(get("/countries/az").header("Authorization", "Bearer " + token));
        actions.andExpect(status().isNotFound());
        actions.andExpect(status().reason("Country not found"));
    }

    @Test
    public void testUpdateEmployeeException() throws Exception {
        String employeeJson = "{\"id\":999,\"name\":\"Non Existent\",\"salary\":50000.0,\"permanent\":true,\"dateOfBirth\":\"01/01/2000\",\"department\":{\"id\":1,\"name\":\"IT\"},\"skills\":[{\"id\":1,\"name\":\"Java\"}]}";
        ResultActions actions = mvc.perform(put("/employees").header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeJson));
        actions.andExpect(status().isNotFound());
        actions.andExpect(status().reason("Employee not found"));
    }
}
