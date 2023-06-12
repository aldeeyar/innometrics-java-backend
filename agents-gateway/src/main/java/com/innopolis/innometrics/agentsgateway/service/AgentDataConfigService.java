package com.innopolis.innometrics.agentsgateway.service;

import com.innopolis.innometrics.agentsgateway.entity.AgentDataConfig;
import com.innopolis.innometrics.agentsgateway.repository.AgentDataConfigRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AgentDataConfigService {
    private final AgentDataConfigRepository agentdataconfigRepository;

    public List<AgentDataConfig> getDataList() {
        return agentdataconfigRepository.findAll();
    }

    public List<AgentDataConfig> getDataAgentsByAgentId(Integer agentId) {
        List<AgentDataConfig> dataList = agentdataconfigRepository.findAll();
        List<AgentDataConfig> result = new ArrayList<>();
        for (AgentDataConfig agentdataconfig : dataList) {
            if (agentdataconfig.getAgentId().equals(agentId)) {
                result.add(agentdataconfig);
            }
        }
        return result;
    }

    public AgentDataConfig getDataById(Integer dataId) {
        return agentdataconfigRepository.findById(dataId).orElse(null);
    }

    public AgentDataConfig postData(AgentDataConfig agentdataconfig) {
        return agentdataconfigRepository.save(agentdataconfig);
    }

    public AgentDataConfig putData(Integer dataId, AgentDataConfig data) {
        return agentdataconfigRepository.findById(dataId).map(agentData -> {
            agentData.setAgentId(data.getAgentId());
            agentData.setAgentConfig(data.getAgentConfig());
            agentData.setSchemaName(data.getSchemaName());
            agentData.setTableName(data.getTableName());
            agentData.setEventDateField(data.getEventDateField());
            agentData.setEventAuthorField(data.getEventAuthorField());
            agentData.setEventDescriptionField(data.getEventDescriptionField());
            agentData.setEventType(data.getEventType());
            agentData.setIsActive(data.getIsActive());
            return agentdataconfigRepository.save(agentData);
        }).orElseGet(() -> agentdataconfigRepository.save(data));
    }

    public List<AgentDataConfig> deleteDataByAgentId(Integer agentId) {
        List<AgentDataConfig> dataList = getDataAgentsByAgentId(agentId);
        if (dataList == null || dataList.isEmpty()) {
            return Collections.emptyList();
        }
        List<AgentDataConfig> deletedDataList = new ArrayList<>(dataList.size());
        for (AgentDataConfig data : dataList) {
            AgentDataConfig deletedData = this.deleteDataById(data.getDataConfigId());
            if (deletedData != null) {
                deletedDataList.add(deletedData);
            }
        }
        return deletedDataList;
    }

    public AgentDataConfig deleteDataById(Integer dataId) {
        Optional<AgentDataConfig> data = agentdataconfigRepository.findById(dataId);
        if (!data.isPresent()) {
            return null;
        }
        agentdataconfigRepository.deleteById(dataId);
        return data.get();
    }
}
