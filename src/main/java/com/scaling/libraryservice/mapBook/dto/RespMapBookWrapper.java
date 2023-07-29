package com.scaling.libraryservice.mapBook.dto;

import com.scaling.libraryservice.commons.timer.TimeMeasurable;
import java.util.List;
import lombok.Getter;
import org.springframework.lang.Nullable;

@Getter
public class RespMapBookWrapper implements TimeMeasurable<String> {

    private final List<RespMapBookDto> respMapBooks;

    private final String bookTitle;

    private final int areaCd;

    @Nullable
    private String taskTime;

    public RespMapBookWrapper(List<RespMapBookDto> respMapBooks, String bookTitle, int areaCd) {
        this.respMapBooks = respMapBooks;
        this.bookTitle = bookTitle;
        this.areaCd = areaCd;
    }

    public RespMapBookWrapper(List<RespMapBookDto> respMapBooks, ReqMapBookDto reqMapBookDto){
        this.respMapBooks = respMapBooks;
        this.bookTitle = reqMapBookDto.getTitle();
        this.areaCd = reqMapBookDto.getAreaCd();
    }

    @Override
    public void addMeasuredTime(String time) {
        this.taskTime = time;
    }
}
