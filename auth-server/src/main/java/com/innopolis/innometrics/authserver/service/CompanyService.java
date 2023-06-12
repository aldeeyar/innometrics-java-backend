package com.innopolis.innometrics.authserver.service;

import com.innopolis.innometrics.authserver.dto.CompanyListRequest;
import com.innopolis.innometrics.authserver.dto.CompanyRequest;
import com.innopolis.innometrics.authserver.entitiy.Company;
import com.innopolis.innometrics.authserver.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.util.List;

import static com.innopolis.innometrics.authserver.constants.ExceptionMessage.NO_COMPANY_FOUND;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;

    public boolean existsById(Integer id) {
        return companyRepository.existsByCompanyId(id);
    }

    public CompanyRequest create(CompanyRequest detail) {
        Company entity = new Company();
        BeanUtils.copyProperties(detail, entity);
        entity = companyRepository.saveAndFlush(entity);
        BeanUtils.copyProperties(entity, detail);
        return detail;
    }

    public CompanyRequest update(CompanyRequest detail) {
        Company entity = companyRepository.findByCompanyId(detail.getCompanyId());
        assertNotNull(entity, NO_COMPANY_FOUND.getValue() + detail.getCompanyId());
        detail.setCompanyId(null);
        BeanUtils.copyProperties(detail, entity, PropertyNames.getNullPropertyNames(detail));
        entity = companyRepository.saveAndFlush(entity);
        BeanUtils.copyProperties(entity, detail);
        return detail;
    }

    public void delete(Integer id) {
        Company entity = companyRepository.findById(id)
                .orElseThrow(() -> new ValidationException(NO_COMPANY_FOUND.getValue() + id));
        companyRepository.delete(entity);
    }

    public CompanyRequest findByCompanyId(Integer id) {
        Company entity = companyRepository.findByCompanyId(id);
        assertNotNull(entity, NO_COMPANY_FOUND.getValue() + id);
        CompanyRequest detail = new CompanyRequest();
        BeanUtils.copyProperties(entity, detail);
        return detail;
    }

    public CompanyListRequest findAllActiveCompanies() {
        List<Company> activeCompanies = companyRepository.findAllActive();
        return convertFromList(activeCompanies);
    }

    public CompanyListRequest findAllCompanies() {
        List<Company> allCompanies = companyRepository.findAll();
        return convertFromList(allCompanies);
    }

    private CompanyListRequest convertFromList(List<Company> companyList) {
        CompanyListRequest companyListRequest = new CompanyListRequest();
        for (Company activeCompany : companyList) {
            CompanyRequest detail = new CompanyRequest();
            BeanUtils.copyProperties(activeCompany, detail);
            companyListRequest.addCompanyRequest(detail);
        }
        return companyListRequest;
    }
}
