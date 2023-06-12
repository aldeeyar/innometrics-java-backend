package com.innopolis.innometrics.configservice.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest implements Serializable {
    private Integer catId;
    private String catName;
    private String catDescription;
}
