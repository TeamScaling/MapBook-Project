package com.scaling.libraryservice;

import com.scaling.libraryservice.dataPipe.csv.exporter.BookExporter;
import com.scaling.libraryservice.dataPipe.csv.util.LibraryDataCsvMerger;
import com.scaling.libraryservice.dataPipe.download.LoanCntDownloader;
import com.scaling.libraryservice.dataPipe.updater.service.BookUpdateService;
import com.scaling.libraryservice.search.engine.TitleAnalyzer;
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LibraryServiceApplicationTests {

    @Autowired
    private BookUpdateService bookUpdateService;

    @Autowired
    LoanCntDownloader loanCntDownloader;

    @Autowired
    LibraryDataCsvMerger merger;

    @Autowired
    BookExporter bookExporter;

    @Autowired
    TitleAnalyzer analyzer;

    @DisplayName("도서 업데이트 메소드 실행. 테스트 메소드가 아닌 실행을 위한 메소드")
    public void execute_update() {

        bookUpdateService.UpdateBookFromApi(200, 100);
    }

    //작업용
    //다운 로드한 Csv 파일을 하나로 합친다.
    public void mergeLibraryData() {
        /* given */

        merger.mergeLibraryData("download", "loanCnt.csv", "EUC-KR", CSVFormat.DEFAULT);
        /* when */

        /* then */
    }

    // 크롤러를 통해 원하는 날짜의 대출 횟수를 자동 다운로드 한다.
    public void collectLoanCnt(){
        /* given */

        loanCntDownloader.collectLoanCnt("(2023년 06월)");
        /* when */

        /* then */
    }

    // bookVo object를 Csv 파일로 output 한다.
    public void exportToCsv(){
        bookExporter.exportToCsv(0,500000,"books3.csv",true);
    }


}
