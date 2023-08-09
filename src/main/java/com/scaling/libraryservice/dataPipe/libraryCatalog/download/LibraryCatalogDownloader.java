package com.scaling.libraryservice.dataPipe.libraryCatalog.download;

import com.scaling.libraryservice.dataPipe.aop.BatchLogging;
import com.scaling.libraryservice.dataPipe.libraryCatalog.LibraryCatalogUrlCrawler;
import com.scaling.libraryservice.mapBook.dto.LibraryInfoDto;
import com.scaling.libraryservice.mapBook.service.LibraryFindService;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
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
    public Path downLoad(String outPutDirectory, String targetDate,boolean limitOption,int limit) {

        // csv file을 서버로부터 다운 받기 위해 SSL을 모두 신뢰 한다.
        setupTrustAllSSLContext();

        List<LibraryInfoDto> libraryInfoList = setUpLibraryInfoByOption(limitOption,limit);

        libraryInfoList.parallelStream()
            .forEach(
            library -> {
                try {
                    Optional<String> url =
                        LibraryCatalogUrlCrawler.getDownloadUrl(library.getLibCd(), targetDate);

                    if (url.isPresent()) {
                        String fileNm = configureFileName(outPutDirectory, library.getLibNm(), targetDate);
                        downloadFile(url.get(),fileNm);
                    }
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }
        );

        return Path.of(outPutDirectory);
    }

    public static String getDefaultDirectory() {
        return DEFAULT_DIRECTORY;
    }

    private String configureFileName(String outPutDirectory, String libNm, String date) {
        return outPutDirectory + "/"
            + String.join(" ", libNm, date)
            + String.join(".", DEFAULT_EXTENSION);
    }

    private List<LibraryInfoDto> setUpLibraryInfoByOption(boolean limitOption,int limit){

        return limitOption? libraryFindService.getLibrariesWithLimit(limit):
            libraryFindService.getAllLibraries();
    }


}
