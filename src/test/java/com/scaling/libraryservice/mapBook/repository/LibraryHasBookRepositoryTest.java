package com.scaling.libraryservice.mapBook.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.NONE)
class LibraryHasBookRepositoryTest {

    @Autowired
    private LibraryHasBookRepository libraryHasBookRepo;


    @Test
    public void load(){
        /* given */
        System.out.println(libraryHasBookRepo.findSupportedArea().get(0));
        /* when */

        /* then */
    }
}