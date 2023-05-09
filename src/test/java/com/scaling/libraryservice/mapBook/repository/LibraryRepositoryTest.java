package com.scaling.libraryservice.mapBook.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.NONE)
class LibraryRepositoryTest {

    @Autowired
    private LibraryRepository libraryRepository;

    @Test
    @DisplayName("areaCd로 도서관 정보 찾기")
    public void find_All_By_AREA_CD(){
        /* given */
        Integer areaCd = 7000;

        /* when */

        var libs = libraryRepository.findAllByAreaCd(areaCd);

        /* then */

        System.out.println(libs);
    }

}