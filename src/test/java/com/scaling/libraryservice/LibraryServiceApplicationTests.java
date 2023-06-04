package com.scaling.libraryservice;

import com.scaling.libraryservice.commons.updater.service.BookUpdateService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LibraryServiceApplicationTests {

    @Autowired
    private BookUpdateService bookUpdateService;


    @DisplayName("도서 업데이트 메소드 실행. 테스트 메소드가 아닌 실행을 위한 메소드")
    public void execute_update(){

        bookUpdateService.UpdateBookFromApi(200,100);
    }

}
