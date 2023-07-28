package com.scaling.libraryservice.commons.data.exporter;

import com.scaling.libraryservice.data.exporter.BookExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BookExporterTest {

    @Autowired
    BookExporter bookExporter;


    public void execute(){
        /* given */

        bookExporter.exportToCsv(0,500000,"books.csv");

        /* when */

        /* then */
    }

}