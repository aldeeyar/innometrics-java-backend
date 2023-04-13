package com.innopolis.innometrics.agentsgateway.service;

import com.innopolis.innometrics.agentsgateway.entity.AgentConfigMethods;
import com.innopolis.innometrics.agentsgateway.repository.AgentConfigMethodsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class AgentConfigMethodsService {
    private final AgentConfigMethodsRepository agentconfigmethodsRepository;
    private final AgentConfigDetailsService agentconfigdetailsService;
    private final AgentResponseConfigService agentresponseconfigService;

    public List<AgentConfigMethods> getMethodsList() {
        return agentconfigmethodsRepository.findAll();
    }

    public List<AgentConfigMethods> getMethodsByAgentId(Integer agentId) {
        return agentconfigmethodsRepository.findByAgentId(agentId);
    }

    public AgentConfigMethods getMethodById(Integer methodId) {
        return agentconfigmethodsRepository.findById(methodId).orElse(null);
    }

    public AgentConfigMethods getMethodsByAgentidAndOperation(Integer agentId, String operation) {
        return agentconfigmethodsRepository.findByAgentIdAndOperation(agentId, operation);
    }

    public AgentConfigMethods postMethod(AgentConfigMethods agentconfigmethods) {
        agentconfigmethods.setParams(new HashSet<>());
        return agentconfigmethodsRepository.save(agentconfigmethods);
    }

    public AgentConfigMethods putMethod(Integer methodId, AgentConfigMethods method) {
        return agentconfigmethodsRepository.findById(methodId).map(agentMethod -> {
            agentMethod.setAgentId(method.getAgentId());
            agentMethod.setAgentConfig(method.getAgentConfig());
            agentMethod.setDescription(method.getDescription());
            agentMethod.setEndpoint(method.getEndpoint());
            agentMethod.setIsActive(method.getIsActive());
            agentMethod.setOperation(method.getOperation());
            agentMethod.setRequestType(method.getRequestType());
            return agentconfigmethodsRepository.save(agentMethod);
        }).orElseGet(() -> agentconfigmethodsRepository.save(method));
    }

    public List<AgentConfigMethods> deleteMethodsByAgentId(Integer agentId) {
        List<AgentConfigMethods> methods = agentconfigmethodsRepository.findByAgentId(agentId);
        if (methods == null || methods.isEmpty()) {
            return Collections.emptyList();
        }
        List<AgentConfigMethods> deletedMethods = new ArrayList<>(methods.size());
        for (AgentConfigMethods method : methods) {
            AgentConfigMethods deletedMethod = this.deleteMethodById(method.getMethodId());
            if (deletedMethod != null) {
                deletedMethods.add(deletedMethod);
            }
        }
        return deletedMethods;
    }

    public AgentConfigMethods deleteMethodById(Integer methodId) {
        Optional<AgentConfigMethods> method = agentconfigmethodsRepository.findById(methodId);
        if (!method.isPresent()) {
            return null;
        }
        agentconfigdetailsService.deleteDetailsByMethodId(methodId);
        agentresponseconfigService.deleteResponseByMethodId(methodId);
        this.agentconfigmethodsRepository.deleteById(methodId);
        return method.get();
    }
}
