package com.innopolis.innometrics.restapi.entity;


import lombok.Data;

import javax.persistence.*;

@Entity
@Table
@IdClass(PermissionId.class)
@Data
public class Permission {
    @Id
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "page", referencedColumnName = "page")
    private Page page;
    @Id
    @Column
    private String role;
}
