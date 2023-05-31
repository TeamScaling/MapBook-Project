package com.scaling.libraryservice.commons.api.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "AUTHKEY")
@Getter
public class AuthKey {

    @Id @Column(name = "id")
    private Integer id;

    @Column(name = "api_Nm")
    private String apiNm;

    @Column(name = "auth_key")
    private String authKey;

}
