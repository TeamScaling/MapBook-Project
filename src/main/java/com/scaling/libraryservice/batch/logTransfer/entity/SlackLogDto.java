package com.scaling.libraryservice.batch.logTransfer.entity;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter @ToString
public class SlackLogDto {

    private Integer taskCode;

    private LocalDateTime logDateTime;

    private String title;

    private Double taskTime;

    private Integer areaCd;

    private String apiUrl;
}
