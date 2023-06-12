package com.innopolis.innometrics.authserver.service;

import com.innopolis.innometrics.authserver.repository.TemporalTokenRepository;
import com.innopolis.innometrics.authserver.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import(value = {
        UserService.class,
        JavaMailSenderImpl.class
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
class UserServiceTest {
    private static final String EMAIL = "email";
    @Autowired
    JavaMailSender mailSender;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TemporalTokenRepository temporalTokenRepository;
    @Autowired
    UserService service;

    @Test
    void existsByIDNegative() {
        boolean result = service.existsByEmail(EMAIL);
        assertFalse(result);
    }

    @Test
    void findByEmailNegative() {
        assertNull(service.findByEmail(EMAIL));
    }

    @Test
    void getActiveUserslNegative() {
        assertEquals(0, service.getActiveUsers(EMAIL).getUserList().size());
    }

}
