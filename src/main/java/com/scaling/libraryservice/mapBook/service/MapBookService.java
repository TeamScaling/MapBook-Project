package com.scaling.libraryservice.mapBook.service;

import com.scaling.libraryservice.aop.Timer;
import com.scaling.libraryservice.mapBook.dto.ApiBookExistDto;
import com.scaling.libraryservice.mapBook.dto.LibraryDto;
import com.scaling.libraryservice.mapBook.dto.RespMapBookDto;
import com.scaling.libraryservice.mapBook.exception.OpenApiException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MapBookService {

    @Transactional(readOnly = true) @Timer
    public List<RespMapBookDto> getMapBooks(List<LibraryDto> nearByLibraries,
        Map<Integer, ApiBookExistDto> respOpenApiDtoMap) throws OpenApiException {

        Objects.requireNonNull(nearByLibraries);
        Objects.requireNonNull(respOpenApiDtoMap);

        return matchLoanableLibraries(nearByLibraries, respOpenApiDtoMap);
    }

    // 대출 가능 응답 결과와 도서관 정보를 매칭하기 위한 내부 메소드.
    private List<RespMapBookDto> matchLoanableLibraries(List<LibraryDto> nearByLibraries,
        Map<Integer, ApiBookExistDto> respOpenApiDtoMap) {

        List<RespMapBookDto> result = new ArrayList<>();

        for (LibraryDto l : nearByLibraries) {

            ApiBookExistDto apiBookExistDto = respOpenApiDtoMap.get(l.getLibNo());

            if (apiBookExistDto != null) {
                result.add(new RespMapBookDto(apiBookExistDto, l));
            }
        }

        return result;
    }

}
