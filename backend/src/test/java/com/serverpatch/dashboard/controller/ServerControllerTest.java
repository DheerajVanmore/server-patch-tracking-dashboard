package com.serverpatch.dashboard.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverpatch.dashboard.dto.ServerDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ServerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetServers() throws Exception {
        mockMvc.perform(get("/api/servers"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testCreateServer() throws Exception {
        ServerDTO dto = new ServerDTO();
        dto.setHostname("test-controller-srv");
        dto.setIpAddress("10.0.0.100");
        dto.setOs("Windows");
        dto.setEnvironment("DEV");
        dto.setOwnerTeam("Alpha");

        mockMvc.perform(post("/api/servers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.hostname", is("test-controller-srv")));
    }
}
