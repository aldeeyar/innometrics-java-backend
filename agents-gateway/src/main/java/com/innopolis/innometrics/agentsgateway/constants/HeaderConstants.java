package com.innopolis.innometrics.agentsgateway.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum HeaderConstants {
    LOCATION("Location"),
    ACCEPT("Accept"),
    APPLICATION_JSON("application/json"),
    UTF("UTF-8"),
    INNO_METRICS("InnoMetrics"),
    SECURITY("Security"),
    SIZE_COMPLEXITY("SizeAndComplexity"),
    COVERAGE("Coverage"),
    RELIABILITY("Reliability"),
    ISSUE("Issues"),
    MAINTAINABILITY("Maintainability");

    String value;
}
