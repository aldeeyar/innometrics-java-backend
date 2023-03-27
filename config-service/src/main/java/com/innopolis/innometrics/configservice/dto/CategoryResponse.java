package com.innopolis.innometrics.configservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CategoryResponse implements Serializable {
    private Integer catId;
    private String catName;
    private String catDescription;
    private Date creationDate;
    private String createdBy;
    private Date lastUpdate;
    private String updateBy;
}
