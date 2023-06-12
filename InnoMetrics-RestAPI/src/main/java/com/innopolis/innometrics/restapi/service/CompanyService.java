package com.innopolis.innometrics.restapi.service;

import com.innopolis.innometrics.restapi.constants.RequestConstants;
import com.innopolis.innometrics.restapi.dto.CompanyListRequest;
import com.innopolis.innometrics.restapi.dto.CompanyRequest;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private static final Logger LOG = LogManager.getLogger();
    private static final String BASE_URL = "http://INNOMETRICS-AUTH-SERVER/AdminAPI/Company";
    private final RestTemplate restTemplate;

    @HystrixCommand(commandKey = "updateCompany", fallbackMethod = "updateCompanyFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000")
    })
    public CompanyRequest updateCompany(CompanyRequest companyRequest, String token) {
        String uri = BASE_URL + "/" + companyRequest.getCompanyId();
        return uploadCompany(companyRequest, token, uri, HttpMethod.PUT);
    }

    public CompanyRequest createCompany(CompanyRequest companyRequest, String token) {
        return uploadCompany(companyRequest, token, BASE_URL, HttpMethod.POST);
    }

    private CompanyRequest uploadCompany(CompanyRequest companyRequest, String token, String uri, HttpMethod method) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(RequestConstants.TOKEN.getValue(), token);
        HttpEntity<CompanyRequest> entity = new HttpEntity<>(companyRequest, headers);
        ResponseEntity<CompanyRequest> response = restTemplate.exchange(uri, method, entity, CompanyRequest.class);
        HttpStatus status = response.getStatusCode();
        if (status == HttpStatus.NO_CONTENT) return null;
        return response.getBody();
    }
    public boolean deleteCompany(Integer id, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(RequestConstants.TOKEN.getValue(), token);
        HttpEntity<CompanyRequest> entity = new HttpEntity<>(headers);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam(RequestConstants.ID.getValue(), id);
        try {
            restTemplate.exchange(builder.toUriString(), HttpMethod.DELETE, entity, Object.class);
            return true;
        } catch (Exception e) {
            LOG.error(e);
            return false;
        }
    }

    @HystrixCommand(commandKey = "findByCompanyId", fallbackMethod = "findByCompanyIdFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000")
    })
    public CompanyRequest findByCompanyId(Integer id, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(RequestConstants.TOKEN.getValue(), token);
        HttpEntity<CompanyRequest> entity = new HttpEntity<>(headers);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam(RequestConstants.ID.getValue(), id);
        ResponseEntity<CompanyRequest> responseEntity = restTemplate
                .exchange(builder.toUriString(), HttpMethod.GET, entity, CompanyRequest.class);
        return responseEntity.getBody();
    }

    @HystrixCommand(commandKey = "getActiveCompanies", fallbackMethod = "getActiveCompaniesFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000")
    })
    public CompanyListRequest getAllActiveCompanies(String token) {
        String uri = BASE_URL + "/active";
        return getCompanies(token, uri);
    }

    @HystrixCommand(commandKey = "getAllCompanies", fallbackMethod = "getAllCompaniesFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000")
    })
    public CompanyListRequest getAllCompanies(String token) {
        String uri = BASE_URL + "/all";
        return getCompanies(token, uri);
    }

    private CompanyListRequest getCompanies(String token, String uri) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(RequestConstants.TOKEN.getValue(), token);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uri);
        ResponseEntity<CompanyListRequest> response = restTemplate
                .exchange(builder.toUriString(), HttpMethod.GET, entity, CompanyListRequest.class);
        return response.getBody();
    }
}
