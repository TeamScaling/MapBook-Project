package com.scaling.libraryservice.mapBook.service;

import com.scaling.libraryservice.commons.api.apiConnection.LoanableLibConn;
import com.scaling.libraryservice.commons.api.apiConnection.generator.ConnectionGenerator;
import com.scaling.libraryservice.commons.api.service.provider.DataProvider;
import com.scaling.libraryservice.commons.caching.aop.CustomCacheable;
import com.scaling.libraryservice.commons.timer.MeasureTaskTime;
import com.scaling.libraryservice.mapBook.dto.ApiLoanableLibDto;
import com.scaling.libraryservice.mapBook.dto.LibraryInfoDto;
import com.scaling.libraryservice.mapBook.dto.ReqMapBookDto;
import com.scaling.libraryservice.mapBook.dto.RespMapBookDto;
import com.scaling.libraryservice.mapBook.dto.RespMapBookWrapper;
import com.scaling.libraryservice.mapBook.exception.OpenApiException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 사용자가 원하는 도서와 사용자 주변의 도서관을 연결하여 대출 가능한 도서관의 정보를 제공하는 서비스 클래스입니다.
 *
 * <p>이 클래스는 {@link DataProvider< ApiLoanableLibDto >}를 사용하여 도서의 대출 가능 여부 데이터를 가져옵니다.</p>
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class MapBookService implements ApiRelatedService {

    private final DataProvider<ApiLoanableLibDto> dataProvider;

    private final ConnectionGenerator<LoanableLibConn, LibraryInfoDto, ReqMapBookDto> connGenerator;

    @MeasureTaskTime // 시간을 측정하여 기록하기 위한 wrapper 메소드
    public RespMapBookWrapper getLoanableMarker(List<LibraryInfoDto> nearByLibraries,
        ReqMapBookDto reqMapBookDto) {

        List<RespMapBookDto> respMapBooks = matchLibraryBooks(
            nearByLibraries,
            reqMapBookDto
        );
        return new RespMapBookWrapper(respMapBooks, reqMapBookDto);
    }

    /**
     * 사용자가 원하는 도서와 사용자 주변의 도서관을 조합하여 대출 가능한 도서관들의 정보를 반환 한다.
     *
     * @param nearByLibraries 사용자 주변의 도서관 정보 Dto List
     * @param reqMapBookDto   사용자가 요청한 도서 정보와 위치 정보를 담는 Dto
     * @return 대출 가능한 도서관 정보를 담은 응답 Dto를 List 형태로 반환 한다.
     */
    @CustomCacheable
    List<RespMapBookDto> matchLibraryBooks(List<LibraryInfoDto> nearByLibraries,
        ReqMapBookDto reqMapBookDto) throws OpenApiException {

        // 해당 도서를 소장하는 도서관에 한정해서 Api 연결 객체를 만든다.
        List<LoanableLibConn> loanableLibConns = connGenerator.generateNecessaryConns(
            nearByLibraries,
            reqMapBookDto
        );

        // 대출 가능 데이터를 전달 받는다.
        List<ApiLoanableLibDto> apiResults = dataProvider.provideDataList(
            loanableLibConns,
            loanableLibConns.size()
        );

        return loanableLibConns.isEmpty() ?
            notFoundLoanableLib(nearByLibraries, reqMapBookDto) :
            mappingLoanableLib(nearByLibraries, apiResults);
    }

    private List<RespMapBookDto> notFoundLoanableLib(List<LibraryInfoDto> nearByLibraries,
        ReqMapBookDto reqMapBookDto) {

        return nearByLibraries.stream()
            .map(libraryDto -> new RespMapBookDto(reqMapBookDto, libraryDto, false))
            .toList();
    }


    /**
     * 대출 가능 Api 응답을 주변 도서관 정보와 연결 하여 대출 가능한 주변 도서관 정보를 담은 List를 반환 한다.
     *
     * @param nearByLibraries 사용자 주변 도서관 정보를 담은 Dto
     * @param apiResults      api로 부터 응답 받은 해당 도서의 대출 가능 여부 데이터를 담은 Dto List
     * @return 대출 가능한 주변 도서관 정보에 대한 Dto List
     */

    private List<RespMapBookDto> mappingLoanableLib(List<LibraryInfoDto> nearByLibraries,
        List<ApiLoanableLibDto> apiResults) {

        Map<Integer, ApiLoanableLibDto> loanableLibMap = collectLoanableListToMap(apiResults);

        return nearByLibraries.stream()
            .map(libraryDto -> createRespMapBook(loanableLibMap, libraryDto))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
    }

    private Optional<RespMapBookDto> createRespMapBook(
        Map<Integer, ApiLoanableLibDto> loanableBookMap,
        LibraryInfoDto libraryInfoDto) {

        return Optional.ofNullable(loanableBookMap.get(libraryInfoDto.getLibNo()))
            .map(apiLoanableLibDto ->
                RespMapBookDto.responseWithLoanable(
                    apiLoanableLibDto,
                    libraryInfoDto)
            );
    }


    /**
     * API에서 받은 도서 대출 가능 여부 데이터를 Map 형태로 변환하여 반환합니다.
     *
     * <p>대출 가능한 도서만 Map에 추가되며, Map의 키는 도서관 코드, 값은 도서 대출 가능 여부 데이터입니다.</p>
     *
     * @param apiResults API로부터 받은 도서 대출 가능 여부 데이터 리스트
     * @return 도서관 코드를 키로, 도서 대출 가능 여부 데이터를 값으로 가지는 Map
     */
    private Map<Integer, ApiLoanableLibDto> collectLoanableListToMap(
        List<ApiLoanableLibDto> apiResults) {

        return apiResults.stream()
            .filter(ApiLoanableLibDto::isLoanAble)
            .collect(Collectors.toMap(
                apiLoanableLib -> Integer.valueOf(apiLoanableLib.getLibCode()),
                Function.identity(),
                (existingKey, newKey) -> existingKey
            ));
    }

}
