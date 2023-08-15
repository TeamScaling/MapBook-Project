package com.scaling.libraryservice.batch.jobController;

import lombok.Data;

@Data
public class ReqJobDto {

    private String jobName;

    private String parameters;

    private String authKey;

    public ReqJobDto() {
    }

    public ReqJobDto(String jobName, String parameters, String authKey) {
        this.jobName = jobName;
        this.parameters = parameters;
        this.authKey = authKey;
    }
}
