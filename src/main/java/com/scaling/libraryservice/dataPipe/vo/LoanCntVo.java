package com.scaling.libraryservice.dataPipe.vo;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.csv.CSVRecord;

@Getter
@NoArgsConstructor
@ToString
public class LoanCntVo {

    @CsvBindByName(column = "ISBN")
    private String isbn;
    // 책 제목
    @CsvBindByName(column = "LOAN_CNT")
    private String loanCnt;

    public LoanCntVo(String isbn, String loanCnt) {
        this.isbn = isbn;
        this.loanCnt = loanCnt;
    }

}
