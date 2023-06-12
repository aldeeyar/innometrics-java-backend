package com.innopolis.innometrics.authserver;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class AuthServerApplicationTests {

    @Test
    void contextLoads() {
        Assertions.assertTrue(true);
    }

}
