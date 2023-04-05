package com.innopolis.innometrics.activitiescollector.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class DeleteRequest implements Serializable {
    private Integer[] ids;
}
