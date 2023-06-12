package com.innopolis.innometrics.restapi.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
public class Report implements Serializable {
    private Set<ActivityReport> activities = new HashSet<>();
}
