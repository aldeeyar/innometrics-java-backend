package com.innopolis.innometrics.authserver.entitiy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "teams")
public class Team implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, name = "teamid")
    private Integer teamId;
    @Column(name = "teamname")
    private String teamName;
    @Column(name = "companyid")
    private Integer companyId;
    @Column
    private Integer projectID;
    @Column
    private String description;
    @Column(name = "isactive")
    private String isActive;
    @Column(name = "creationdate", insertable = false, updatable = false)
    private Date creationDate;
    @Column(name = "createdby", insertable = false, updatable = false)
    private String createdBy;
    @Column(name = "lastupdate", insertable = false)
    private Date lastUpdate;
    @Column(name = "updateby", insertable = false)
    private String updateBy;
}
