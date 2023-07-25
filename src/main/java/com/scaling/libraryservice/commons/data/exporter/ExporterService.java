package com.scaling.libraryservice.commons.data.exporter;

import com.scaling.libraryservice.commons.data.CsvWriter;
import com.scaling.libraryservice.search.entity.Book;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public abstract class ExporterService<V,T> {

    private final CsvWriter<V> csvWriter;

    public void exportTemplate(int pageNumber,int pageSize,String outputName){

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<T> page = Page.empty();

        do {
            List<V> books = analyzeAndExportBooks(page,pageable,outputName);
            csvWriter.writeToCsv(books, outputName);

        } while (page.hasNext());  // 다음 페이지가 존재하는지 확인
    }

    abstract List<V> analyzeAndExportBooks(Page<T> page, Pageable pageable,String outputName);
}
