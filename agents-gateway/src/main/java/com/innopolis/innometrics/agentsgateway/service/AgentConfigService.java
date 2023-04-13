package com.innopolis.innometrics.agentsgateway.service;

import com.innopolis.innometrics.agentsgateway.dto.*;
import com.innopolis.innometrics.agentsgateway.entity.AgentConfig;
import com.innopolis.innometrics.agentsgateway.entity.AgentConfigMethods;
import com.innopolis.innometrics.agentsgateway.repository.AgentConfigMethodsRepository;
import com.innopolis.innometrics.agentsgateway.repository.AgentConfigRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AgentConfigService {
    private final AgentConfigRepository agentconfigRepository;
    private final AgentConfigMethodsRepository agentconfigmethodsRepository;
    private final AgentDataConfigService agentdataconfigService;
    private final AgentConfigMethodsService agentconfigmethodsService;
    private final ReposXProjectService reposxprojectService;
    private final AgentsXProjectService agentsxprojectService;
    private final ExternalProjectXTeamService externalprojectxteamService;
    private final AgentsXCompanyService agentsxcompanyService;

    public AgentConfigResponse getAgentConfig(Integer agentId, String callType) {
        List<AgentConfigMethods> result = agentconfigmethodsRepository.findByAgentId(agentId);
        if (result == null) {
            return null;
        }
        AgentConfigResponse response = new AgentConfigResponse();
        for (AgentConfigMethods agentConfigMethod : result) {
            if (agentConfigMethod.getOperation().equals(callType) || callType == null) {
                List<ParamsConfigDTO> parameters = CommonFieldsProcessor.createParameters(agentConfigMethod);
                response.addMethodConfig(
                        new MethodConfigDTO(
                                agentConfigMethod.getMethodId(),
                                agentConfigMethod.getOperation(),
                                agentConfigMethod.getDescription(),
                                parameters
                        )
                );
            }
        }
        return response;
    }

    public AgentListResponse getAgentList(Integer projectId) {
        AgentListResponse agentListResponse = new AgentListResponse();
        for (IAgentStatus agentStatus : agentconfigRepository.getAgentList(projectId)) {
            agentListResponse.add(
                    new AgentResponse(
                            Integer.valueOf(agentStatus.getAgentId()),
                            agentStatus.getAgentName(),
                            agentStatus.getDescription(),
                            agentStatus.getIsConnected()
                    )
            );
        }
        return agentListResponse;
    }

    public AgentConfig getAgentById(Integer agentId) {
        return agentconfigRepository.findByAgentId(agentId);
    }

    public AgentConfig postAgent(AgentConfig agentconfig) {
        return agentconfigRepository.save(agentconfig);
    }

    public AgentConfig putAgent(Integer agentId, AgentConfig agentconfig) {
        return agentconfigRepository.findById(agentId).map(agent -> {
            agent.setAgentName(agentconfig.getAgentName());
            agent.setDescription(agentconfig.getDescription());
            agent.setIsActive(agentconfig.getIsActive());
            agent.setSourceType(agentconfig.getSourceType());
            agent.setDbSchemaSource(agentconfig.getDbSchemaSource());
            agent.setRepoIdField(agentconfig.getRepoIdField());
            agent.setOauthUri(agentconfig.getOauthUri());
            agent.setAuthenticationMethod(agentconfig.getAuthenticationMethod());
            agent.setAccessTokenEndpoint(agentconfig.getAccessTokenEndpoint());
            agent.setAuthorizationBaseUrl(agentconfig.getAuthorizationBaseUrl());
            agent.setRequestTokenEndpoint(agentconfig.getRequestTokenEndpoint());
            agent.setApikey(agentconfig.getApikey());
            agent.setApiSecret(agentconfig.getApiSecret());
            return agentconfigRepository.save(agent);
        }).orElseGet(() -> agentconfigRepository.save(agentconfig));
    }

    public AgentConfig deleteAgentById(Integer agentId) {
        AgentConfig agentconfig = agentconfigRepository.findByAgentId(agentId);
        if (agentconfig == null) {
            return null;
        }
        agentconfigmethodsService.deleteMethodsByAgentId(agentId);
        agentdataconfigService.deleteDataByAgentId(agentId);
        reposxprojectService.deleteReposProjectByAgentId(agentId);
        agentsxprojectService.deleteAgentsProjectByAgentId(agentId);
        externalprojectxteamService.deleteExternalProjectTeamByAgentId(agentId);
        agentsxcompanyService.deleteAgentsCompanyByAgentId(agentId);
        agentconfigRepository.deleteById(agentId);
        return agentconfig;
    }
}
