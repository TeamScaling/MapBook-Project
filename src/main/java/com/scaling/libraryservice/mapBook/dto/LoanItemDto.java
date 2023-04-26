package com.scaling.libraryservice.mapBook.dto;

import com.scaling.libraryservice.mapBook.domain.ConfigureUriBuilder;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.json.JSONObject;
import org.springframework.web.util.UriComponentsBuilder;

@Getter
@RequiredArgsConstructor
@ToString
public class LoanItemDto implements ConfigureUriBuilder {

    private Integer no;

    private Integer ranking;

    private String bookName;

    private Double isbn13;

    private Integer loan_count;

    private String classNo;

    public LoanItemDto(JSONObject obj) {

        this.no = obj.getInt("no");
        this.ranking = Integer.parseInt(obj.getString("ranking"));
        this.bookName = obj.getString("bookname");
        this.isbn13 = Double.parseDouble(obj.getString("isbn13"));
        this.loan_count = Integer.parseInt(obj.getString("loan_count"));
        this.classNo = obj.getString("class_no");
    }

    @Override
    public UriComponentsBuilder configUriBuilder(String pageSize) {
        UriComponentsBuilder uriBuilder
            = UriComponentsBuilder.fromHttpUrl("http://data4library.kr/api/loanItemSrch")
            .queryParam("authKey","0f6d5c95011bddd3da9a0cc6975868d8293f79f0ed1c66e9cd84e54a43d4bb72")
            .queryParam("pageSize", pageSize)
            .queryParam("format","json");

        return uriBuilder;
    }
}
