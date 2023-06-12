package com.innopolis.innometrics.restapi.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class DeleteRequest implements Serializable {
    private Integer[] ids;
}
