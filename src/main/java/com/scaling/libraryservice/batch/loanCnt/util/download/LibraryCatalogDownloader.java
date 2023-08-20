package com.scaling.libraryservice.batch.loanCnt.util.download;

import com.scaling.libraryservice.batch.aop.BatchLogging;
import com.scaling.libraryservice.batch.loanCnt.util.LibraryCatalogUrlCrawler;
import com.scaling.libraryservice.mapBook.dto.LibraryInfoDto;
import com.scaling.libraryservice.mapBook.service.LibraryFindService;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class LibraryCatalogDownloader extends AbstractDownLoader {

    private final LibraryFindService libraryFindService;
    private static final String DEFAULT_EXTENSION = ".csv";
    private static final String DEFAULT_DIRECTORY = "pipe/download/";

    @Override
    @BatchLogging
    public void downLoad(String outPutDirectory, String targetDate, boolean limitOption,
        int limit) {

        // csv file을 서버로부터 다운 받기 위해 SSL을 모두 신뢰 한다.
        setupTrustAllSSLContext();

        List<LibraryInfoDto> libraryInfoList = setUpLibraryInfoByOption(limitOption, limit);

        // CompletableFuture 리스트를 생성합니다.
        List<CompletableFuture<Void>> futures = libraryInfoList.stream()
            .map(library -> CompletableFuture.runAsync(() -> {
                try {
                    Optional<String> url = LibraryCatalogUrlCrawler.getDownloadUrl(
                        library.getLibCd(), targetDate);

                    if (url.isPresent()) {
                        String fileNm = configureFileName(outPutDirectory, library.getLibNm(),
                            targetDate);
                        downloadFile(url.get(), fileNm);
                    }
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            })).toList();

        // 모든 CompletableFuture 작업이 완료될 때까지 기다린다.
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

    public static String getDefaultDirectory() {
        return DEFAULT_DIRECTORY;
    }

    private String configureFileName(String outPutDirectory, String libNm, String date) {
        return outPutDirectory + "/"
            + String.join(" ", libNm, date)
            + String.join(".", DEFAULT_EXTENSION);
    }

    private List<LibraryInfoDto> setUpLibraryInfoByOption(boolean limitOption, int limit) {

        return limitOption ? libraryFindService.getLibrariesWithLimit(limit) :
            libraryFindService.getAllLibraries();
    }


}
