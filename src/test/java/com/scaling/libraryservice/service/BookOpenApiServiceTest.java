package com.scaling.libraryservice.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.scaling.libraryservice.dto.BookApiDto;
import com.scaling.libraryservice.dto.RespBookMapDto;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BookOpenApiServiceTest {

    @Autowired
    private BookOpenApiService bookOpenApiService;

    @Test
    @DisplayName("book api 작동 제대로 하는지")
    public void api_basic_check(){
        /* given */

        String isbn = "9788994492032";

        /* when */

        BookApiDto result = bookOpenApiService.sendQuery(isbn,"141053");

        /* then */

        assertNotNull(result);
        System.out.println(result);
    }

    @Test @DisplayName("성남 도서관에 특정 도서가 있는지 확인")
    public void exist_book_with_location(){
        /* given */

        String isbn = "9788994492032";
        String area = "성남";

        /* when */ //10개
        List<BookApiDto> result = bookOpenApiService.queryExistLocation(isbn,area);

        /* then */

        result.forEach(System.out::println);
    }

    @Test @DisplayName("최종적으로 프론트 영역에 전달할 데이터 확인")
    public void respBookDto(){
        /* given */

        String isbn = "9788994492032";
        String area = "성남";

        /* when */ //10개
        List<RespBookMapDto> result = bookOpenApiService.getMarkerData(isbn,area);

        /* then */

        result.forEach(System.out::println);
    }

    @Test
    public void executorQuery() throws ExecutionException, InterruptedException {
        /* given */
        String isbn = "9788994492032";
        String area = "성남";

        /* when */

        /* then */

        System.out.println(bookOpenApiService.queryExistLocation(isbn,area));
    }


}