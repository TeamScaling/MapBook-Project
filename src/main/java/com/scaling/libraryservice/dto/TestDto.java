package com.scaling.libraryservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter@Setter
@ToString
public class TestDto {

    private final Integer id;

    private final String thread;

    public TestDto(Integer id,String name) {
        this.id = id;
        this.thread = name;
    }
}
