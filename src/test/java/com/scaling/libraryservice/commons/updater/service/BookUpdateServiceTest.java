package com.scaling.libraryservice.commons.updater.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;

import com.scaling.libraryservice.commons.api.service.KakaoBookProvider;
import com.scaling.libraryservice.commons.updater.dto.BookApiDto;
import com.scaling.libraryservice.commons.updater.entity.UpdateBook;
import com.scaling.libraryservice.commons.updater.repository.BookUpdateRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookUpdateServiceTest {

    @InjectMocks
    private BookUpdateService bookUpdateService;

    @Mock
    private BookUpdateRepository bookUpdateRepo;

    @Mock
    private KakaoBookProvider kakaoBookProvider;

    @Test @DisplayName("도서 업데이트 메소드 실행 성공")
    public void test_update(){
        /* given */

        UpdateBook updateBook1 = UpdateBook.builder().isbn("1").id(1L).build();
        UpdateBook updateBook2 = UpdateBook.builder().isbn("2").id(2L).build();
        UpdateBook updateBook3 = UpdateBook.builder().isbn("3").id(3L).build();

        List<UpdateBook> nonUpdateBooks = List.of(updateBook1,updateBook2,updateBook3);

        BookApiDto bookApiDto1 = BookApiDto.builder().isbn("1").title("업데이트").build();
        BookApiDto bookApiDto2 = BookApiDto.builder().isbn("2").title("업데이트").build();
        BookApiDto bookApiDto3 = BookApiDto.builder().isbn("3").title("업데이트").build();

        List<BookApiDto> apiBooks = List.of(bookApiDto1,bookApiDto2,bookApiDto3);

        when(bookUpdateRepo.findBooksWithLimit(anyInt())).thenReturn(nonUpdateBooks);
        when(kakaoBookProvider.provideDataList(any(),anyInt())).thenReturn(apiBooks);


        /* when */

        bookUpdateService.UpdateBookFromApi(3,10);

        /* then */

        assertEquals(updateBook1.getTitle(),"업데이트");
        assertEquals(updateBook2.getTitle(),"업데이트");
        assertEquals(updateBook3.getTitle(),"업데이트");
    }



}