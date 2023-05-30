package com.scaling.libraryservice.search.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.json.JSONObject;
import org.springframework.data.domain.Page;

@Getter
@Setter @ToString
public class
RespBooksDto {

    private MetaDto meta;
    private List<BookDto> documents;

    public RespBooksDto(MetaDto metaDto, List<BookDto> documents) {
        this.meta = metaDto;
        this.documents = documents;
    }

    public RespBooksDto(MetaDto metaDto, Page<BookDto> booksPage) {
        this.meta = metaDto;
        this.documents = booksPage.stream().map(BookDto::new).toList();
    }

    public RespBooksDto(JSONObject jsonObject){
        this.meta = (MetaDto) jsonObject.get("meta");
        this.documents = jsonObject.getJSONArray("documents").toList().stream().map(o -> (BookDto)o).toList();
    }


}