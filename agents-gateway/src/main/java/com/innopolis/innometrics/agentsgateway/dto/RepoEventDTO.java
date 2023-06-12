package com.innopolis.innometrics.agentsgateway.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class RepoEventDTO implements Serializable {
    private String eventDate;
    private String eventAuthor;
    private String eventDescription;
}
