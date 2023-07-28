package com.scaling.libraryservice.commons.data;

import com.scaling.libraryservice.data.LibraryDataCsvMerger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LibraryDataCsvMergerTest {
    
    @Autowired
    LibraryDataCsvMerger merger;

    public void execute(){
        /* given */
        merger.mergeLibraryData("C:\\teamScaling\\test","testLib.csv");
        
        /* when */
    
        /* then */
    }
}