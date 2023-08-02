package com.scaling.libraryservice.search.dto;

import com.scaling.libraryservice.commons.timer.TimeMeasurable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.lang.NonNull;

@Getter
@Setter @ToString
public class RespBooksDto implements TimeMeasurable<MetaDto> {

    private MetaDto meta;
    private List<BookDto> documents;

    public RespBooksDto(MetaDto metaDto, List<BookDto> documents) {
        this.meta = metaDto;
        this.documents = documents;
    }

    public RespBooksDto(MetaDto metaDto, @NonNull Page<BookDto> booksPage) {
        this.meta = metaDto;
        this.documents = booksPage.stream().collect(Collectors.toList());
    }

    public boolean isEmptyResult(){
        return this.meta.getTotalElements() == 0;
    }

    @Override
    public void addMeasuredTime(String time) {
        this.getMeta().addSearchTime(time);
    }
}