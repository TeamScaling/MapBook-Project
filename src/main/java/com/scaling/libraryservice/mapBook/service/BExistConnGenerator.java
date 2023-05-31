package com.scaling.libraryservice.mapBook.service;

import com.scaling.libraryservice.commons.api.apiConnection.BExistConn;
import com.scaling.libraryservice.mapBook.dto.LibraryDto;
import com.scaling.libraryservice.mapBook.dto.ReqMapBookDto;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BExistConnGenerator {

    public List<BExistConn> generateNecessaryConns(@NonNull List<LibraryDto> nearByLibraries,
        ReqMapBookDto reqMapBookDto) {

        if(nearByLibraries.isEmpty()){
            log.info(reqMapBookDto.getAreaCd() + " 이 지역에 관련 도서관이 없으므로 API 통신을 하지 않음");
            return Collections.emptyList();
        }

        if (reqMapBookDto.isSupportedArea()) {
            return nearByLibraries.stream().filter(LibraryDto::hasBook)
                .map(n -> new BExistConn(n.getLibNo(), reqMapBookDto.getIsbn())).toList();

        } else {
            return nearByLibraries.stream()
                .map(n -> new BExistConn(n.getLibNo(), reqMapBookDto.getIsbn())).toList();
        }
    }
}
