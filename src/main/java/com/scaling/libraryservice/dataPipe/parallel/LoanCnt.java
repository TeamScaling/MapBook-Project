package com.scaling.libraryservice.dataPipe.parallel;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;


@Table(name = "loancnt")
public class LoanCnt {

    @Id
    private Long id;

    @Column(name = "isbn")
    private Double isbn;

    @Column(name = "loan_cnt")
    private Integer loanCnt;

}
