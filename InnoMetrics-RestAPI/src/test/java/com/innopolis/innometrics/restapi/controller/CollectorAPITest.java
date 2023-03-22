package com.innopolis.innometrics.restapi.controller;

import com.innopolis.innometrics.restapi.entity.CollectorVersion;
import com.innopolis.innometrics.restapi.repository.CollectorVersionRepository;
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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest
@AutoConfigureMockMvc
class CollectorAPITest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CollectorVersionRepository versionRepository;

    @SneakyThrows
    @Test
    void getWithNonExistTest() {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/V1/Admin/collector-version")
                        .param("osversion", "new"))
                .andReturn();
        assertEquals("", result.getResponse().getContentAsString());
    }

    @SneakyThrows
    @Test
    void getWithExistTest() {
        CollectorVersion version = createVersion();
        versionRepository.save(version);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/V1/Admin/collector-version")
                        .param("osversion", "1"))
                .andReturn();
        assertEquals("test", result.getResponse().getContentAsString());
    }

    @SneakyThrows
    @Test
    void updateCurrentVersionTest() {
        CollectorVersion version = createVersion();
        versionRepository.save(version);
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/V1/Admin/collector-version")
                        .param("osversion", "1")
                        .param("newVersion", "2"))
                .andReturn();
        assertEquals("true", result.getResponse().getContentAsString());
    }

    private CollectorVersion createVersion() {
        CollectorVersion version = new CollectorVersion();
        version.setOsVersion("1");
        version.setValue("test");
        version.setCreatedBy("Dariya");
        version.setCreationDate(null);
        version.setLastUpdate(null);
        version.setUpdateBy("Dariya");
        return version;
    }
}
