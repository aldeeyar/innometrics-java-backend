package com.innopolis.innometrics.agentsgateway.service;


public interface OAuthService {
    String getAuthorizationURL(Integer agentId, Integer projectId, String cb);

    void storeToken(Integer agentId, Integer projectId, String token, String cb);
}
