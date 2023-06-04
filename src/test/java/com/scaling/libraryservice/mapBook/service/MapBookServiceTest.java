package com.scaling.libraryservice.mapBook.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.scaling.libraryservice.commons.api.apiConnection.BExistConn;
import com.scaling.libraryservice.commons.api.service.provider.DataProvider;
import com.scaling.libraryservice.mapBook.dto.ApiBookExistDto;
import com.scaling.libraryservice.mapBook.dto.LibraryDto;
import com.scaling.libraryservice.mapBook.dto.ReqMapBookDto;
import com.scaling.libraryservice.mapBook.dto.RespMapBookDto;
import com.scaling.libraryservice.mapBook.entity.Library;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
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
    private DataProvider<ApiBookExistDto> dataProvider;

    @Mock
    private Library library;

    @Mock
    private ReqMapBookDto reqMapBookDto;

    @Mock
    private List<BExistConn> bExistConns;

    @BeforeEach
    public void setUP() {

        mapBookService = new MapBookService(dataProvider);
    }

    @Test
    void matchMapBooks_when_bExistConns_isEmpty() {
        /* given */

        LibraryDto library1 = LibraryDto.builder().hasBook("Y").isHasBookSupport(true).libNo(1).build();
        LibraryDto library2 = LibraryDto.builder().hasBook("Y").isHasBookSupport(true).libNo(2).build();

        List<LibraryDto> libraries = Arrays.asList(library1, library2);

        when(bExistConns.isEmpty()).thenReturn(true);

        /* when */

        var result = mapBookService.matchLibraryBooks(bExistConns, libraries, reqMapBookDto);

        /* then */

        assertFalse(result.stream().allMatch(RespMapBookDto::getAvailable));
    }

    @Test
    void matchMapBooks_when_bExistConns_non_isEmpty() {
        /* given */

        LibraryDto library1 = LibraryDto.builder().hasBook("Y").isHasBookSupport(false).libNo(1).build();
        LibraryDto library2 = LibraryDto.builder().hasBook("Y").isHasBookSupport(false).libNo(2).build();

        List<LibraryDto> libraries = List.of(library1, library2);

        ApiBookExistDto apiBookExistDto1 = ApiBookExistDto.builder().libCode("1").loanAvailable("Y")
            .build();
        ApiBookExistDto apiBookExistDto2 = ApiBookExistDto.builder().libCode("2").loanAvailable("Y")
            .build();

        List<ApiBookExistDto> bookExists = List.of(apiBookExistDto1, apiBookExistDto2);

        when(bExistConns.isEmpty()).thenReturn(false);
        when(dataProvider.provideDataList(bExistConns,10)).thenReturn(bookExists);

        /* when */

        var result = mapBookService.matchLibraryBooks(bExistConns, libraries, reqMapBookDto);

        /* then */

        assertTrue(result.stream().allMatch(RespMapBookDto::getAvailable));
    }


    @Test
    void mappingLoanableLib() {
        /* given */

        /* when */

        /* then */
    }

    @Test
    void changeToMap() {
        /* given */

        /* when */

        /* then */
    }
}