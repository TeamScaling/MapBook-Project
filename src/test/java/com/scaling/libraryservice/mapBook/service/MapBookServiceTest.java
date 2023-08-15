package com.scaling.libraryservice.mapBook.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import com.scaling.libraryservice.commons.api.apiConnection.LoanableLibConn;
import com.scaling.libraryservice.commons.api.apiConnection.generator.ConnectionGenerator;
import com.scaling.libraryservice.commons.api.apiConnection.generator.LoanableLibConnGenerator;
import com.scaling.libraryservice.commons.api.service.provider.DataProvider;
import com.scaling.libraryservice.commons.api.service.provider.LoanableLibProvider;
import com.scaling.libraryservice.mapBook.dto.ApiLoanableLibDto;
import com.scaling.libraryservice.mapBook.dto.LibraryInfoDto;
import com.scaling.libraryservice.mapBook.dto.ReqMapBookDto;
import com.scaling.libraryservice.mapBook.dto.RespMapBookDto;
import com.scaling.libraryservice.mapBook.entity.LibraryInfo;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MapBookServiceTest {

    @InjectMocks
    private MapBookService mapBookService;

    @Mock
    private DataProvider<ApiLoanableLibDto> dataProvider;

    @Mock
    private LibraryInfo libraryInfo;

    @Mock
    private ReqMapBookDto reqMapBookDto;

    @Mock
    private List<LoanableLibConn> loanableLibConns;

    private ConnectionGenerator<LoanableLibConn, LibraryInfoDto, ReqMapBookDto> connectionGenerator = new LoanableLibConnGenerator();

    @BeforeEach
    public void setUP() {

        mapBookService = new MapBookService(dataProvider,connectionGenerator);
    }

//    @Test @DisplayName("소장하지 않는 도서관에 대해선 빌릴 수 없는 상태로 결과를 반환 한다")
//    void matchMapBooks_when_bExistConns_isEmpty() {
//        /* given */
//
//        LibraryInfoDto library1 = LibraryInfoDto.builder().hasBook(false).isHasBookSupport(true).libNo(1).build();
//        LibraryInfoDto library2 = LibraryInfoDto.builder().hasBook(true).isHasBookSupport(true).libNo(2).build();
//
//        List<LibraryInfoDto> libraries = Arrays.asList(library1, library2);
//        /* when */
//
//        var result = mapBookService.matchLibraryBooks(libraries, reqMapBookDto);
//
//        System.out.println(result);
//
//        RespMapBookDto respMapBookDto1 = result.get(0);
//        RespMapBookDto respMapBookDto2 = result.get(1);
//
//        /* then */
//
//        assertFalse(respMapBookDto1.isAvailable());
//        assertTrue(respMapBookDto2.isAvailable());
//    }

    @Test
    void matchMapBooks_when_bExistConns_non_isEmpty() {
        /* given */

        LibraryInfoDto library1 = LibraryInfoDto.builder().hasBook(true).isHasBookSupport(false).libNo(1).build();
        LibraryInfoDto library2 = LibraryInfoDto.builder().hasBook(false).isHasBookSupport(false).libNo(2).build();

        List<LibraryInfoDto> libraries = List.of(library1, library2);

        ApiLoanableLibDto apiLoanableLibDto1 = ApiLoanableLibDto.builder().libCode("1").loanAvailable("Y")
            .build();
        ApiLoanableLibDto apiLoanableLibDto2 = ApiLoanableLibDto.builder().libCode("2").loanAvailable("Y")
            .build();

        List<ApiLoanableLibDto> bookExists = List.of(apiLoanableLibDto1, apiLoanableLibDto2);


        /* when */

        var result = mapBookService.matchLibraryBooks(libraries, reqMapBookDto);

        /* then */

        System.out.println(result);

        assertTrue(result.stream().allMatch(RespMapBookDto::isAvailable));
    }

}