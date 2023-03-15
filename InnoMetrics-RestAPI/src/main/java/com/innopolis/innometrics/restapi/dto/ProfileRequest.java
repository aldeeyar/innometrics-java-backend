package com.innopolis.innometrics.restapi.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProfileRequest implements Serializable {
    private Integer profileId;
    private String userEmail;
    private String macAddress;
    private Double averageCpu;
    private Double averageGpu;
    private Double averageMemory;
    private Double averageStorage;
    private String mainVendor;
    private String cpuVendor;
    private String cpuModel;
    private String gpuVendor;
    private String gpuModel;
    private String memoryVendor;
    private int memoryCounter;
    private String memoryModel;
    private String storageVendor;
    private String storageModel;
    private int storageCounter;
}
