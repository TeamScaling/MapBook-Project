package com.scaling.libraryservice.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.scaling.libraryservice.dto.BookApiDto;
import com.scaling.libraryservice.dto.RespBookMapDto;
import com.scaling.libraryservice.exception.OpenApiException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BookMapServiceTest {

    @Autowired
    private BookMapService bookMapService;


    @Test @DisplayName("해당 지역 도서관에서 해당 도서의 대출 가능여부 데이터 생성")
    public void exist_book_with_location(){
        /* given */

        String isbn = "9788994492032";
        String area = "성남";

        /* when */
        List<RespBookMapDto> result = bookMapService.queryExistLocation(isbn,area);

        /* then */

        result.forEach(System.out::println);
    }

    @Test @DisplayName("최종적으로 프론트 영역에 전달할 데이터 확인")
    public void respBookDto(){
        /* given */

        String isbn = "9788994492032";
        String area = "성남";

        /* when */
        List<RespBookMapDto> result = bookMapService.loanAbleLibrary(isbn,area);

        /* then */

        result.forEach(System.out::println);
    }

    @Test @DisplayName("open API 응답 중 error가 있을 때, 에러 처리")
    public void sendQuery_error_case(){
        /* given */
        String isbn = "9788994492032";
        int libCode = 1410;

        /* when */

        Executable executable = () -> bookMapService.sendQuery(isbn,libCode);

        /* then */
        assertThrows(OpenApiException.class,executable);
    }

    @Test @DisplayName("open API 서버에 문제가 있을 시")
    public void sendQuery_badReqeust(){
        /* given */
        String isbn = "9788994492032";
        int libCode = 1410;

        /* when */

        bookMapService.sendQuery(isbn,libCode);

        /* then */

    }


}