package com.scaling.libraryservice.repository;

import com.scaling.libraryservice.mapBook.repository.LibraryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LibraryRepositoryTest {

    @Autowired
    private LibraryRepository libraryRepo;


    @Test
    public void find_library_meta(){
        /* given */

        /* when */

        /* then */

        System.out.println(libraryRepo.findAll());
    }


}