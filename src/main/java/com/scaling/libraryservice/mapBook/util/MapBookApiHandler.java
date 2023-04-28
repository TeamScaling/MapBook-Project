package com.scaling.libraryservice.mapBook.util;

import com.scaling.libraryservice.aop.Timer;
import com.scaling.libraryservice.mapBook.apiConnection.BExistConnection;
import com.scaling.libraryservice.mapBook.dto.ApiBookExistDto;
import com.scaling.libraryservice.mapBook.dto.ApiStatus;
import com.scaling.libraryservice.mapBook.dto.LibraryDto;
import com.scaling.libraryservice.mapBook.dto.ReqMapBookDto;
import com.scaling.libraryservice.mapBook.dto.RespMapBookDto;
import com.scaling.libraryservice.mapBook.exception.OpenApiException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MapBookApiHandler {

    private final ApiQuerySender apiQuerySender;
    private final ApiQueryBinder apiQueryBinder;

    public void checkOpenApi() {

        BExistConnection bExistConn = new BExistConnection();
        ApiStatus status = BExistConnection.apiStatus;

        ResponseEntity<String> connection
            = apiQuerySender.singleQueryJson(bExistConn, "9788089365210");

        if (apiQueryBinder.bindBookExist(connection) == null) {
            status.closeAccess();
        } else {
            status.openAccess();
        }
    }

    private List<BExistConnection> getBExistConns(List<LibraryDto> libraries) {
        return libraries.stream()
            .map(n -> new BExistConnection(n.getLibNo())).toList();
    }

    public BExistConnection getBExist(Integer libNo) {

        return new BExistConnection(libNo);
    }


    @Timer
    public List<RespMapBookDto> matchMapBooks(List<LibraryDto> nearByLibraries,
        ReqMapBookDto reqMapBookDto) throws OpenApiException {

        Objects.requireNonNull(nearByLibraries);
        Objects.requireNonNull(reqMapBookDto);

        List<BExistConnection> bExistConnections = nearByLibraries.stream()
            .map(n -> new BExistConnection(n.getLibNo())).toList();

        List<ResponseEntity<String>> responseEntities = apiQuerySender.multiQuery(
            bExistConnections,
            reqMapBookDto.getIsbn(),
            nearByLibraries.size());

        Map<Integer, ApiBookExistDto> bookExistMap
            = apiQueryBinder.bindBookExistMap(responseEntities);

        // 대출 가능 응답 결과와 도서관 정보를 매칭하기 위한 내부 메소드.
        List<RespMapBookDto> result = new ArrayList<>();

        for (LibraryDto l : nearByLibraries) {

            ApiBookExistDto apiBookExistDto = bookExistMap.get(l.getLibNo());

            if (apiBookExistDto != null) {
                result.add(new RespMapBookDto(apiBookExistDto, l));
            }
        }

        return result;
    }

}
