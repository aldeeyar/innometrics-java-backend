package com.innopolis.innometrics.agentsgateway.service;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class CustomOAuth1 extends DefaultApi10a {

    private String requestTokenEndpoint;
    private String accessTokenEndpoint;
    private String authorizationBaseUrl;

    @Override
    public String getRequestTokenEndpoint() {
        return requestTokenEndpoint;
    }

    public void setRequestTokenEndpoint(String requestTokenEndpoint) {
        this.requestTokenEndpoint = requestTokenEndpoint;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return accessTokenEndpoint;
    }

    public void setAccessTokenEndpoint(String accessTokenEndpoint) {
        this.accessTokenEndpoint = accessTokenEndpoint;
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return authorizationBaseUrl;
    }

    public void setAuthorizationBaseUrl(String authorizationBaseUrl) {
        this.authorizationBaseUrl = authorizationBaseUrl;
    }
}
