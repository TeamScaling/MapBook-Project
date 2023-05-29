package com.scaling.libraryservice.commons.updater.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;

import com.scaling.libraryservice.commons.updater.entity.UpdateBook;
import com.scaling.libraryservice.commons.updater.repository.BookUpdateRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(MockitoExtension.class)
class BookUpdateServiceTest {

    @InjectMocks
    private BookUpdateService bookUpdateService;

    @Mock
    private BookUpdateRepository bookUpdateRepo;

    @Mock
    private List<UpdateBook> nonUpdateBooks;

    @Test @DisplayName("도서 업데이트 메소드 실행 성공")
    @Transactional
    public void test_update(){
        /* given */

        when(bookUpdateRepo.findBooksWithLimit(anyInt())).thenReturn(nonUpdateBooks);

        var nonUpdateBooks= bookUpdateRepo.findBooksWithLimit(100);

        var nonUpdateBookId = nonUpdateBooks.get(0).getId();

        /* when */

        bookUpdateService.UpdateBookFromApi(100,10);

        /* then */

        var updatedBook= bookUpdateRepo.findById(nonUpdateBookId);

        if(updatedBook.isPresent()){

            var updateBook = updatedBook.get();

            assertNotNull(updateBook.getTitle());
        }
    }


    @DisplayName("도서 업데이트 메소드 실행. 테스트 메소드가 아닌 실행을 위한 메소드")
    public void execute_update(){

        bookUpdateService.UpdateBookFromApi(19900,100);
    }

    @Test
    void updateBookFromApi() {
    }
}