package com.scaling.libraryservice.dataPipe.libraryCatalog;

import static org.junit.jupiter.api.Assertions.*;

import com.scaling.libraryservice.dataPipe.csv.util.CsvFileMerger;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

@ExtendWith(MockitoExtension.class)
class LibraryCatalogNormalizerTest {

    @InjectMocks
    LibraryCatalogNormalizer libraryCatalogNormalizer;


    @Test
    public void test1() {
        /* given */

        String element1 = "6,전쟁 같은 맛,그레이스 M. 조 (지은이), 주해연 (옮긴이),2023,9791169091183,,,,,,1,0,2023-06-30";
        String element2 = "7,두길 천자문 - 중국의 역사, 선비의 일생,김세중 (지은이),2023,9788928518685,,,,,,8,0,2023-06-30";

        String target1 = "9791169091183,1";
        String target2 = "9788928518685,8";

        List<String> list = List.of(element1,element2);

        /* when */
        List<String> normalize = libraryCatalogNormalizer.normalize(list);

        /* then */
        assertTrue(normalize.stream().anyMatch(s -> s.equals(target1)));
        assertTrue(normalize.stream().anyMatch(s -> s.equals(target2)));

    }
}