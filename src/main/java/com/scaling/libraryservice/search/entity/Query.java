package com.scaling.libraryservice.search.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.ToString;

@Entity
@Table(name = "ranks5000")
@ToString
@Getter
public class Query {

    @Id
    @Column(name = "no")
    private Long id;
    @Column(name = "class_no")
    private String kdcNm;
    @Column(name = "isbn_thirteen_no")
    private String isbn;

}
