package com.innopolis.innometrics.agentsgateway.service;

import com.innopolis.innometrics.agentsgateway.dto.MetricsResponse;
import com.innopolis.innometrics.agentsgateway.dto.ProjectDTO;
import com.innopolis.innometrics.agentsgateway.dto.ProjectListResponse;
import com.innopolis.innometrics.agentsgateway.entity.AgentConfig;
import com.innopolis.innometrics.agentsgateway.entity.AgentConfigMethods;
import com.innopolis.innometrics.agentsgateway.entity.sonarqube.Project;
import com.innopolis.innometrics.agentsgateway.repository.AgentConfigMethodsRepository;
import com.innopolis.innometrics.agentsgateway.repository.AgentConfigRepository;
import com.innopolis.innometrics.agentsgateway.repository.sonarqube.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static com.innopolis.innometrics.agentsgateway.constants.HeaderConstants.*;

@Service
@RequiredArgsConstructor
public class SonarQubeService {
    private static final String METRICS_HISTORY = "MetricsHistory";
    private static final String SONAR_AGENT_ID = "Sonarqube";
    private final ProjectRepository projectRepository;
    private final AgentConfigMethodsRepository agentconfigmethodsRepository;
    private final AgentConfigRepository agentconfigRepository;
    private final RestTemplate restTemplate;


    public ProjectListResponse getProjectList() {
        List<Project> projectList = projectRepository.findAll();
        ProjectListResponse response = new ProjectListResponse();
        for (Project p : projectList) {
            ProjectDTO tmp = new ProjectDTO();
            tmp.setProjectId(p.getId().toString());
            tmp.setProjectName(p.getProjectName());
            tmp.setReference(p.getProjectId());
            response.getProjectList().add(tmp);
        }
        return response;
    }


    public MetricsResponse getMetrics() {
        MetricsResponse response = new MetricsResponse();
        response.getMetricList().put("security_hotspots", SECURITY.getValue());
        response.getMetricList().put("security_rating", SECURITY.getValue());
        response.getMetricList().put("new_security_hotspots", SECURITY.getValue());
        response.getMetricList().put("new_security_hotspots_reviewed", SECURITY.getValue());
        response.getMetricList().put("new_vulnerabilities", SECURITY.getValue());
        response.getMetricList().put("classes", SIZE_COMPLEXITY.getValue());
        response.getMetricList().put("lines", SIZE_COMPLEXITY.getValue());
        response.getMetricList().put("ncloc", SIZE_COMPLEXITY.getValue());
        response.getMetricList().put("complexity_in_classes", SIZE_COMPLEXITY.getValue());
        response.getMetricList().put("branch_coverage", COVERAGE.getValue());
        response.getMetricList().put("coverage", COVERAGE.getValue());
        response.getMetricList().put("line_coverage", COVERAGE.getValue());
        response.getMetricList().put("bugs", RELIABILITY.getValue());
        response.getMetricList().put("alert_status", RELIABILITY.getValue());
        response.getMetricList().put("reliability_rating", RELIABILITY.getValue());
        response.getMetricList().put("new_bugs", RELIABILITY.getValue());
        response.getMetricList().put("blocker_violations", ISSUE.getValue());
        response.getMetricList().put("violations", ISSUE.getValue());
        response.getMetricList().put("major_violations", ISSUE.getValue());
        response.getMetricList().put("minor_violations", ISSUE.getValue());
        response.getMetricList().put("reopened_issues", ISSUE.getValue());
        response.getMetricList().put("new_blocker_violations", ISSUE.getValue());
        response.getMetricList().put("new_critical_violations", ISSUE.getValue());
        response.getMetricList().put("new_major_violations", ISSUE.getValue());
        response.getMetricList().put("new_minor_violations", ISSUE.getValue());
        response.getMetricList().put("new_technical_debt", MAINTAINABILITY.getValue());
        response.getMetricList().put("code_smells", MAINTAINABILITY.getValue());
        response.getMetricList().put("sqale_rating", MAINTAINABILITY.getValue());
        response.getMetricList().put("new_code_smells", MAINTAINABILITY.getValue());
        return response;

    }

    public Object getHistoryOfMetrics(String sonarProjectId) {
        AgentConfig agent = agentconfigRepository.findByAgentName(SONAR_AGENT_ID);
        AgentConfigMethods agentConfig = agentconfigmethodsRepository
                .findByAgentIdAndOperation(agent.getAgentId(), METRICS_HISTORY);
        String uri = agentConfig.getEndpoint();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uri);
        builder.queryParam("projectId", sonarProjectId);
        HttpMethod requestMethod = CommonFieldsProcessor.getRequestType(agentConfig.getRequestType().toUpperCase());
        assert requestMethod != null;
        ResponseEntity<Object> response = restTemplate
                .exchange(builder.toUriString(), requestMethod, null, Object.class);
        return response.getBody();
    }
}
