package com.innopolis.innometrics.restapi.service;


import com.innopolis.innometrics.restapi.constants.RequestConstants;
import com.innopolis.innometrics.restapi.dto.ProfileRequest;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private static final String BASE_URL = "http://INNOMETRICS-AUTH-SERVER/AdminAPI/User/Profile";
    private final RestTemplate restTemplate;

    @HystrixCommand(commandKey = "updateProfileOfUser", fallbackMethod = "updateProfileOfUserFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000")
    })
    public ProfileRequest updateProfileOfUser(ProfileRequest profileRequest, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(RequestConstants.TOKEN.getValue(), token);
        HttpEntity<ProfileRequest> entity = new HttpEntity<>(profileRequest, headers);
        ResponseEntity<ProfileRequest> response = restTemplate.exchange(BASE_URL, HttpMethod.POST, entity, ProfileRequest.class);
        return response.getBody();
    }

    @HystrixCommand(commandKey = "deleteProfile", fallbackMethod = "deleteProfileFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000")
    })
    public boolean deleteProfile(Integer id, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(RequestConstants.TOKEN.getValue(), token);
        HttpEntity<ProfileRequest> entity = new HttpEntity<>(headers);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam(RequestConstants.ID.getValue(), id);
        restTemplate.exchange(builder.toUriString(), HttpMethod.DELETE, entity, Object.class);
        return true;
    }

    @HystrixCommand(commandKey = "findByMacaddress", fallbackMethod = "findByMacaddressFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "600000")
    })
    public ProfileRequest findByMacaddress(String macaddress, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(RequestConstants.TOKEN.getValue(), token);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam("macaddress", macaddress);
        ResponseEntity<ProfileRequest> responseEntity = restTemplate
                .exchange(builder.toUriString(), HttpMethod.GET, entity, ProfileRequest.class);
        return responseEntity.getBody();
    }
}
