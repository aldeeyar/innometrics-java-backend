package com.innopolis.innometrics.restapi.service;

import com.innopolis.innometrics.restapi.config.JwtToken;
import com.innopolis.innometrics.restapi.constants.ErrorMessages;
import com.innopolis.innometrics.restapi.dto.MeasurementTypeRequest;
import com.innopolis.innometrics.restapi.dto.MeasurementTypeResponse;
import com.innopolis.innometrics.restapi.entity.MeasurementType;
import com.innopolis.innometrics.restapi.exceptions.ValidationException;
import com.innopolis.innometrics.restapi.repository.MeasurementTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class MeasurementTypeService {
    private final MeasurementTypeRepository measurementTypeRepository;
    @Autowired
    private JwtToken jwtTokenUtil;

    public MeasurementTypeResponse createMeasurement(MeasurementTypeRequest measurementType, String token) {
        if (Boolean.TRUE.equals(measurementTypeRepository.existsByLabel(measurementType.getLabel()))) {
            throw new ValidationException(ErrorMessages.MEASUREMENT_TYPE_EXISTS.getMessage());
        }
        String username = jwtTokenUtil.getUsernameFromToken(token);
        MeasurementType myType = new MeasurementType();
        myType.setLabel(measurementType.getLabel());
        myType.setDescription(measurementType.getDescription());
        myType.setWeight(measurementType.getWeight());
        myType.setOperation(measurementType.getOperation());
        myType.setScale(measurementType.getScale());
        myType.setIsActive("Y");
        myType.setCreatedBy(username);
        myType.setCreationDate(new Date());
        measurementTypeRepository.save(myType);
        return new MeasurementTypeResponse(myType);
    }
}
