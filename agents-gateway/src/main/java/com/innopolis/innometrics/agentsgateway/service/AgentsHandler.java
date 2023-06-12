package com.innopolis.innometrics.agentsgateway.service;

import com.innopolis.innometrics.agentsgateway.dto.*;
import com.innopolis.innometrics.agentsgateway.entity.*;
import com.innopolis.innometrics.agentsgateway.repository.AgentConfigMethodsRepository;
import com.innopolis.innometrics.agentsgateway.repository.AgentConfigRepository;
import com.innopolis.innometrics.agentsgateway.repository.AgentsXProjectRepository;
import com.innopolis.innometrics.agentsgateway.repository.ReposXProjectRepository;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;

@Service
@AllArgsConstructor
public class AgentsHandler {
    private static final Logger LOG = LogManager.getLogger();
    private static final String GET_REPOS_LIST = "ProjectList";
    private static final String CONNECT_REPO = "ProjectConnect";
    private final AgentConfigRepository agentconfigRepository;
    private final AgentConfigMethodsRepository agentconfigmethodsRepository;
    private final ReposXProjectRepository reposxprojectRepository;
    private final AgentsXProjectRepository agentsxprojectRepository;
    private final RestTemplate restTemplate;

    @PersistenceContext
    private EntityManager entityManager;

    public ProjectListResponse getProjectList(Integer agentID, Integer projectId) throws NoSuchFieldException,
            IllegalAccessException {
        ProjectListResponse projectList = new ProjectListResponse();
        AgentConfigMethods agentConfig = agentconfigmethodsRepository.findByAgentIdAndOperation(agentID, GET_REPOS_LIST);
        AgentsXProject agentsKeys = agentsxprojectRepository.findByAgentIdAndProjectId(agentID, projectId);
        String uri = agentConfig.getEndpoint();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uri);
        Class<?> requestMapping = AgentsXProject.class;
        for (AgentConfigDetails param : agentConfig.getParams()) {
            String paramValue = "";
            if (param.getRequestParam() != null) {
                LOG.info("getRequestparam -> {} ", param.getRequestParam());
                Field f = requestMapping.getDeclaredField(param.getRequestParam());
                f.setAccessible(true);
                paramValue = f.get(agentsKeys).toString();
            } else {
                paramValue = param.getDefaultValue();
                LOG.info("getDefaultvalue -> {}", param.getDefaultValue());
            }
            builder.queryParam(
                    param.getParamName(),
                    paramValue
            );
        }
        HttpMethod requestMethod = CommonFieldsProcessor.getRequestType(agentConfig.getRequestType().toUpperCase());
        ResponseEntity<LinkedHashMap[]> response = restTemplate.exchange(builder.toUriString(), requestMethod, null, LinkedHashMap[].class);
        Class<?> responseMapping = ProjectDTO.class;
        projectList.setAgentId(agentID);
        for (LinkedHashMap element : response.getBody()) {
            ProjectDTO projectTmp = new ProjectDTO();
            for (AgentResponseConfig responseF : agentConfig.getResponseParams()) {
                Field f = responseMapping.getDeclaredField(responseF.getParamName());
                f.setAccessible(true);
                f.set(projectTmp, element.get(responseF.getResponseParam()).toString());
            }
            projectList.getProjectList().add(projectTmp);
        }
        for (ProjectDTO project : projectList.getProjectList()) {
            List<ReposXProject> saved = reposxprojectRepository.findByRepoId(project.getReference());
            if (!saved.isEmpty()) project.setIsConnected("Y");
            else project.setIsConnected("N");
        }
        return projectList;
    }


    public Boolean getConnectProject(ConnectProjectRequest request) throws NoSuchFieldException, IllegalAccessException {
        LOG.info("getConnectProject...");
        AgentConfigMethods agentConfig = agentconfigmethodsRepository.findByAgentIdAndOperation(request.getAgentId(), CONNECT_REPO);
        AgentsXProject agentsKeys = agentsxprojectRepository.findByAgentIdAndProjectId(request.getAgentId(), request.getProjectID());
        String uri = agentConfig.getEndpoint();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uri);
        Class<?> agentConfigMapping = AgentsXProject.class;
        Class<?> requestMapping = ConnectProjectRequest.class;
        LOG.info("setting paramters...");
        for (AgentConfigDetails param : agentConfig.getParams()) {
            String paramValue = "";
            if (("request").equalsIgnoreCase(param.getRequestType())) {
                Field f = requestMapping.getDeclaredField(param.getRequestParam());
                f.setAccessible(true);
                paramValue = f.get(request).toString();
            } else {
                Field f = agentConfigMapping.getDeclaredField(param.getRequestParam());
                f.setAccessible(true);
                paramValue = f.get(agentsKeys).toString();
            }
            LOG.info("setting {} -> {}", param.getParamName(), paramValue);
            builder.queryParam(
                    param.getParamName(),
                    paramValue
            );
        }
        HttpMethod requestMethod = CommonFieldsProcessor.getRequestType(agentConfig.getRequestType().toUpperCase());
        LOG.info("creating webhook...");
        ResponseEntity<LinkedHashMap> response = restTemplate
                .exchange(builder.toUriString(), requestMethod, null, LinkedHashMap.class);
        HttpStatus status = response.getStatusCode();
        LOG.info("response -> {}", status);
        if (status == HttpStatus.CREATED || status == HttpStatus.OK) {
            ReposXProject newrepo = new ReposXProject();
            newrepo.setAgentId(request.getAgentId());
            newrepo.setProjectId(request.getProjectID());
            newrepo.setRepoId(request.getRepoReference());
            newrepo.setIsActive("Y");
            LOG.info("Saving repo connected");
            reposxprojectRepository.save(newrepo);
            return true;
        }
        return false;
    }

    public RepoDataPerProjectResponse getRepoDataPerProject(Integer projectId) {
        List<ReposXProject> repos = reposxprojectRepository.findByProjectId(projectId);
        RepoDataPerProjectResponse response = new RepoDataPerProjectResponse();
        for (ReposXProject repo : repos) {
            AgentConfig agent = agentconfigRepository.getOne(repo.getAgentId());
            RepoResponseDTO repoResponse = new RepoResponseDTO();
            repoResponse.setAgentName(agent.getAgentName());
            for (AgentDataConfig datasource : agent.getDataconfig()) {
                String source = datasource.getSchemaName() + "." + datasource.getTableName();
                String fields = datasource.getEventAuthorField() + "  eventauthor, " +
                        datasource.getEventDescriptionField() + " eventdescription, " +
                        datasource.getEventDateField() + " eventdate";
                String queryStr = "select " + fields + " from " + source + " where " + agent.getRepoIdField() + " = ?1";
                try {
                    Query query = entityManager.createNativeQuery(queryStr);
                    query.setParameter(1, repo.getRepoId());
                    List<Object[]> result = query.getResultList();
                    for (Object[] row : result) {
                        RepoEventDTO eventRow = new RepoEventDTO();
                        eventRow.setEventAuthor(row[0].toString());
                        eventRow.setEventDescription(row[1].toString());
                        eventRow.setEventDate(row[2].toString());
                        repoResponse.getEvents().add(eventRow);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            }
            response.getRepos().add(repoResponse);
        }
        return response;
    }
}
