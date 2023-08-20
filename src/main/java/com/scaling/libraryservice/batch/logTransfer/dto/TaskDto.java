package com.scaling.libraryservice.batch.logTransfer.dto;

import com.scaling.libraryservice.batch.logTransfer.entity.Task;
import lombok.Getter;

@Getter
public class TaskDto {

    private final Integer code;

    private final String name;

    public TaskDto(Task task) {
        this.code = task.getCode();
        this.name = task.getName();
    }
}
