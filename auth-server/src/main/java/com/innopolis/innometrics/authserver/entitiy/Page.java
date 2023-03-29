package com.innopolis.innometrics.authserver.entitiy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Page {
    @Id
    @Column
    private String page;
    @Column
    private String icon;
}
