package com.scaling.libraryservice.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.scaling.libraryservice.entity.Library;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LibraryRepositoryTest {

    @Autowired
    private LibraryRepository libraryRepo;

    @Test
    @DisplayName("Libray 엔티티 기본 find 확인")
    public void contextLoad(){
        /* given */

        Integer libCd = 4702;

        /* when */
        Library library = libraryRepo.findById(libCd).get();

        /* then */

        assertNotNull(library);
        System.out.println(library);
    }


    @Test @DisplayName("지역 이름에 따라 도서관 정보 찾기")
    public void find_Lib_by_CD(){
        /* given */

        String area = "성남";

        /* when */

        List<Library> result = libraryRepo.findLibInfo(area);
        /* then */

        assertNotNull(result);

        result.forEach(System.out::println);
    }


}