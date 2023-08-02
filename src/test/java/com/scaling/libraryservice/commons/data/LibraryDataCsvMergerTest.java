package com.scaling.libraryservice.commons.data;

import com.scaling.libraryservice.dataPipe.csv.util.LoanCntCsvNormalizer;
import org.apache.commons.csv.CSVFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LibraryDataCsvMergerTest {
    
    @Autowired
    LoanCntCsvNormalizer merger;

    public void execute(){
        /* given */
        merger.mergeLibraryData("C:\\teamScaling\\test","testLib.csv","EUC-KR", CSVFormat.DEFAULT);
        
        /* when */
    
        /* then */
    }
}