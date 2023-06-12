package com.innopolis.innometrics.agentsgateway.service;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.oauth.OAuth10aService;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.innopolis.innometrics.agentsgateway.entity.AgentConfig;
import com.innopolis.innometrics.agentsgateway.entity.AgentsXProject;
import com.innopolis.innometrics.agentsgateway.repository.AgentConfigRepository;
import com.innopolis.innometrics.agentsgateway.repository.AgentsXProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static com.innopolis.innometrics.agentsgateway.constants.HeaderConstants.INNO_METRICS;

@Service
@RequiredArgsConstructor
public class OAuthImp implements OAuthService {

    private static final String OAUTH_1A = "OAuth1a";
    private static final String OAUTH_20 = "OAuth20";
    private static final String OAUTH_20J = "OAuth20J";
    private static final String SIMPLE = "Simple";
    private static final String AGENT = "?agentid=";
    private static final String PROJECT = "&projectid=";
    private static final String CB = "?cb=";
    private final AgentConfigRepository agentconfigRepository;
    private final AgentsXProjectRepository agentsxprojectRepository;
    @Value("${innometrics.oauth1a.default-callback}")
    private String defaultCallback1A;
    @Value("${innometrics.oauth20.default-callback}")
    private String defaultCallback20;
    @Value("${innometrics.oauthSimple.default-callback}")
    private String defaultCallbacksimple;

    @Override
    public String getAuthorizationURL(Integer agentId, Integer projectId, String cb) {
        AgentConfig config = agentconfigRepository.findByAgentId(agentId);
        switch (config.getAuthenticationMethod()) {
            case OAUTH_1A:
                return getAuthorizationURL1a(config, projectId, cb);
            case OAUTH_20:
            case OAUTH_20J:
                return getAuthorizationURL20(config, projectId, cb);
            case SIMPLE:
                return getAuthorizationURLSimple(config, projectId, cb);
            default:
        }
        return null;
    }

    @Override
    public void storeToken(Integer agentId, Integer projectId, String oauthVerifier, String cb) {
        AgentConfig config = agentconfigRepository.findByAgentId(agentId);
        String token = "";
        switch (config.getAuthenticationMethod()) {
            case OAUTH_1A:
                token = getToken1a(config, projectId, oauthVerifier);
                break;
            case OAUTH_20:
                token = getToken20(config, projectId, oauthVerifier, cb);
                break;
            case OAUTH_20J:
                token = getToken20j(config, projectId, oauthVerifier, cb);
                break;
            case SIMPLE:
                token = oauthVerifier;
                break;
            default:
        }
        AgentsXProject newAgent = new AgentsXProject();
        newAgent.setAgentId(config.getAgentId());
        newAgent.setKey(config.getApikey());
        newAgent.setToken(token);
        newAgent.setProjectId(projectId);
        newAgent.setIsActive("Y");
        agentsxprojectRepository.save(newAgent);
    }

    private String getAuthorizationURL1a(AgentConfig config, Integer projectId, String cb) {
        CustomOAuth1 client = new CustomOAuth1();
        client.setAccessTokenEndpoint(config.getAccessTokenEndpoint());
        client.setAuthorizationBaseUrl(config.getAuthorizationBaseUrl());
        client.setRequestTokenEndpoint(config.getRequestTokenEndpoint());
        try {
            OAuth10aService service = new ServiceBuilder(config.getApikey()).debug()
                    .apiSecret(config.getApiSecret())
                    .callback(defaultCallback1A + config.getAgentId() + "/" + projectId + CB + cb)
                    .userAgent(INNO_METRICS.getValue())
                    .build(client);
            OAuth1RequestToken requestToken = service.getRequestToken();
            return service.getAuthorizationUrl(requestToken);
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getAuthorizationURL20(AgentConfig config, Integer projectId, String cb) {
        CustomOAuth2 client = new CustomOAuth2();
        client.setAccessTokenEndpoint(config.getAccessTokenEndpoint());
        client.setAuthorizationBaseUrl(config.getAuthorizationBaseUrl());
        try {
            OAuth20Service service = new ServiceBuilder(config.getApikey()).debug()
                    .apiSecret(config.getApiSecret())
                    .callback(defaultCallback20 + AGENT + config.getAgentId() + PROJECT + projectId + CB + cb)
                    .userAgent(INNO_METRICS.getValue())
                    .build(client);
            return service.getAuthorizationUrl();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private String getAuthorizationURLSimple(AgentConfig config, Integer projectid, String cb) {
        try {
            String callback = defaultCallbacksimple + AGENT + config.getAgentId() + PROJECT + projectid + CB + cb;
            return config.getAuthorizationBaseUrl() + "?callback=" + callback;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getToken1a(AgentConfig config, Integer projectId, String oauthVerifier) {
        CustomOAuth1 client = new CustomOAuth1();
        client.setAccessTokenEndpoint(config.getAccessTokenEndpoint());
        client.setAuthorizationBaseUrl(config.getAuthorizationBaseUrl());
        client.setRequestTokenEndpoint(config.getRequestTokenEndpoint());
        try {
            OAuth10aService service = new ServiceBuilder(config.getApikey()).debug()
                    .apiSecret(config.getApiSecret())
                    .callback(defaultCallback1A + config.getAgentId() + "/" + projectId)
                    .userAgent(INNO_METRICS.getValue())
                    .build(client);
            OAuth1RequestToken requestToken = service.getRequestToken();
            OAuth1AccessToken accessToken = service.getAccessToken(requestToken, oauthVerifier);
            return accessToken.getToken();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getToken20(AgentConfig config, Integer projectId, String oauthVerifier, String cb) {
        CustomOAuth2 client = new CustomOAuth2();
        client.setAccessTokenEndpoint(config.getAccessTokenEndpoint());
        client.setAuthorizationBaseUrl(config.getAuthorizationBaseUrl());
        return process(config, oauthVerifier, projectId, client, cb);
    }

    private String getToken20j(AgentConfig config, Integer projectId, String oauthVerifier, String cb) {
        CustomOAuth2J client = new CustomOAuth2J();
        client.setAccessTokenEndpoint(config.getAccessTokenEndpoint());
        client.setAuthorizationBaseUrl(config.getAuthorizationBaseUrl());
        return process(config, oauthVerifier, projectId, client, cb);
    }

    private String process(AgentConfig config, String oauthVerifier, Integer projectId, DefaultApi20 client, String cb) {
        try {
            OAuth20Service service = new ServiceBuilder(config.getApikey()).debug()
                    .apiSecret(config.getApiSecret())
                    .defaultScope("api")
                    .callback(defaultCallback20 + AGENT + config.getAgentId() + PROJECT + projectId + CB + cb)
                    .userAgent(INNO_METRICS.getValue())
                    .build(client);
            OAuth2AccessToken token = service.getAccessToken(oauthVerifier);
            return token.getAccessToken();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
