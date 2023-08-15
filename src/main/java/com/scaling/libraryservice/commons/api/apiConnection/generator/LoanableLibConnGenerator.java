package com.scaling.libraryservice.commons.api.apiConnection.generator;

import com.scaling.libraryservice.commons.api.apiConnection.LoanableLibConn;
import com.scaling.libraryservice.mapBook.dto.LibraryInfoDto;
import com.scaling.libraryservice.mapBook.dto.ReqMapBookDto;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LoanableLibConnGenerator implements
    ConnectionGenerator<LoanableLibConn, LibraryInfoDto, ReqMapBookDto> {

    // 필요한 Api Connection 객체만을 만드는 메소드
    @Override
    public List<LoanableLibConn> generateNecessaryConns(@NonNull List<LibraryInfoDto> nearByLibraries,
        ReqMapBookDto reqMapBookDto) {

        // 내부 DB를 통한 도서관 소장 목록을 통한 전처리가 가능한 지역인지 판별
        boolean isHasBookServiceSupport = nearByLibraries.stream()
            .anyMatch(LibraryInfoDto::isHasBookSupport);

        return nearByLibraries.isEmpty() ?
            Collections.emptyList() : // 요청하는 도서관이 없을 경우 Connection 객체를 만들지 않는다
            nearByLibraries.stream()
                .filter(libraryDto -> !isHasBookServiceSupport || libraryDto.isHasBook())
                .map(libraryDto ->
                    new LoanableLibConn(
                        libraryDto.getLibNo(),
                        reqMapBookDto.getIsbn())
                ).toList();
    }

}
