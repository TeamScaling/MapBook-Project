package com.scaling.libraryservice.mapBook.entity;

import java.time.LocalDate;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 생성 및 수정된 날짜 & 시간 데이터를 DB에 기록 한다. 상속 구조로 사용되며 상속하는
 * 엔티티 관련 테이블에 관련 칼럼과 데이터가 생성 된다.
 */
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public class TimeStamp {

    @CreatedDate
    private LocalDate createdAt;

    @LastModifiedDate
    private LocalDate modifiedAt;

}
