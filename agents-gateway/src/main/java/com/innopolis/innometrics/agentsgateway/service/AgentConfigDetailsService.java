package com.innopolis.innometrics.agentsgateway.service;

import com.innopolis.innometrics.agentsgateway.entity.AgentConfigDetails;
import com.innopolis.innometrics.agentsgateway.repository.AgentConfigDetailsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AgentConfigDetailsService {
    private final AgentConfigDetailsRepository agentConfigDetailsRepository;

    public List<AgentConfigDetails> getDetailsList() {
        return agentConfigDetailsRepository.findAll();
    }

    public List<AgentConfigDetails> getDetailsByMethodId(Integer methodId) {
        List<AgentConfigDetails> detailsList = agentConfigDetailsRepository.findAll();
        List<AgentConfigDetails> result = new ArrayList<>();
        for (AgentConfigDetails agentconfigdetails : detailsList) {
            if (agentconfigdetails.getAgentconfigmethods().getMethodId().equals(methodId)) {
                result.add(agentconfigdetails);
            }
        }
        return result;
    }

    public AgentConfigDetails getDetailsById(Integer detailsId) {
        return agentConfigDetailsRepository.findById(detailsId).orElse(null);
    }

    public AgentConfigDetails postDetails(AgentConfigDetails agentconfigdetails) {
        return agentConfigDetailsRepository.save(agentconfigdetails);
    }

    public AgentConfigDetails putDetails(Integer detailsId, AgentConfigDetails details) {
        return agentConfigDetailsRepository.findById(detailsId).map(agentDetails -> {
            agentDetails.setAgentconfigmethods(details.getAgentconfigmethods());
            agentDetails.setParamName(details.getParamName());
            agentDetails.setParamType(details.getParamType());
            agentDetails.setRequestParam(details.getRequestParam());
            agentDetails.setRequestType(details.getRequestType());
            agentDetails.setIsActive(details.getIsActive());
            agentDetails.setDefaultValue(details.getDefaultValue());
            return agentConfigDetailsRepository.save(agentDetails);
        }).orElseGet(() -> agentConfigDetailsRepository.save(details));
    }

    public List<AgentConfigDetails> deleteDetailsByMethodId(Integer methodId) {
        List<AgentConfigDetails> detailsList = this.getDetailsByMethodId(methodId);
        if (detailsList == null || detailsList.isEmpty()) {
            return Collections.emptyList();
        }
        List<AgentConfigDetails> deletedDetailsList = new ArrayList<>(detailsList.size());
        for (AgentConfigDetails details : detailsList) {
            AgentConfigDetails deletedDetails = this.deleteDetailsById(details.getConfigDetId());
            if (deletedDetails != null) {
                deletedDetailsList.add(deletedDetails);
            }
        }
        return deletedDetailsList;
    }

    public AgentConfigDetails deleteDetailsById(Integer detailsId) {
        Optional<AgentConfigDetails> details = agentConfigDetailsRepository.findById(detailsId);
        if (!details.isPresent()) {
            return null;
        }
        agentConfigDetailsRepository.deleteById(detailsId);
        return details.get();
    }
}
