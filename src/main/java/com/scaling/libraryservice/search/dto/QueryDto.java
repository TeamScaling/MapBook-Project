package com.scaling.libraryservice.search.dto;

import com.scaling.libraryservice.search.entity.Query;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueryDto {
    private Long id;
    private String kdcNm;
    private String isbn;

    public QueryDto(Query ranks) {
        this.id = ranks.getId();
        this.kdcNm = ranks.getKdcNm();
        this.isbn = ranks.getIsbn();
    }

    @Override
    public String toString() {
        return "QueryDto{" +
            "id=" + id +
            ", kdcNm='" + kdcNm + '\'' +
            ", isbn='" + isbn + '\'' +
            '}';
    }
}
