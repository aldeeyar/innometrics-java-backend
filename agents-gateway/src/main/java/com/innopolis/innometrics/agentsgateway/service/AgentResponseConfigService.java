package com.innopolis.innometrics.agentsgateway.service;

import com.innopolis.innometrics.agentsgateway.entity.AgentResponseConfig;
import com.innopolis.innometrics.agentsgateway.repository.AgentResponseConfigRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AgentResponseConfigService {
    private final AgentResponseConfigRepository agentresponseconfigRepository;

    public List<AgentResponseConfig> getResponsesList() {
        return agentresponseconfigRepository.findAll();
    }

    public List<AgentResponseConfig> getResponsesByMethodId(Integer methodId) {
        List<AgentResponseConfig> responsesList = agentresponseconfigRepository.findAll();
        List<AgentResponseConfig> result = new ArrayList<>();
        for (AgentResponseConfig agentresponseconfig : responsesList) {
            if (agentresponseconfig.getAgentconfigmethods() != null &&
                    agentresponseconfig.getAgentconfigmethods().getMethodId().equals(methodId)) {
                result.add(agentresponseconfig);
            }
        }
        return result;
    }

    public AgentResponseConfig getResponseById(Integer responseId) {
        return agentresponseconfigRepository.findById(responseId).orElse(null);
    }

    public AgentResponseConfig postResponse(AgentResponseConfig agentresponseconfig) {
        return agentresponseconfigRepository.save(agentresponseconfig);
    }

    public AgentResponseConfig putResponse(Integer responseId, AgentResponseConfig response) {
        return agentresponseconfigRepository.findById(responseId).map(agentDetails -> {
            agentDetails.setAgentconfigmethods(response.getAgentconfigmethods());
            agentDetails.setResponseParam(response.getResponseParam());
            agentDetails.setParamName(response.getParamName());
            agentDetails.setParamType(response.getParamType());
            agentDetails.setIsActive(response.getIsActive());
            return agentresponseconfigRepository.save(agentDetails);
        }).orElseGet(() -> agentresponseconfigRepository.save(response));
    }

    public List<AgentResponseConfig> deleteResponseByMethodId(Integer methodId) {
        List<AgentResponseConfig> responsesList = this.getResponsesByMethodId(methodId);
        if (responsesList == null || responsesList.isEmpty()) {
            return Collections.emptyList();
        }
        List<AgentResponseConfig> deletedResponsesList = new ArrayList<>(responsesList.size());
        for (AgentResponseConfig response : responsesList) {
            AgentResponseConfig deletedResponse = this.deleteResponseById(response.getConfigResponseId());
            if (deletedResponse != null) {
                deletedResponsesList.add(deletedResponse);
            }
        }
        return deletedResponsesList;
    }

    public AgentResponseConfig deleteResponseById(Integer responseId) {
        Optional<AgentResponseConfig> response = this.agentresponseconfigRepository.findById(responseId);
        if (!response.isPresent()) {
            return null;
        }
        this.agentresponseconfigRepository.deleteById(responseId);
        return response.get();
    }
}
