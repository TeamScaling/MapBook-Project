package com.scaling.libraryservice.dataPipe.csv.util;

import static com.scaling.libraryservice.dataPipe.csv.util.LoanCntMerger.getDefaultOutputFormat;
import static com.scaling.libraryservice.dataPipe.download.LoanCntDownloader.getDefaultDirectory;

import com.scaling.libraryservice.dataPipe.download.LoanCntDownloader;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DataPipeManager {

    private static final int ISBN_IDX = 0;
    private static final int LOAN_CNT_IDX = 1;

    private final LoanCntDownloader loanCntDownloader;

    private final LoanCntCsvNormalizer normalizer;

    private final static String ROOT = "pipe";

    private final static String BRANCH = "/step";

    public void updateNewData() throws IOException {

//        loanCntDownloader.collectLoanCntFile(getDefaultDirectory(),"(2023년 06월)");

        normalizer.mergeLibraryData(ROOT +"/download", getPosition(1)+"/normalFile.csv", "EUC-KR", CSVFormat.DEFAULT);

        CsvDivider.divide(getPosition(1)+"/normalFile.csv", getPosition(2)+"/divide",10000000);

        LoanCntMerger.mergeLoanCntFiles(getPosition(2), getPosition(3)+"/step3", getDefaultOutputFormat());

        CsvFileMerger.mergeCsvFile(getPosition(3), getPosition(4)+"/result.csv",0,1);
    }

    private String getPosition(int number){
        return ROOT+BRANCH+number;
    }

}
