package com.scaling.libraryservice.data.exporter;

import com.scaling.libraryservice.data.CsvWriter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public abstract class ExporterService<V,T> {

    private final CsvWriter<V> csvWriter;

    public void exportToCsv(int pageNumber,int pageSize,String outputName){

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<T> page = Page.empty();

        do {
            List<V> voList = analyzeAndExportVo(pageable,outputName);
            csvWriter.writeToCsv(voList, outputName);
            page = renewPage();
            pageable = pageable.next();

        } while (page.hasNext());  // 다음 페이지가 존재하는지 확인
    }

    abstract List<V> analyzeAndExportVo(Pageable pageable,String outputName);

    abstract Page<T> renewPage();
}
