package com.innopolis.innometrics.restapi;

import com.innopolis.innometrics.restapi.controller.AdminAPI;
import com.innopolis.innometrics.restapi.controller.AuthAPI;
import com.innopolis.innometrics.restapi.controller.DataCollectorsAPI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class RestapiApplicationTests {
    @Autowired
    AdminAPI adminAPI;

    @Autowired
    AuthAPI authAPI;

    @Autowired
    DataCollectorsAPI dataCollectorsAPI;

    @Test
    void contextLoads() {
        assertTrue(true);
    }
}
