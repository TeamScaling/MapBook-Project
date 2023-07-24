package com.scaling.libraryservice.mapBook.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.scaling.libraryservice.mapBook.dto.LibraryDto;
import com.scaling.libraryservice.mapBook.dto.ReqMapBookDto;
import com.scaling.libraryservice.mapBook.entity.HsAreaCd;
import com.scaling.libraryservice.mapBook.entity.Library;
import com.scaling.libraryservice.mapBook.repository.HasBookAreaRepository;
import com.scaling.libraryservice.mapBook.repository.LibraryHasBookRepository;
import com.scaling.libraryservice.mapBook.repository.LibraryRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LibraryFindServiceTest {

    @InjectMocks
    private LibraryFindService libraryFindService;

    @Mock
    private LibraryHasBookRepository libraryHasBookRepo;

    @Mock
    private HasBookAreaRepository hasBookAreaRepo;

    @Mock
    private LibraryRepository libraryRepo;

    String isbn13 = "9788089365210";
    int areaCd = 26000;

    @BeforeEach
    void setUp() {
    }

    @Test @DisplayName("소장 서비스 지역일 때 소장하고 있는 도서관 목록을 반환 할 수 있다")
    public void getNearByLibraries_SupportedArea(){
        /* given */

        Library library1 = Library.builder().libNm("1").areaCd(areaCd).build();
        Library library2 = Library.builder().libNm("2").areaCd(areaCd).build();

        List<Library> libraries = List.of(library1, library2);

        Optional<HsAreaCd> optional = Optional.of(new HsAreaCd());

        when(hasBookAreaRepo.findById(areaCd)).thenReturn(optional);
        when(libraryHasBookRepo.findHasBookLibraries(isbn13, areaCd)).thenReturn(libraries);

        /* when */

        var result = libraryFindService.getNearByLibraries(isbn13,areaCd);

        /* then */
        assertEquals(2,result.size());
        assertTrue(result.stream().allMatch(LibraryDto::isHasBook));
        assertTrue(result.stream().allMatch(LibraryDto::isHasBookSupport));
    }

    @Test @DisplayName("소장 서비스 지역이 아닐 때 주변 도서관 목록을 반환 할 수 있다")
    public void getNearByLibraries_Not_SupportedArea(){
        /* given */

        String isbn13 = "9788089365210";
        int areaCd = 26000;

        Library library1 = Library.builder().libNm("1").areaCd(areaCd).build();
        Library library2 = Library.builder().libNm("2").areaCd(areaCd).build();

        List<Library> libraries = List.of(library1, library2);

        Optional<HsAreaCd> optional = Optional.empty();

        when(hasBookAreaRepo.findById(26000)).thenReturn(optional);
        when(libraryRepo.findAllByAreaCd(areaCd)).thenReturn(libraries);

        /* when */

        var result = libraryFindService.getNearByLibraries(isbn13,26000);

        /* then */
        assertEquals(2,result.size());
        assertFalse(result.stream().allMatch(LibraryDto::isHasBook));
        assertFalse(result.stream().allMatch(LibraryDto::isHasBookSupport));
    }


    @Test
    void getNearByHasBookLibraries_when_hasBookLibraries_true() {
        /* given */

        ReqMapBookDto reqMapBookDto = ReqMapBookDto.builder().isbn("1234").areaCd(123).build();

        Library library1 = Library.builder().libNm("1").build();
        Library library2 = Library.builder().libNm("2").build();

        List<Library> libraries = List.of(library1, library2);

        when(libraryHasBookRepo.findHasBookLibraries("1234",123)).thenReturn(libraries);


        /* when */
        var result = libraryFindService.getNearByHasBookLibraries("1234",123);

        /* then */

        assertEquals(result.size(),2);
        assertTrue(result.stream().allMatch(LibraryDto::isHasBook));
    }

    @Test
    void getNearByHasBookLibraries_when_hasBookLibraries_false() {
        /* given */

        String isbn13 = "123456789";
        int areaCd = 123;

        ReqMapBookDto reqMapBookDto = ReqMapBookDto.builder().isbn(isbn13).areaCd(areaCd).build();

        Library library1 = Library.builder().libNm("1").build();
        Library library2 = Library.builder().libNm("2").build();

        List<Library> libraries = List.of(library1, library2);

        when(libraryHasBookRepo.findHasBookLibraries(isbn13,areaCd)).thenReturn(Collections.emptyList());

        /* when */
        var result = libraryFindService.getNearByHasBookLibraries(isbn13,areaCd);

        /* then */
        assertEquals(result.size(),0);
    }

}