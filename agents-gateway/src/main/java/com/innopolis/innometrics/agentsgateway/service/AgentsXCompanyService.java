package com.innopolis.innometrics.agentsgateway.service;

import com.innopolis.innometrics.agentsgateway.entity.AgentsXCompany;
import com.innopolis.innometrics.agentsgateway.repository.AgentsXCompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AgentsXCompanyService {
    private final AgentsXCompanyRepository agentsxcompanyRepository;

    public List<AgentsXCompany> getAgentsCompanyList() {
        return agentsxcompanyRepository.findAll();
    }

    public List<AgentsXCompany> getAgentsCompanyByAgentId(Integer agentId) {
        List<AgentsXCompany> agentsCompanyList = agentsxcompanyRepository.findAll();
        List<AgentsXCompany> result = new ArrayList<>();
        for (AgentsXCompany agentsxcompany : agentsCompanyList) {
            if (agentsxcompany.getAgentId().equals(agentId)) {
                result.add(agentsxcompany);
            }
        }
        return result;
    }

    public List<AgentsXCompany> getAgentsCompanyByCompanyId(Integer companyId) {
        List<AgentsXCompany> agentsCompanyList = agentsxcompanyRepository.findAll();
        List<AgentsXCompany> result = new ArrayList<>();
        for (AgentsXCompany agentsxcompany : agentsCompanyList) {
            if (agentsxcompany.getCompanyId().equals(companyId)) {
                result.add(agentsxcompany);
            }
        }
        return result;
    }

    public AgentsXCompany getAgentsCompanyById(Integer configId) {
        return agentsxcompanyRepository.findById(configId).orElse(null);
    }

    public AgentsXCompany postAgentsCompany(AgentsXCompany agentsxcompany) {
        return agentsxcompanyRepository.save(agentsxcompany);
    }

    public AgentsXCompany putAgentsCompany(Integer configId, AgentsXCompany agentsxcompany) {
        return agentsxcompanyRepository.findById(configId).map(agentsCompany -> {
            agentsCompany.setAgentConfig(agentsxcompany.getAgentConfig());
            agentsCompany.setAgentId(agentsxcompany.getAgentId());
            agentsCompany.setCompanyId(agentsxcompany.getCompanyId());
            agentsCompany.setKey(agentsxcompany.getKey());
            agentsCompany.setToken(agentsxcompany.getToken());
            agentsCompany.setIsActive(agentsxcompany.getIsActive());
            return agentsxcompanyRepository.save(agentsCompany);
        }).orElseGet(() -> agentsxcompanyRepository.save(agentsxcompany));
    }

    public List<AgentsXCompany> deleteAgentsCompanyByAgentId(Integer agentId) {
        return deleteAgentsCompanyList(getAgentsCompanyByAgentId(agentId));
    }

    public List<AgentsXCompany> deleteAgentsCompanyByCompanyId(Integer companyId) {
        return deleteAgentsCompanyList(getAgentsCompanyByCompanyId(companyId));
    }

    public AgentsXCompany deleteAgentsCompanyById(Integer configId) {
        Optional<AgentsXCompany> agentsCompany = agentsxcompanyRepository.findById(configId);
        if (!agentsCompany.isPresent()) {
            return null;
        }
        agentsxcompanyRepository.deleteById(configId);
        return agentsCompany.get();
    }

    private List<AgentsXCompany> deleteAgentsCompanyList(List<AgentsXCompany> agentsCompanyList) {
        if (agentsCompanyList == null || agentsCompanyList.isEmpty()) {
            return Collections.emptyList();
        }
        List<AgentsXCompany> deletedAgentsCompanyList = new ArrayList<>(agentsCompanyList.size());
        for (AgentsXCompany agentsxcompany : agentsCompanyList) {
            AgentsXCompany deletedAgentsCompany = this.deleteAgentsCompanyById(agentsxcompany.getConfigid());
            if (deletedAgentsCompany != null) {
                deletedAgentsCompanyList.add(deletedAgentsCompany);
            }
        }
        return deletedAgentsCompanyList;
    }
}
