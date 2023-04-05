package com.scaling.libraryservice.dto;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class MetaDto {

    private boolean is_End = false;

    private int pageable_count = 3;

    private int total_count = 10;

}
