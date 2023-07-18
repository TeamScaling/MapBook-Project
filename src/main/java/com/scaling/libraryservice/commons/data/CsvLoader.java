package com.scaling.libraryservice.commons.data;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CsvLoader<T> {

    public List<T> loadBooksFromCsv(String path,Class<T> clazz) {
        List<T> voList = null;

        try(Reader reader = Files.newBufferedReader(Paths.get(path))) {

            CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
                .withType(clazz)
                .withIgnoreLeadingWhiteSpace(true)
                .withSeparator('\t')
                .withQuoteChar('"')
                .withIgnoreQuotations(true)
                .build();

            voList = csvToBean.parse();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return voList;
    }

}
