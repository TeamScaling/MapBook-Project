package com.scaling.libraryservice.dataPipe.csv.util;

import com.opencsv.bean.StatefulBeanToCsvBuilder;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CsvWriter<V> {

    public void writeToCsv(List<V> target, String outputPath) {
        try {
            Path path = Paths.get(outputPath);
            Writer writer;

            // file이 이미 존재하면 이어쓰기를 실시 한다.
            writer = Files.exists(path) ?
                Files.newBufferedWriter(path, StandardOpenOption.APPEND)
                : Files.newBufferedWriter(path);

            new StatefulBeanToCsvBuilder<V>(writer)
                .withQuotechar('"')
                .build().write(target);

            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
