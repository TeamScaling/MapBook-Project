package com.scaling.libraryservice.mapBook.controller;

import com.scaling.libraryservice.commons.api.apiConnection.LoanableLibConn;
import com.scaling.libraryservice.commons.circuitBreaker.ApiMonitoring;
import com.scaling.libraryservice.logging.logger.MapBookLogger;
import com.scaling.libraryservice.mapBook.dto.LibraryInfoDto;
import com.scaling.libraryservice.mapBook.dto.ReqMapBookDto;
import com.scaling.libraryservice.mapBook.dto.RespMapBookDto;
import com.scaling.libraryservice.commons.api.service.ConnectionGenerator;
import com.scaling.libraryservice.mapBook.dto.RespMapBookWrapper;
import com.scaling.libraryservice.mapBook.service.LibraryFindService;
import com.scaling.libraryservice.mapBook.service.MapBookService;
import com.scaling.libraryservice.mapBook.service.location.LocationResolver;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MapBookController {

    private final MapBookService mapBookService;
    private final LibraryFindService libraryFindService;
    private final ConnectionGenerator<LoanableLibConn, LibraryInfoDto,ReqMapBookDto> connGenerator;
    private final LocationResolver<Integer,ReqMapBookDto> locationResolver;
    private final MapBookLogger mapBookLogger;


    /**
     * 사용자의 요청에 맞는 대출 가능 도서관 마커 데이터를 model에 담아 view에 전달 합니다
     *
     * @param model         View에 전달 할 marker 데이터를 담는 Model 객체
     * @param reqMapBookDto 요청 받은 도서와 사용자의 위치 정보가 담겨 있는 Dto
     * @return Model을 전달 받고 View를 구성 할 html 파일 이름
     */

    @PostMapping("/books/mapBook/search")
    @ApiMonitoring(api = LoanableLibConn.class, substitute = "fallBackMethodHasBook")
    public String getMapBooks(ModelMap model, @RequestBody ReqMapBookDto reqMapBookDto) {

        // 사용자의 위/경도 데이터로 해당 지역 코드를 산출
        Integer areaCd = locationResolver.resolve(reqMapBookDto);

        // 사용자 주변의 도서관을 찾는다.
        List<LibraryInfoDto> nearbyLibraries = libraryFindService.getNearByLibInfoByAreaCd(
            reqMapBookDto.getIsbn(),
            areaCd
        );

        // 해당 도서를 소장하는 도서관에 한정해서 Api 연결 객체를 만든다.
        List<LoanableLibConn> necessaryConns = connGenerator.generateNecessaryConns(
            nearbyLibraries,
            reqMapBookDto
        );

        // Api의 응답 결과와 도서관의 상세 정보를 연결하여, 지도에 표시할 마커를 생성한다.
        RespMapBookWrapper mapBooks = mapBookService.getLoanableMarker(
            necessaryConns,
            nearbyLibraries,
            reqMapBookDto
        );

        mapBookLogger.sendLogToSlack(mapBooks);

        model.put("mapBooks", mapBooks.getRespMapBooks());

        return "mapBook/mapBookMarker";
    }

    @GetMapping("/books/mapBook/loading")
    public String loadingPage(){
        return "mapBook/loading";
    }

    /**
     * Circuit breaker 판단에 의해 필요시 getMapBooks method 대신 호출 합니다
     *
     * @param model         View에 전달 할 소장 도서관 마커 데이터를 담는 Model 객체
     * @param reqMapBookDto getMapBooks moethod에게 전달 받은 사용자 요청 데이터가 담긴 Dto
     * @return Model을 전달 받고 View를 구성 할 html 파일 이름
     */
    public String fallBackMethodHasBook(ModelMap model,
        @ModelAttribute ReqMapBookDto reqMapBookDto) {

        Integer areaCd = locationResolver.resolve(reqMapBookDto);
        
        // 내부 DB의 도서관이 소장하는 도서 데이터를 바탕으로 사용자가 찾는 책을 소장하는 도서관 정보를 반환 한다.
        List<RespMapBookDto> hasBookLibs = libraryFindService.getHasBookLibraries(
            reqMapBookDto,
            areaCd
        );

        model.put("hasBookLibs", hasBookLibs);

        return "mapBook/hasLibMarker";
    }
}