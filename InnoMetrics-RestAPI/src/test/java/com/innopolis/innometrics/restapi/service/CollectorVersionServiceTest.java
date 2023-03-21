package com.innopolis.innometrics.restapi.service;

import com.innopolis.innometrics.restapi.entity.CollectorVersion;
import com.innopolis.innometrics.restapi.repository.CollectorVersionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
@Import(value = {
        CollectorVersionService.class
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CollectorVersionServiceTest {
    @Autowired
    CollectorVersionService collectorVersionService;
    @Autowired
    CollectorVersionRepository versionRepository;

    @Test
    void getCurrentVersionTestForEmptyDataTest() {
        String version = collectorVersionService.getCurrentVersion("test");
        assertEquals("", version);
    }

    @Test
    void getCurrentVersionTestForExistDataTest() {
        versionRepository.save(createVersion());
        String version = collectorVersionService.getCurrentVersion("1");
        assertEquals("test", version);
    }

    @Test
    void updateCurrentVersionTest() {
        versionRepository.save(createVersion());
        boolean result = collectorVersionService.updateCurrentVersion("1", "2");
        Optional<CollectorVersion> version = versionRepository.findById(1);
        version.ifPresent(x -> assertEquals("2", x.getValue()));
        assertTrue(result);
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
