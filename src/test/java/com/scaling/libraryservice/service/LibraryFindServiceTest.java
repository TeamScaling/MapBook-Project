package com.scaling.libraryservice.service;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.scaling.libraryservice.dto.LibraryDto;
import com.scaling.libraryservice.dto.ReqMapBookDto;
import com.scaling.libraryservice.entity.LibraryMeta;
import com.scaling.libraryservice.exception.LocationException;
import com.scaling.libraryservice.repository.LibraryMetaRepository;
import com.scaling.libraryservice.repository.LibraryRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class LibraryFindServiceTest {

    private LibraryFindService libraryFindService;
    @Autowired
    private LibraryRepository libraryRepo;

    private List<LibraryDto> libraries;

    @Autowired
    private LibraryMetaRepository libraryMetaRepo;

    @BeforeEach
    void setUp() {
        this.libraries = libraryRepo.findAll().stream().map(LibraryDto::new).toList();

        libraryFindService = new LibraryFindService(libraryRepo,libraryMetaRepo);
        libraryFindService.setLibraries(libraries);

    }

    @Test @DisplayName("위도/경도 데이터만으로 주변 도서관을 검색")
    public void find_libraries_with_coordinate(){
        /* given */

        var dto = new ReqMapBookDto("9788089365210",34.802858, 126.702513,null,null);

        /* when */

        var result = libraryFindService.findNearByLibraries(dto);

        /* then */
        result.forEach(System.out::println);
        assertNotEquals(0,result.size());
    }

    @Test @DisplayName("직접 주소 선택으로 주변 도서관 검색")
    public void find_libraries_with_address(){
        /* given */
        var dto = new ReqMapBookDto("9788089365210",0.0,0.0,"경기도","성남시");

        /* when */

        var result = libraryFindService.findNearByLibraries(dto);

        /* then */
        result.forEach(System.out::println);
        assertNotEquals(0,result.size());
    }

    @Test @DisplayName("잘못된 위/경도로 주변 도서관 검색")
    public void find_libraries_error(){
        /* given */
        var dto = new ReqMapBookDto("9788089365210",38.74273402531946, 127.3437713197453,null,null);

        /* when */

        Executable e = () -> libraryFindService.findNearByLibraries(dto);

        /* then */
        assertThrows(LocationException.class,e);
    }

    @Test @DisplayName("커스텀으로 만든 지역 코드로 도서관 찾기")
    public void find_libraries_by_areaCd(){
        /* given */
        int areaCd = 27500;

        /* when */

        var result = libraryFindService.findLibrariesByAreaCd(areaCd);

        /* then */

        System.out.println(result);
    }

}