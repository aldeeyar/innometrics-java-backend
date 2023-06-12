package com.innopolis.innometrics.agentsgateway.controller;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.innopolis.innometrics.agentsgateway.dto.*;
import com.innopolis.innometrics.agentsgateway.service.*;
import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
class ControllerTest {

    @Autowired
    private AgentProjectController controller;
    @Autowired
    private AgentConfigService agentconfigService;
    @Autowired
    private AgentDataConfigService agentdataconfigService;
    @Autowired
    private AgentConfigMethodsService agentconfigmethodsService;
    @Autowired
    private AgentConfigDetailsService agentconfigdetailsService;
    @Autowired
    private AgentResponseConfigService agentresponseconfigService;

    @Autowired
    private MockMvc mockMvc;

    ControllerTest() {
    }


    @Test
    void contextLoads() {
        assertThat(controller).isNotNull();
        assertThat(agentconfigService).isNotNull();
        assertThat(agentdataconfigService).isNotNull();
        assertThat(agentconfigmethodsService).isNotNull();
        assertThat(agentconfigdetailsService).isNotNull();
        assertThat(agentresponseconfigService).isNotNull();
    }

    private AgentResponse agentResponseResult;

    @Test
    void testGetAgentConfiguration() throws Exception {
        mockMvc.perform(get("/AgentAdmin/AgentConfiguration?AgentID=1")
                        .accept(APPLICATION_JSON))
                .andExpect(status().is(400));
    }

    @Test
    void testGetAgentList() throws Exception {
        mockMvc.perform(get("/AgentAdmin/Agent?ProjectId=1")
                        .accept(APPLICATION_JSON))
                .andExpect(status().is(400));
    }

    @Test
    void testGetAgentResponse200() throws Exception {
        mockMvc.perform(get("/AgentAdmin/Agent/1")
                        .accept(APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    void testGetAgentResponse404() throws Exception {
        mockMvc.perform(get("/AgentAdmin/Agent/1000000")
                        .accept(APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testPostAgentResponse201() throws Exception {
        AgentResponse agentResponse = createAgentResponse();
        String requestJson = getJSON(agentResponse);
        mockMvc.perform(post("/AgentAdmin/Agent")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated());
    }

    @Test
    void testPostAgentResponse400() throws Exception {
        mockMvc.perform(post("/AgentAdmin/Agent")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testPutAgentResponseUpdate() throws Exception {
        AgentResponse agentResponse = createAgentResponse();
        String requestJson = getJSON(agentResponse);
        mockMvc.perform(post("/AgentAdmin/Agent")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated());
    }

    @Test
    void testPutAgentResponseCreate() throws Exception {
        AgentResponse agentResponse = createAgentResponse();
        agentResponse.setAgentId(1000000);
        String requestJson = getJSON(agentResponse);
        mockMvc.perform(put("/AgentAdmin/Agent/1000000")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    agentResponseResult = (AgentResponse) this.convertJSONStringToObject(json);
                });
        Assertions.assertEquals(agentResponse.getAgentName(), agentResponseResult.getAgentName());
        Assertions.assertNotEquals(agentResponse.getAgentId(), agentResponseResult.getAgentId());
    }

    @Test
    void testPutAgentResponse400() throws Exception {
        mockMvc.perform(put("/AgentAdmin/Agent/55")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteAgentResponse200() throws Exception {
        AgentResponse agentResponse = createAgentResponse();
        String requestJson = getJSON(agentResponse);
        mockMvc.perform(post("/AgentAdmin/Agent")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    agentResponseResult = (AgentResponse) this.convertJSONStringToObject(json);
                });
        Integer agentId = agentResponseResult.getAgentId();
        mockMvc.perform(delete("/AgentAdmin/Agent/" + agentId)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    agentResponseResult = (AgentResponse) this.convertJSONStringToObject(json);
                });
        Assertions.assertEquals(agentResponse.getAgentName(), agentResponseResult.getAgentName());
        Assertions.assertEquals(agentId, agentResponseResult.getAgentId());
        mockMvc.perform(delete("/AgentAdmin/Agent/" + agentId)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteAgentResponse404() throws Exception {
        mockMvc.perform(delete("/AgentAdmin/Agent/1000000")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    private DataConfigDTO createDataConfigDTO() {
        DataConfigDTO dataConfigDTO = new DataConfigDTO();
        dataConfigDTO.setAgentId(1);
        dataConfigDTO.setSchemaName("test");
        dataConfigDTO.setTableName("test");
        dataConfigDTO.setEventDateField("test");
        dataConfigDTO.setEventAuthorField("test");
        dataConfigDTO.setIsActive("Y");
        return dataConfigDTO;
    }

    @Test
    void testGetDataList() throws Exception {
        mockMvc.perform(get("/AgentAdmin/AgentData")
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testGetDataById200() throws Exception {
        mockMvc.perform(get("/AgentAdmin/AgentData/data/7")
                        .accept(APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    void testGetDataById404() throws Exception {
        mockMvc.perform(get("/AgentAdmin/AgentData/data/1000000")
                        .accept(APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetDataByAgentId200() throws Exception {
        mockMvc.perform(get("/AgentAdmin/AgentData/agent/1")
                        .accept(APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    void testGetDataByAgentId404() throws Exception {
        mockMvc.perform(get("/AgentAdmin/AgentData/agent/1000000")
                        .accept(APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testPostData201() throws Exception {
        DataConfigDTO dataConfigDTO = createDataConfigDTO();
        String requestJson = getJSON(dataConfigDTO);
        mockMvc.perform(post("/AgentAdmin/AgentData")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().is(400));
    }

    @Test
    void testPostDataEmptyBody400() throws Exception {
        mockMvc.perform(post("/AgentAdmin/AgentData")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testPostDataWrongAgentId400() throws Exception {
        DataConfigDTO dataConfigDTO = createDataConfigDTO();
        dataConfigDTO.setAgentId(100000);
        String requestJson = getJSON(dataConfigDTO);
        mockMvc.perform(post("/AgentAdmin/AgentData")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testPutDataCreate() throws Exception {
        DataConfigDTO dataConfigDTO = createDataConfigDTO();
        dataConfigDTO.setDataConfigId(100000);
        String requestJson = getJSON(dataConfigDTO);
        mockMvc.perform(put("/AgentAdmin/AgentData/100000")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().is(400));
    }

    @Test
    void testPutDataEmptyBody400() throws Exception {
        mockMvc.perform(put("/AgentAdmin/AgentData/7")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testPutDataWrongAgentId400() throws Exception {
        DataConfigDTO dataConfigDTO = createDataConfigDTO();
        dataConfigDTO.setAgentId(100000);
        String requestJson = getJSON(dataConfigDTO);
        mockMvc.perform(put("/AgentAdmin/AgentData/7")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteData404() throws Exception {
        mockMvc.perform(delete("/AgentAdmin/AgentData/data/1000000")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCRUDData() throws Exception {
        DataConfigDTO dataConfigDTO = createDataConfigDTO();
        String requestJson = getJSON(dataConfigDTO);
        mockMvc.perform(post("/AgentAdmin/AgentData")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().is(400));
    }

    @Test
    void testGetCreateSeveralData() throws Exception {
        DataConfigDTO dataConfigDTO = createDataConfigDTO();
        String requestJson = getJSON(dataConfigDTO);
        String jsonString = mockMvc.perform(get("/AgentAdmin/AgentData")
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        int initialSize = StringUtils.countMatches(jsonString, "datacofingid");
        int count = 5;
        for (int i = 0; i < count; i++) {
            mockMvc.perform(post("/AgentAdmin/AgentData")
                            .contentType(APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().is(400));
        }
        mockMvc.perform(get("/AgentAdmin/AgentData")
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    void testGetMethodsList() throws Exception {
        mockMvc.perform(get("/AgentAdmin/AgentConfigMethods")
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testGetMethodsById200() throws Exception {
        mockMvc.perform(get("/AgentAdmin/AgentConfigMethods/method/1")
                        .accept(APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    void testGetMethodsById404() throws Exception {
        mockMvc.perform(get("/AgentAdmin/AgentConfigMethods/method/100000")
                        .accept(APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetMethodsByAgentId200() throws Exception {
        mockMvc.perform(get("/AgentAdmin/AgentConfigMethods/agent/1")
                        .accept(APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    void testGetMethodsByAgentId404() throws Exception {
        mockMvc.perform(get("/AgentAdmin/AgentConfigMethods/agent/100000")
                        .accept(APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetMethodsByOperation() throws Exception {
        mockMvc.perform(get("/AgentAdmin/AgentConfigMethodsOperation/1?operation=ProjectList")
                        .accept(APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    void testPostMethodsEmptyBody400() throws Exception {
        mockMvc.perform(post("/AgentAdmin/AgentConfigMethods")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testPostMethodsWrongAgentId400() throws Exception {
        MethodConfigDTO methodConfigDTO = createMethodConfigDTO();
        methodConfigDTO.setAgentId(100000);
        String requestJson = getJSON(methodConfigDTO);
        mockMvc.perform(post("/AgentAdmin/AgentConfigMethods")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testPutMethodsEmptyBody400() throws Exception {
        mockMvc.perform(put("/AgentAdmin/AgentConfigMethods/11")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testPutMethodsWrongAgentId400() throws Exception {
        MethodConfigDTO methodConfigDTO = createMethodConfigDTO();
        methodConfigDTO.setAgentId(100000);
        String requestJson = getJSON(methodConfigDTO);
        mockMvc.perform(put("/AgentAdmin/AgentConfigMethods/11")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteMethodsById404() throws Exception {
        mockMvc.perform(delete("/AgentAdmin/AgentConfigMethods/method/1000000")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteMethodsAgentById404() throws Exception {
        mockMvc.perform(delete("/AgentAdmin/AgentConfigMethods/agent/1000000")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    void testGetDetailsByMethodId200() throws Exception {
        mockMvc.perform(get("/AgentAdmin/AgentDetails/method/1")
                        .accept(APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    void testGetDetailsByMethodId404() throws Exception {
        mockMvc.perform(get("/AgentAdmin/AgentDetails/method/1000000")
                        .accept(APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testPostDetailsEmptyBody400() throws Exception {
        mockMvc.perform(post("/AgentAdmin/AgentDetails")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testPostDetailsWrongMethodId400() throws Exception {
        DetailsConfigDTO detailsConfigDTO = createDetailsConfigDTO();
        detailsConfigDTO.setMethodId(100000);
        String requestJson = getJSON(detailsConfigDTO);
        mockMvc.perform(post("/AgentAdmin/AgentDetails")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testPutDetailsEmptyBody400() throws Exception {
        mockMvc.perform(put("/AgentAdmin/AgentDetails/26")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testPutDetailsWrongMethodId400() throws Exception {
        DetailsConfigDTO detailsConfigDTO = createDetailsConfigDTO();
        detailsConfigDTO.setMethodId(100000);
        String requestJson = getJSON(detailsConfigDTO);
        mockMvc.perform(put("/AgentAdmin/AgentDetails/26")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetResponseList() throws Exception {
        mockMvc.perform(get("/AgentAdmin/AgentResponse")
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testGetResponseById200() throws Exception {
        mockMvc.perform(get("/AgentAdmin/AgentResponse/response/1")
                        .accept(APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    void testGetResponseById404() throws Exception {
        mockMvc.perform(get("/AgentAdmin/AgentResponse/response/1000000")
                        .accept(APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetResponseByMethodId200() throws Exception {
        mockMvc.perform(get("/AgentAdmin/AgentResponse/method/1")
                        .accept(APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    void testGetResponseByMethodId404() throws Exception {
        mockMvc.perform(get("/AgentAdmin/AgentResponse/method/1000000")
                        .accept(APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testPostResponseEmptyBody400() throws Exception {
        mockMvc.perform(post("/AgentAdmin/AgentResponse")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testPostResponseWrongMethodId400() throws Exception {
        ResponseConfigDTO responseConfigDTO = createResponseConfigDTO();
        responseConfigDTO.setMethodId(100000);
        String requestJson = getJSON(responseConfigDTO);
        mockMvc.perform(post("/AgentAdmin/AgentResponse")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testPutResponseEmptyBody400() throws Exception {
        mockMvc.perform(put("/AgentAdmin/AgentResponse/26")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testPutResponseWrongMethodId400() throws Exception {
        ResponseConfigDTO responseConfigDTO = createResponseConfigDTO();
        responseConfigDTO.setMethodId(100000);
        String requestJson = getJSON(responseConfigDTO);
        mockMvc.perform(put("/AgentAdmin/AgentResponse/26")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteResponse404() throws Exception {
        mockMvc.perform(delete("/AgentAdmin/AgentResponse/response/1000000")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    private AgentResponse createAgentResponse() {
        AgentResponse agentResponse = new AgentResponse();
        agentResponse.setAgentName("test");
        agentResponse.setDescription("test");
        agentResponse.setIsConnected("Y");
        agentResponse.setSourceType("test");
        agentResponse.setDbSchemaSource("test");
        return agentResponse;
    }

    private MethodConfigDTO createMethodConfigDTO() {
        MethodConfigDTO methodConfigDTO = new MethodConfigDTO();
        methodConfigDTO.setAgentId(34);
        methodConfigDTO.setDescription("test");
        methodConfigDTO.setEndpoint("test");
        methodConfigDTO.setIsActive("Y");
        methodConfigDTO.setOperation("test");
        methodConfigDTO.setRequestType("POST");
        return methodConfigDTO;
    }

    private DetailsConfigDTO createDetailsConfigDTO() {
        DetailsConfigDTO detailsConfigDTO = new DetailsConfigDTO();
        detailsConfigDTO.setMethodId(151);
        detailsConfigDTO.setParamName("test");
        detailsConfigDTO.setParamType("test");
        detailsConfigDTO.setRequestParam("test");
        detailsConfigDTO.setRequestType("test");
        detailsConfigDTO.setIsActive("Y");
        return detailsConfigDTO;
    }

    private ResponseConfigDTO createResponseConfigDTO() {
        ResponseConfigDTO responseConfigDTO = new ResponseConfigDTO();
        responseConfigDTO.setMethodId(151);
        responseConfigDTO.setResponseParam("test");
        responseConfigDTO.setParamName("test");
        responseConfigDTO.setParamType("test");
        responseConfigDTO.setIsActive("Y");
        return responseConfigDTO;
    }

    private <T> Object convertJSONStringToObject(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        JavaTimeModule module = new JavaTimeModule();
        mapper.registerModule(module);
        return mapper.readValue(json, (Class<T>) AgentResponse.class);
    }

    private <T> String getJSON(T object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(object);
    }
}
