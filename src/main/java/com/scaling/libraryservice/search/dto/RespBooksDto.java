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
//
//    public static RespBooksDto defaultRespBooksDto(Page<BookDto> books,ReqBookDto reqBookDto){
//        return new RespBooksDto(new MetaDto(books, reqBookDto), books);
//    }
//
//    public static RespBooksDto emptyRespBookDto(){
//
//        return new RespBooksDto(MetaDto.emptyDto(), Collections.emptyList());
//    }
//
//    public static RespBooksDto isbnRespBookDto(BookDto bookDto){
//
//        return new RespBooksDto(MetaDto.isbnMetaDto(),List.of(bookDto));
//    }
//
//    public static RespBooksDto sessionRespBookDto(MetaDto metaDto,BookDto bookDto){
//
//        return new RespBooksDto(MetaDto.sessionMetaDto(metaDto.getSearchTime()),List.of(bookDto));
//    }
//
//    public static RespBooksDto oneBookRespDto(MetaDto metaDto, BookDto bookDto){
//        return new RespBooksDto(MetaDto.oneMetaDto(), List.of(bookDto));
//    }

    public boolean isEmptyResult(){
        return this.meta.getTotalElements() == 0;
    }

    @Override
    public void addMeasuredTime(String time) {
        this.getMeta().addSearchTime(time);
    }
}