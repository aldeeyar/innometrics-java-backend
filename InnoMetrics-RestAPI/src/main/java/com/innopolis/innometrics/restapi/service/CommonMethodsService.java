package com.innopolis.innometrics.restapi.service;

import com.innopolis.innometrics.restapi.constants.RequestConstants;
import com.innopolis.innometrics.restapi.dto.DeleteRequest;
import org.apache.logging.log4j.Logger;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CommonMethodsService {

    public boolean deleteWithIds(DeleteRequest request, String token, RestTemplate restTemplate, String baseUrl, Logger log) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(RequestConstants.TOKEN.getValue(), token);
        HttpEntity<DeleteRequest> entity = new HttpEntity<>(request, headers);
        try {
            ResponseEntity<Object> response = restTemplate.exchange(baseUrl, HttpMethod.POST, entity, Object.class);
            HttpStatus status = response.getStatusCode();
            return status == HttpStatus.OK;
        } catch (Exception e) {
            log.warn(e);
            return false;
        }
    }
}
