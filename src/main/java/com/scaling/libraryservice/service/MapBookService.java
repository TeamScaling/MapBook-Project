package com.scaling.libraryservice.service;

import com.scaling.libraryservice.aop.Timer;
import com.scaling.libraryservice.dto.ApiBookExistDto;
import com.scaling.libraryservice.dto.LibraryDto;
import com.scaling.libraryservice.dto.RespMapBookDto;
import com.scaling.libraryservice.exception.OpenApiException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MapBookService {

    @Transactional(readOnly = true)
    @Timer
    public List<RespMapBookDto> getMapBooks(List<LibraryDto> nearByLibraries,
        List<ResponseEntity<String>> loanableLibraries) throws OpenApiException {

        Objects.requireNonNull(nearByLibraries);
        Objects.requireNonNull(loanableLibraries);

        // 매개 변수로 받은 대출 가능 응답 결과 데이터를 파싱하기 위해 내부 메소드에 위임한다.
        Map<Integer, ApiBookExistDto> respOpenApiDtoMap
            = getBookExistMap(loanableLibraries);


        return matchLoanableLibraries(nearByLibraries,respOpenApiDtoMap);
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

    @Timer //Http 응답 결과를 필요한 객체로 Mapping 하고, 도서관 코드를 key로 하는 Map으로 담는다.
    private Map<Integer, ApiBookExistDto> getBookExistMap(
        List<ResponseEntity<String>> apiResponse) throws OpenApiException {

        Objects.requireNonNull(apiResponse);

        Map<Integer, ApiBookExistDto> respOpenApiDtoMap = new HashMap<>();

        for (ResponseEntity<String> r : apiResponse) {

            JSONObject respJsonObj =
                new JSONObject(r.getBody()).getJSONObject("response");

            if (respJsonObj.has("error")) {
                String error = respJsonObj.getString("error");
                log.info("[data4library bookExist API error] message :" + error);

                throw new OpenApiException(error);
            }

            ApiBookExistDto apiBookExistDto = new ApiBookExistDto(
                respJsonObj.getJSONObject("request")
                , respJsonObj.getJSONObject("result"));

            respOpenApiDtoMap.put(Integer.valueOf(apiBookExistDto.getLibCode()),
                apiBookExistDto);
        }

        return respOpenApiDtoMap;
    }

    //consider : 같은 지역, 같은 책을 검색 할 때, 캐쉬를 이용하여 API 호출을 하지 않는다.
    /*private List<LibraryDto> getCachedMapBooks(String isbn, String area) {


        if (libraryCache.containsKey(area)) {

            log.info("cache hit !!");
            return libraryCache.get(area);

        } else {

            List<LibraryDto> libraryList
                = libraryRepo.findAroundLibrary(oneArea, twoArea)
                .stream().map(LibraryDto::new).toList();

            libraryCache.put(area, libraryList);
            log.info("no library in cache !!");

            return libraryList;
        }
    }*/

}
