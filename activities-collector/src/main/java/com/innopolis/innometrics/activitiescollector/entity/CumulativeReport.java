package com.innopolis.innometrics.activitiescollector.entity;

import com.vladmihalcea.hibernate.type.interval.PostgreSQLIntervalType;
import lombok.Data;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Duration;
import java.util.Date;

@Entity
@Table(name = "cumlativerepactivity")
@TypeDef(typeClass = PostgreSQLIntervalType.class, defaultForType = Duration.class)
@Data
public class CumulativeReport implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Integer reportid;
    @Column(name = "email")
    private String email;
    @Column(name = "used_time", columnDefinition = "interval")
    private String usedTime;
    @Column(name = "dailysum", columnDefinition = "interval")
    private String dailySum;
    @Column(name = "monthlysum", columnDefinition = "interval")
    private String monthlySum;
    @Column(name = "yearlysum", columnDefinition = "interval")
    private String yearlySum;
    @Column(name = "captureddate")
    private Date capturedDate;
    @Column(name = "executable_name")
    private String executableName;
}
