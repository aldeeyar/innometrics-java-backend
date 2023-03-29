package com.innopolis.innometrics.authserver.entitiy;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@NoArgsConstructor
@Embeddable
@Table(name = "project_users")
public class ProjectUsers implements Serializable {
    @Column(name = "projectid")
    private Integer projectId;
    @Column
    private String email;
}
