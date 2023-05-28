package com.scaling.libraryservice.commons.updater.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.scaling.libraryservice.commons.updater.repository.BookUpdateRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class BookUpdateServiceTest {

    @Autowired
    private BookUpdateService bookUpdateService;

    @Autowired
    private BookUpdateRepository bookUpdateRepo;

    @Test @DisplayName("도서 업데이트 메소드 실행 성공")
    @Transactional
    public void test_update(){
        /* given */

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


    @Test @DisplayName("도서 업데이트 메소드 실행. 테스트 메소드가 아닌 실행을 위한 메소드")
    public void execute_update(){

        bookUpdateService.UpdateBookFromApi(10000,100);
    }
}