package com.innopolis.innometrics.restapi.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class CategoryResponse extends CategoryRequest implements Serializable {
    private Date creationDate;
    private String createdBy;
    private Date lastUpdate;
    private String updateBy;
}
