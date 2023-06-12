package com.innopolis.innometrics.authserver.controller;

import com.innopolis.innometrics.authserver.config.JwtToken;
import com.innopolis.innometrics.authserver.constants.ExceptionMessage;
import com.innopolis.innometrics.authserver.constants.RequestConstants;
import com.innopolis.innometrics.authserver.dto.CompanyListRequest;
import com.innopolis.innometrics.authserver.dto.CompanyRequest;
import com.innopolis.innometrics.authserver.exceptions.ValidationException;
import com.innopolis.innometrics.authserver.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("AdminAPI")
@RequiredArgsConstructor
public class CompanyAPI {
    private final CompanyService companyService;
    private final JwtToken jwtToken;

    @PostMapping("/Company")
    public ResponseEntity<CompanyRequest> createCompany(@RequestBody CompanyRequest companyRequest,
                                                        @RequestHeader(required = false) String token) {
        String userName = RequestConstants.API.getValue();
        if (StringUtils.isNotEmpty(token))
            userName = jwtToken.getUsernameFromToken(token);
        if (companyRequest == null)
            throw new ValidationException(ExceptionMessage.NOT_ENOUGH_DATA.getValue());
        companyRequest.setCompanyId(null);
        companyRequest.setUpdateBy(userName);
        companyRequest.setCreatedBy(userName);
        CompanyRequest response = companyService.create(companyRequest);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/Company/{id}")
    public ResponseEntity<CompanyRequest> updateCompany(@PathVariable Integer id,
                                                        @RequestBody CompanyRequest companyRequest,
                                                        @RequestHeader(required = false) String token) {
        String userName = RequestConstants.API.getValue();
        if (StringUtils.isNotEmpty(token))
            userName = jwtToken.getUsernameFromToken(token);
        if (companyRequest == null)
            throw new ValidationException(ExceptionMessage.NOT_ENOUGH_DATA.getValue());
        companyRequest.setCompanyId(id);
        companyRequest.setUpdateBy(userName);
        CompanyRequest response;
        if (!companyService.existsById(companyRequest.getCompanyId())) {
            companyRequest.setCreatedBy(userName);
            response = companyService.create(companyRequest);
        } else {
            response = companyService.update(companyRequest);
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/Company")
    public ResponseEntity<CompanyRequest> deleteCompany(@RequestParam Integer id,
                                                        @RequestHeader(required = false) String token) {
        if (id == null)
            throw new ValidationException(ExceptionMessage.NOT_ENOUGH_DATA.getValue());
        try {
            companyService.delete(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (Exception e) {
            throw new ValidationException("Entries from 'agents_x_company' table with such companyid must be deleted firstly");
        }
    }

    @GetMapping("/Company")
    public ResponseEntity<CompanyRequest> findByCompanyId(@RequestParam Integer id,
                                                          @RequestHeader(required = false) String token) {
        if (id == null)
            throw new ValidationException(ExceptionMessage.NOT_ENOUGH_DATA.getValue());
        return ResponseEntity.ok(
                companyService.findByCompanyId(id)
        );
    }

    @GetMapping("/Company/active")
    public ResponseEntity<CompanyListRequest> findAllActiveCompanies(@RequestHeader(required = false) String token) {
        return ResponseEntity.ok(
                companyService.findAllActiveCompanies()
        );
    }

    @GetMapping("/Company/all")
    public ResponseEntity<CompanyListRequest> findAllCompanies(@RequestHeader(required = false) String token) {
        return ResponseEntity.ok(
                companyService.findAllCompanies()
        );
    }
}
