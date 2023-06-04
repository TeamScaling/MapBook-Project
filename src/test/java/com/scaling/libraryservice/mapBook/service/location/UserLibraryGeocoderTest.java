package com.scaling.libraryservice.mapBook.service.location;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.scaling.libraryservice.mapBook.dto.ReqMapBookDto;
import com.scaling.libraryservice.mapBook.entity.Library;
import com.scaling.libraryservice.mapBook.exception.LocationException;
import com.scaling.libraryservice.mapBook.repository.LibraryRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserLibraryGeocoderTest {

    @InjectMocks
    private UserLibraryGeocoder geocoder;

    @Mock
    private LibraryRepository libraryRepo;

    private ReqMapBookDto reqMapBookDto;

    private List<Library> libraries;

    int areaCd1 = 9000; //송파구
    int areaCd2 = 6000; //강서구
    int areaCd3 = 27700; // 동작구

    @BeforeEach
    void setUp() {
        //송파구 거주하는 사용자
        double lat = 37.4935096;
        double lon = 127.1468394;
        reqMapBookDto = ReqMapBookDto.builder().lat(lat).lon(lon).build();

        //각 지역별 도서관
        Library library1 = Library.builder().libNm("강서구 도서관").areaCd(areaCd2).libLat(37.5473333)
            .libLon(126.8731625)
            .build();

        Library library2 = Library.builder().libNm("송파구 도서관").areaCd(areaCd1).libLat(37.5272233)
            .libLon(127.1177482).build();

        Library library3 = Library.builder().libNm("동작구 도서관").areaCd(areaCd3).libLat(37.4978365)
            .libLon(126.9474396)
            .build();

        //도서관 정보를 담는 list
        libraries = List.of(library1, library2, library3);
    }

    @Test
    @DisplayName("사용자의 위도/경도를 기반으로 가장 가까운 도서관을 도출 할 수 있다.")
    void resolve() {
        /* given */

        when(libraryRepo.findAll()).thenReturn(libraries);

        /* when */

        int result = geocoder.resolve(reqMapBookDto);

        /* then */

        assertEquals(result, areaCd1);
    }

    @Test
    @DisplayName("resolve 메소드가 호출 되지 않으면 지역 코드는 업데이트 되지 않는다.")
    void not_resolve_not_update_areaCd() {
        /* given */

        /* when */

        var result = reqMapBookDto.getAreaCd();

        /* then */
        assertEquals(result, 0);
    }

}