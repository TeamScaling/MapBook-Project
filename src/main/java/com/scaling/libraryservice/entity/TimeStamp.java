package com.scaling.libraryservice.entity;

import java.time.LocalDate;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public class TimeStamp {

    @CreatedDate
    private LocalDate createdAt;

    @LastModifiedDate
    private LocalDate modifiedAt;

}
