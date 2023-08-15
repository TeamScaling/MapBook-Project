package com.scaling.libraryservice.mapBook.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.scaling.libraryservice.TestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
class LibraryHasBookRepositoryTest {

    @Autowired
    LibraryHasBookRepository libraryHasBookRepository;

    @Test
    public void test1(){
        /* given */
        System.out.println(libraryHasBookRepository.findHasBookLibraries("9788937460005",26300));
        /* when */

        /* then */
    }

}