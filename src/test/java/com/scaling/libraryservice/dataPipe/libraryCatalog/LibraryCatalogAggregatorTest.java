package com.scaling.libraryservice.dataPipe.libraryCatalog;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LibraryCatalogAggregatorTest {

    @InjectMocks
    LibraryCatalogAggregator libraryCatalogAggregator;


    @Test @DisplayName("같은 ISBN에 대해 Loan Cnt를 합칠 수 있다")
    public void collectIsbnLoanCounts() {
        /* given */

        String line1 = "9788973374113,3";
        String line2 = "9788976491114,1";
        String line3 = "9788973374113,7";
        String line4 = "9788976491114,2";
        String line5 = "9788976491114,10";

        List<String> lineList = List.of(line1, line2, line3, line4, line5);

        /* when */
        List<String> result
            = libraryCatalogAggregator.aggregateLoanCnt(lineList);

        /* then */

        assertTrue(result.stream().anyMatch(line -> line.equals("9788973374113,10")));
        assertTrue(result.stream().anyMatch(line -> line.equals("9788976491114,13")));
    }


}