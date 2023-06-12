package com.innopolis.innometrics.restapi.controller;

import com.innopolis.innometrics.restapi.dto.CompanyListRequest;
import com.innopolis.innometrics.restapi.dto.CompanyRequest;
import com.innopolis.innometrics.restapi.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*",
        methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE})
@RequestMapping(value = "/V1/Admin/Company", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class CompanyAPI {
    private final CompanyService companyService;

    @PostMapping()
    public ResponseEntity<CompanyRequest> createCompany(@RequestBody CompanyRequest companyRequest,
                                                        @RequestHeader(required = false) String token) {
        companyRequest.setCompanyId(null);
        return new ResponseEntity<>(companyService.createCompany(companyRequest, token),
                HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyRequest> updateCompany(@PathVariable Integer id,
                                                        @RequestBody CompanyRequest companyRequest,
                                                        @RequestHeader(required = false) String token) {
        companyRequest.setCompanyId(id);
        return new ResponseEntity<>(companyService.updateCompany(companyRequest, token),
                HttpStatus.OK);
    }

    @DeleteMapping()
    public boolean deleteCompany(@RequestParam Integer id, @RequestHeader(required = false) String token) {
        return companyService.deleteCompany(id, token);
    }

    @GetMapping()
    public ResponseEntity<CompanyRequest> findByCompanyId(@RequestParam Integer id,
                                                          @RequestHeader(required = false) String token) {
        return new ResponseEntity<>(
                companyService.findByCompanyId(id, token),
                HttpStatus.OK
        );
    }

    @GetMapping("/all")
    public ResponseEntity<CompanyListRequest> findAllCompanies(@RequestHeader(required = false) String token) {
        return new ResponseEntity<>(
                companyService.getAllCompanies(token),
                HttpStatus.OK
        );
    }

    @GetMapping("/active")
    public ResponseEntity<CompanyListRequest> findAllActiveCompanies(@RequestHeader(required = false) String token) {
        return new ResponseEntity<>(
                companyService.getAllActiveCompanies(token),
                HttpStatus.OK
        );
    }
}
