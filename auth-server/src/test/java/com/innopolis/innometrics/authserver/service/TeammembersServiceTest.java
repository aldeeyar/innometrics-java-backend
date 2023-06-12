package com.innopolis.innometrics.authserver.service;

import com.innopolis.innometrics.authserver.dto.TeammembersListRequest;
import com.innopolis.innometrics.authserver.dto.TeammembersRequest;
import com.innopolis.innometrics.authserver.entitiy.Teammembers;
import com.innopolis.innometrics.authserver.repository.TeammembersRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import javax.validation.ValidationException;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import(value = {
        TeammembersService.class
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
class TeammembersServiceTest {
    private static final String EMAIL = "email";
    private final static String TEST_DATA = "Test";

    @Autowired
    TeammembersService service;
    @Autowired
    TeammembersRepository teammembersRepository;

    @Test
    void existsByIDNegative() {
        boolean result = service.existsById(1);
        assertFalse(result);
    }

    @Test
    void existsByIDPositive() {
        teammembersRepository.save(createTeammembers());
        boolean result = service.existsById(1);
        assertTrue(result);
    }

    @Test
    void existInTheTeamNegative() {
        assertFalse(service.existInTheTeam(1, EMAIL));
    }

    @Test
    void existInTheTeamPositive() {
        teammembersRepository.save(createTeammembers());
        assertTrue(service.existInTheTeam(1, EMAIL));
    }

    @Test
    void deleteNegative() {
        assertThrows(ValidationException.class, () -> service.delete(1));
    }

    @Test
    void deletePositive() {
        teammembersRepository.save(createTeammembers());
        service.delete(1);
        assertEquals(0, teammembersRepository.findAll().size());
    }

    @Test
    void createTest() {
        service.create(createTeammembersRequest());
        assertEquals(1, teammembersRepository.findAll().size());
    }

    @Test
    void updateNegative() {
        TeammembersRequest request = createTeammembersRequest();
        assertThrows(IllegalArgumentException.class, () -> service.update((request)));
    }

    @Test
    void updatePositive() {
        teammembersRepository.save(createTeammembers());
        TeammembersRequest request = createTeammembersRequest();
        assertNotNull(service.update((request)));
    }

    @Test
    void findAllActiveTeammembers() {
        TeammembersListRequest request = service.findAllActiveTeammembers();
        assertNotNull(request);
    }

    @Test
    void findAllTeammembers() {
        TeammembersListRequest request = service.findAllTeammembers();
        assertNotNull(request);
    }

    @Test
    void findByTeammemberPropertiesNegative() {
        assertThrows(IllegalArgumentException.class,
                () -> service.findByTeammemberProperties(1, 1, EMAIL));
    }

    @Test
    void findByTeammemberPropertiesPositive() {
        teammembersRepository.save(createTeammembers());
        TeammembersListRequest teammembersListRequest = service.findByTeammemberProperties(1, 1, EMAIL);
        assertNotNull(teammembersListRequest);
    }

    private Teammembers createTeammembers() {
        Teammembers teammembers = new Teammembers();
        teammembers.setEmail(EMAIL);
        teammembers.setCreatedBy(TEST_DATA);
        teammembers.setTeamId(1);
        teammembers.setMemberId(1);
        teammembers.setIsActive("Y");
        return teammembers;
    }

    private TeammembersRequest createTeammembersRequest() {
        TeammembersRequest request = new TeammembersRequest();
        request.setEmail(EMAIL);
        request.setTeamId(1);
        request.setCreatedBy(TEST_DATA);
        request.setIsActive("Y");
        request.setMemberId(1);
        return request;
    }
}
