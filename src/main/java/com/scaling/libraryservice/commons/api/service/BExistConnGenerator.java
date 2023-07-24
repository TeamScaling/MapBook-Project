package com.scaling.libraryservice.commons.api.service;

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
public class BExistConnGenerator implements
    ConnectionGenerator<BExistConn, LibraryDto, ReqMapBookDto> {

    @Override
    public List<BExistConn> generateNecessaryConns(@NonNull List<LibraryDto> nearByLibraries,
        ReqMapBookDto reqMapBookDto) {

        if (nearByLibraries.isEmpty()) {
            log.info(reqMapBookDto.getAreaCd() + " 이 지역에 관련 도서관이 없으므로 API 통신을 하지 않음");
            return Collections.emptyList();
        }

        boolean isHasBookSupport = nearByLibraries.stream().anyMatch(LibraryDto::isHasBookSupport);

        return nearByLibraries.stream()
            .filter(libraryDto -> !isHasBookSupport || libraryDto.isHasBook())
            .map(libraryDto -> new BExistConn(libraryDto.getLibNo(), reqMapBookDto.getIsbn()))
            .toList();
    }

}
