package com.scaling.libraryservice.mapBook.dto;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.scaling.libraryservice.mapBook.service.location.UserLibraryGeocoder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ReqMapBookDtoTest {

    @InjectMocks
    private UserLibraryGeocoder userLibraryGeocoder;

//    @Test
//    @DisplayName("ReqMapBookDto 생성")
//    void test_construct_MapBookDto() {
//        String isbn = "1111111111111";
//        Double lat = 38.0;
//        Double lon = 130.0;
//
//        ReqMapBookDto reqMapBookDto = new ReqMapBookDto(isbn, lat, lon);
//
//        Assertions.assertTrue(reqMapBookDto.getLat().equals(lat)
//            && reqMapBookDto.getLon().equals(lon) && reqMapBookDto.getIsbn().equals(isbn));
//    }
//
//
//    @Test
//    @DisplayName("한국에 있으면 true 반환")
//    void test_validate_in_Korea() {
//        /* given */
//        String isbn = "9788994142333";
//        Double lat = 37.5447;
//        Double lon = 126.9015;
//        ReqMapBookDto reqMapBookDto = new ReqMapBookDto(isbn, lat, lon);
//
//        /* when */
//        Executable e = () -> userLibraryGeocoder.resolve(reqMapBookDto);
//
//        /* then */
//
//        assertDoesNotThrow(e);
//
//    }
//
//
//    @Test
//    @DisplayName("한국에 없으면 false 반환")
//    void test_validate_not_in_Korea() {
//        /* given */
//        String isbn = "9788994142333";
//        Double lat = 37.5447;
//        Double lon = 126.9015;
//
//        /* when */
//        Executable e = () -> userLibraryGeocoder.resolve(new ReqMapBookDto(isbn,lat,lon));
//
//        /* then */
//
//        assertDoesNotThrow(e);
//
//    }

//    @Test
//    @DisplayName("지역정보를 담은 Dto를 통해 지원되는 지역인지 확인")
//    void test_setSupportedArea() {
//        boolean supported = true;
//        String isbn = "9788994142333";
//        Double lat = 37.5447;
//        Double lon = 126.9015;
//
//        ReqMapBookDto reqMapBookDto = new ReqMapBookDto(isbn, lat, lon);
//
//        reqMapBookDto.setSupportedArea(supported);
//
//        Assertions.assertTrue(reqMapBookDto.isHasBookSupport());
//
//    }
//
//    @Test
//    @DisplayName("isbn과 aseaCd에 대한 hash값 반환")
//    void test_hashCode() {
//        String isbn = "9788994142333";
//        Double lat = 37.5447;
//        Double lon = 126.9015;
//        ReqMapBookDto reqMapBookDto = new ReqMapBookDto(isbn, lat, lon);
//
//        int result = reqMapBookDto.hashCode();
//
//        System.out.println(result);
//    }
//
//    @Test
//    @DisplayName("지역코드 업데이트")
//    public void test_updateAreaCd() {
//        String isbn = "9788994142333";
//        Integer first_areaCd = 0;
//        ReqMapBookDto reqMapBookDto = ReqMapBookDto.builder().isbn(isbn).areaCd(first_areaCd)
//            .build();
//        Integer updated_areaCd = 6000;
//
//        reqMapBookDto.updateAreaCd(updated_areaCd);
//
//        Assertions.assertEquals(6000, reqMapBookDto.getAreaCd());
//
//    }
}
