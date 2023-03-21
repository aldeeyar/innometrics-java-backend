package com.innopolis.innometrics.restapi.controller;

import com.innopolis.innometrics.restapi.config.JwtToken;
import com.innopolis.innometrics.restapi.constants.ErrorMessages;
import com.innopolis.innometrics.restapi.dto.MeasurementTypeRequest;
import com.innopolis.innometrics.restapi.dto.MeasurementTypeResponse;
import com.innopolis.innometrics.restapi.exceptions.ValidationException;
import com.innopolis.innometrics.restapi.service.MeasurementTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*",
        methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE})
@RequestMapping(value = "/V1/Admin", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class MeasurementAPI {
    private final MeasurementTypeService measurementTypeService;
    @Autowired
    private JwtToken jwtTokenUtil;

    @PostMapping("/MeasurementType")
    public ResponseEntity<MeasurementTypeResponse> createMeasurementType(@RequestBody @NotNull MeasurementTypeRequest measurementType,
                                                                         @RequestHeader String token) {
        if (measurementType.getLabel() == null || measurementType.getWeight() == null) {
            throw new ValidationException(ErrorMessages.NOT_ENOUGH_DATA.getMessage());
        }
        MeasurementTypeResponse response = measurementTypeService.createMeasurement(measurementType, token, jwtTokenUtil);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private void handeException() {
        throw new ValidationException(ErrorMessages.NOT_ENOUGH_DATA.getMessage());
    }
}
