package com.scaling.libraryservice.commons.updater.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookUpdateRepositoryTest {

    @Autowired
    private BookUpdateRepository bookUpdatedRepo;

    @Test @DisplayName("가장 기본적인 findById 메소드가 정상적으로 실행")
    public void find_entity(){
        /* given */

        long id = 443L;

        /* when */
        Executable e = () -> bookUpdatedRepo.findById(id);
    
        /* then */

        assertDoesNotThrow(e);

    }

    @Test @DisplayName("find_Books_with_limit 메소드가 정상적으로 실행")
    public void find_Books_with_limit(){
        /* given */
        int limit = 500;

        /* when */

        Executable e = () -> bookUpdatedRepo.findBooksWithLimit(limit);

        /* then */

        assertDoesNotThrow(e);
    }
    
    @Test @Transactional
    @DisplayName("deleteNotFoundBook 메소드가 정상적으로 실행")
    public void test_deleteNotFoundBook(){
        /* given */

        long lastId = 1330;
        
        /* when */

        Executable e = () ->bookUpdatedRepo.deleteNotFoundBook(lastId);
    
        /* then */

        assertDoesNotThrow(e);
    }

}