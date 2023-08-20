package com.scaling.libraryservice.batch.logTransfer.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.ToString;

@Entity
@Table(name = "TASK")
@Getter @ToString
public class Task {

    @Id
    @Column(name = "CODE")
    private Integer code;

    @Column(name = "NAME")
    private String name;

}
