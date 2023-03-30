package com.innopolis.innometrics.authserver.service;

import com.innopolis.innometrics.authserver.dto.CompanyListRequest;
import com.innopolis.innometrics.authserver.dto.CompanyRequest;
import com.innopolis.innometrics.authserver.entitiy.Company;
import com.innopolis.innometrics.authserver.repository.CompanyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import javax.validation.ValidationException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import(value = {
        CompanyService.class
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
class CompanyServiceTest {
    private final static String TEST_DATA = "Test";
    @Autowired
    CompanyService service;
    @Autowired
    CompanyRepository companyRepository;

    @Test
    void existsByIdNegativeTest() {
        boolean result = service.existsById(1);
        assertFalse(result);
    }

    @Test
    void existsById() {
        companyRepository.save(createCompany());
        boolean result = service.existsById(1);
        assertTrue(result);
    }

    @Test
    void createTest() {
        service.create(createCompanyRequest());
        Company company = companyRepository.findByCompanyId(1);
        assertEquals(TEST_DATA, company.getCompanyName());
    }

    @Test
    void updateTestNegativeTest() {
        CompanyRequest request = createCompanyRequest();
        assertThrows(IllegalArgumentException.class, () -> service.update((request)));
    }

    @Test
    void updateTest() {
        companyRepository.save(createCompany());
        CompanyRequest request = createCompanyRequest();
        assertNotNull(service.update((request)));
    }

    @Test
    void deleteNegativeTest() {
        assertThrows(ValidationException.class, () -> service.delete(1));
    }

    @Test
    void delete() {
        companyRepository.save(createCompany());
        service.delete(1);
        assertEquals(0, companyRepository.findAll().size());
    }

    @Test
    void findByCompanyIdTest() {
        companyRepository.save(createCompany());
        CompanyRequest result = service.findByCompanyId(1);
        assertEquals(TEST_DATA, result.getCompanyName());
    }

    @Test
    void findByCompanyIdNegativeTest() {
        assertThrows(IllegalArgumentException.class, () -> service.findByCompanyId(1));
    }

    @Test
    void findAllCompaniesNegativeTest() {
        CompanyListRequest request = service.findAllCompanies();
        assertNotNull(request);
    }

    @Test
    void findAllCompaniesTest() {
        companyRepository.save(createCompany());
        CompanyListRequest request = service.findAllCompanies();
        assertNotNull(request);
    }

    @Test
    void findAllActiveCompaniesNegativeTest() {
        CompanyListRequest request = service.findAllActiveCompanies();
        assertNotNull(request);
    }

    @Test
    void findAllActiveCompaniesTest() {
        companyRepository.save(createCompany());
        CompanyListRequest request = service.findAllActiveCompanies();
        assertNotNull(request);
    }

    private Company createCompany() {
        Company company = new Company();
        company.setCompanyId(1);
        company.setCreatedBy(TEST_DATA);
        company.setCreationDate(new Date());
        company.setCompanyName(TEST_DATA);
        company.setIsActive("Y");
        return company;
    }

    private CompanyRequest createCompanyRequest() {
        CompanyRequest request = new CompanyRequest();
        request.setCompanyId(1);
        request.setCompanyName(TEST_DATA);
        request.setCreatedBy(TEST_DATA);
        request.setIsActive("Y");
        return request;
    }
}
