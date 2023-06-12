package com.innopolis.innometrics.restapi.controller;

import com.innopolis.innometrics.restapi.repository.ProjectRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest
@AutoConfigureMockMvc
class ProjectAPITest {

    @Autowired
    private MockMvc mockMvc;

    @SneakyThrows
    @Test
    void createMeasurementTypeNullValuesTest() {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/V1/Admin/project/test")
                        .param("userEmail", "test@test.test")
                        .param("manager", "true"))
                .andReturn();
        assertEquals("", result.getResponse().getContentAsString());
    }
}
