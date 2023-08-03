package com.scaling.libraryservice.dataPipe.csv.exporter;

import com.scaling.libraryservice.dataPipe.csv.util.CsvFileWriter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public abstract class ExporterService<V,T> {

    private final CsvFileWriter<V> csvFileWriter;

    // vo object를 Csv 파일로 output 한다.
    public void exportToCsv(int pageNumber,int pageSize,String outputName,boolean option){

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<T> page = Page.empty();

        do {
            List<V> voList = exportVoWithOption(pageable,outputName,option);
            csvFileWriter.writeToCsv(voList, outputName);
            page = renewPage();
            pageable = pageable.next();

        } while (page.hasNext());  // 다음 페이지가 존재하는지 확인
    }

    abstract List<V> exportVoWithOption(Pageable pageable,String outputName,boolean option);

    abstract Page<T> renewPage();
}
