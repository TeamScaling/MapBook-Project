package com.scaling.libraryservice.commons.caching;

import java.io.Serializable;
import lombok.ToString;

@ToString
public class UserInfo implements Serializable {

    private String name;

    private int age;

    private String password;

    public UserInfo(String name, int age, String password) {
        this.name = name;
        this.age = age;
        this.password = password;
    }
}
