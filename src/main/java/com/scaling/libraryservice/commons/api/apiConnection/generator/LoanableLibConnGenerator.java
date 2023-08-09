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

    @Override
    public List<LoanableLibConn> generateNecessaryConns(@NonNull List<LibraryInfoDto> nearByLibraries, ReqMapBookDto reqMapBookDto) {

        boolean isHasBookServiceSupport = nearByLibraries.stream()
            .anyMatch(LibraryInfoDto::isHasBookSupport);

        return nearByLibraries.isEmpty() ?
            Collections.emptyList() :
            nearByLibraries.stream()
                .filter(libraryDto -> !isHasBookServiceSupport || libraryDto.isHasBook())
                .map(libraryDto ->
                    new LoanableLibConn(
                        libraryDto.getLibNo(),
                        reqMapBookDto.getIsbn())
                ).toList();
    }

}
