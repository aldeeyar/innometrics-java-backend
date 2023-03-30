package com.innopolis.innometrics.authserver.service;

import com.innopolis.innometrics.authserver.dto.ProfileRequest;
import com.innopolis.innometrics.authserver.entitiy.Profile;
import com.innopolis.innometrics.authserver.repository.ProfileRepository;
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
        ProfileService.class
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
class ProfileServiceTest {
    private static final String EMAIL = "email";
    private static final String ADDRESS = "address";
    private final static double TEST_DATA = 1.00;
    @Autowired
    ProfileService service;
    @Autowired
    ProfileRepository profileRepository;

    @Test
    void existsByEmailNegativeTest() {
        Boolean result = service.existsByEmail(EMAIL, ADDRESS);
        assertFalse(result);
    }

    @Test
    void existsByEmailPositiveTest() {
        profileRepository.save(createProfile());
        Boolean result = service.existsByEmail(EMAIL, ADDRESS);
        assertTrue(result);
    }

    @Test
    void createTest() {
        service.create(createProfileRequest());
        assertEquals(1, profileRepository.findAll().size());
    }

    @Test
    void findByMacAddressNegativeTest() {
        assertThrows(IllegalArgumentException.class, () -> service.findByMacAddress(ADDRESS));
    }

    @Test
    void findByMacAddressPositiveTest() {
        profileRepository.save(createProfile());
        ProfileRequest request = service.findByMacAddress(ADDRESS);
        assertEquals(ADDRESS, request.getMacAddress());
    }

    @Test
    void updateNegativeTest() {
        ProfileRequest request = createProfileRequest();
        assertThrows(IllegalArgumentException.class, () -> service.update((request)));
    }

    @Test
    void updatePositiveTest() {
        profileRepository.save(createProfile());
        ProfileRequest request = createProfileRequest();
        assertNotNull(service.update((request)));
    }

    @Test
    void deleteNegativeTest() {
        assertThrows(ValidationException.class, () -> service.delete(1));
    }

    @Test
    void delete() {
        profileRepository.save(createProfile());
        service.delete(1);
        assertEquals(0, profileRepository.findAll().size());
    }

    private Profile createProfile() {
        Profile profile = new Profile();
        profile.setProfileId(1);
        profile.setUserEmail(EMAIL);
        profile.setMacAddress(ADDRESS);
        profile.setAverageCpu(TEST_DATA);
        profile.setAverageGpu(TEST_DATA);
        profile.setAverageMemory(TEST_DATA);
        profile.setAverageStorage(TEST_DATA);
        profile.setMainVendor("vendor");
        profile.setCpuVendor("cpuVendor");
        profile.setCpuModel("cpuModel");
        profile.setGpuVendor("gpuVendor");
        profile.setGpuModel("gpuModel");
        profile.setMemoryVendor("memoryVendor");
        profile.setMemoryModel("memoryModel");
        profile.setStorageModel("storageModel");
        profile.setStorageVendor("storageVendor");
        return profile;
    }

    private ProfileRequest createProfileRequest() {
        ProfileRequest request = new ProfileRequest();
        request.setProfileId(1);
        request.setUserEmail(EMAIL);
        request.setMacAddress(ADDRESS);
        request.setAverageCpu(TEST_DATA);
        request.setAverageGpu(TEST_DATA);
        request.setAverageMemory(TEST_DATA);
        request.setAverageStorage(TEST_DATA);
        request.setMainVendor("vendor");
        request.setCpuVendor("cpuVendor");
        request.setCpuModel("cpuModel");
        request.setGpuVendor("gpuVendor");
        request.setGpuModel("gpuModel");
        request.setMemoryVendor("memoryVendor");
        request.setMemoryModel("memoryModel");
        request.setStorageModel("storageModel");
        request.setStorageVendor("storageVendor");
        return request;
    }
}
