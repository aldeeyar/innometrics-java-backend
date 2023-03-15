package com.innopolis.innometrics.restapi.controller;

import com.innopolis.innometrics.restapi.dto.MeasurementTypeRequest;
import com.innopolis.innometrics.restapi.dto.MeasurementTypeResponse;
import com.innopolis.innometrics.restapi.config.JwtToken;
import com.innopolis.innometrics.restapi.constants.ErrorMessages;
import com.innopolis.innometrics.restapi.entity.MeasurementType;
import com.innopolis.innometrics.restapi.exceptions.ValidationException;
import com.innopolis.innometrics.restapi.repository.MeasurementTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Date;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*",
        methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE})
@RequestMapping(value = "/V1/Admin", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class MeasurementAPI {
    private final MeasurementTypeRepository measurementTypeService;
    @Autowired
    private JwtToken jwtTokenUtil;

    @PostMapping("/MeasurementType")
    public ResponseEntity<MeasurementTypeResponse> createMeasurementType(@RequestBody @NotNull MeasurementTypeRequest measurementType,
                                                                         @RequestHeader String token) {
        if (measurementType.getLabel() == null || measurementType.getWeight() == null) {
            throw new ValidationException(ErrorMessages.NOT_ENOUGH_DATA.getMessage());
        }
        if (Boolean.TRUE.equals(measurementTypeService.existsByLabel(measurementType.getLabel()))) {
            throw new ValidationException(ErrorMessages.MEASUREMENT_TYPE_EXISTS.getMessage());
        }
        String username = jwtTokenUtil.getUsernameFromToken(token);
        //todo
        MeasurementType myType = new MeasurementType();
        myType.setLabel(measurementType.getLabel());
        myType.setDescription(measurementType.getDescription());
        myType.setWeight(measurementType.getWeight());
        myType.setOperation(measurementType.getOperation());
        myType.setScale(measurementType.getScale());
        myType.setIsactive("Y");
        myType.setCreatedby(username);
        myType.setCreationdate(new Date());
        measurementTypeService.save(myType);
        MeasurementTypeResponse response = new MeasurementTypeResponse(myType);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private void handeException(MethodArgumentNotValidException e) {
        throw new ValidationException(ErrorMessages.NOT_ENOUGH_DATA.getMessage());
    }
}
