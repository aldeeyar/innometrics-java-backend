package com.innopolis.innometrics.restapi.service;

import com.innopolis.innometrics.restapi.config.JwtToken;
import com.innopolis.innometrics.restapi.dto.MeasurementTypeRequest;
import com.innopolis.innometrics.restapi.entity.MeasurementType;
import com.innopolis.innometrics.restapi.exceptions.ValidationException;
import com.innopolis.innometrics.restapi.repository.MeasurementTypeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@Import(value = {
        MeasurementTypeService.class
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MeasurementTypeServiceTest {

    @Autowired
    MeasurementTypeService measurementTypeService;
    @Autowired
    MeasurementTypeRepository measurementTypeRepository;


    @Test
    void create() {
        MeasurementTypeRequest request = createRequest();
        measurementTypeRepository.save(createMeasurement());
        JwtToken token = new JwtToken();
        Assertions.assertThrows(ValidationException.class, () -> measurementTypeService.createMeasurement(request, "a", token));
    }

    private MeasurementTypeRequest createRequest() {
        MeasurementTypeRequest request = new MeasurementTypeRequest();
        request.setDescription("testDescription");
        request.setWeight(500F);
        request.setScale(100F);
        request.setLabel("testLabel");
        request.setOperation("testOperation");
        return request;
    }

    private MeasurementType createMeasurement() {
        MeasurementType type = new MeasurementType();
        type.setCreatedBy("dariya");
        type.setLabel("testLabel");
        type.setWeight(100F);
        type.setScale(1F);
        type.setIsActive("Y");
        return type;
    }
}
