package com.innopolis.innometrics.agentsgateway.service;

import org.springframework.stereotype.Service;


public interface OAuthService {
    String getAuthorizationURL(Integer agentId, Integer projectId);
    String storeToken(Integer agentId, Integer projectId, String token);
}
