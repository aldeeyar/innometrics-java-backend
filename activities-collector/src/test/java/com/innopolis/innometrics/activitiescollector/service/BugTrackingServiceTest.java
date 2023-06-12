package com.innopolis.innometrics.activitiescollector.service;

import com.innopolis.innometrics.activitiescollector.dto.BugTrackingListRequest;
import com.innopolis.innometrics.activitiescollector.dto.BugTrackingRequest;
import com.innopolis.innometrics.activitiescollector.entity.BugTracking;
import com.innopolis.innometrics.activitiescollector.repository.BugTrackingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Timestamp;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import(value = {
        BugTrackingService.class
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
class BugTrackingServiceTest {
    private final static String TEST_DATA = "Test";
    @Autowired
    BugTrackingService service;
    @Autowired
    BugTrackingRepository bugTrackingRepository;

    @Test
    void existsByIdNegativeTest() {
        boolean result = service.existsById(1);
        assertFalse(result);
    }

    @Test
    void existsById() {
        bugTrackingRepository.save(createBugTracking());
        boolean result = service.existsById(1);
        assertTrue(result);
    }

    @Test
    void createTest() {
        service.create(createBugTrackingRequest());
        Optional<BugTracking> bugTracking = bugTrackingRepository.findById(1);
        bugTracking.ifPresent(tracking -> assertEquals(TEST_DATA, tracking.getOs()));
    }

    @Test
    void findByIDTest() {
        bugTrackingRepository.save(createBugTracking());
        BugTrackingRequest result = service.findByID(1);
        assertEquals(TEST_DATA, result.getOs());
    }

    @Test
    void findByIDNegativeTest() {
        assertThrows(IllegalArgumentException.class, () -> service.findByID(1));
    }

    @Test
    void updateTestNegativeTest() {
        BugTrackingRequest request = createBugTrackingRequest();
        assertThrows(IllegalArgumentException.class, () -> service.update((request)));
    }

    @Test
    void updateTest() {
        bugTrackingRepository.save(createBugTracking());
        BugTrackingRequest request = createBugTrackingRequest();
        assertNotNull(service.update((request)));
    }

    @Test
    void deleteNegativeTest() {
        assertThrows(IllegalArgumentException.class, () -> service.delete(1));
    }

    @Test
    void delete() {
        bugTrackingRepository.save(createBugTracking());
        service.delete(1);
        assertEquals(0, bugTrackingRepository.findAll().size());
    }

    @Test
    void findBugsByCreationDate() {
        bugTrackingRepository.save(createBugTracking());
        BugTrackingListRequest request = service.findBugsByCreationDate(new Timestamp(1), new Timestamp(3));
        assertNotNull(request);
    }

    @Test
    void findBugsByCreationDateWithStatus() {
        bugTrackingRepository.save(createBugTracking());
        BugTrackingListRequest request = service.findBugsByCreationDateWithStatus(new Timestamp(1), new Timestamp(3), true);
        assertNotNull(request);
    }

    private BugTrackingRequest createBugTrackingRequest() {
        BugTrackingRequest request = new BugTrackingRequest();
        request.setBugId(1);
        request.setOs(TEST_DATA);
        request.setTitle(TEST_DATA);
        request.setLine(1);
        return request;
    }

    private BugTracking createBugTracking() {
        BugTracking bugTracking = new BugTracking();
        bugTracking.setBugId(1);
        bugTracking.setOs(TEST_DATA);
        bugTracking.setTitle(TEST_DATA);
        bugTracking.setLine(1);
        bugTracking.setCreationDate(new Timestamp(2));
        bugTracking.setStatus(true);
        return bugTracking;
    }
}
