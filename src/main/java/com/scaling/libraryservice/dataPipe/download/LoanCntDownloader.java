package com.scaling.libraryservice.dataPipe.download;

import com.scaling.libraryservice.mapBook.service.LibraryFindService;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service @Slf4j
public class LoanCntDownloader {

    private final LibraryFindService libraryFindService;

    private static final String DEFAULT_EXTENSION = ".csv";
    private static final String DEFAULT_DIRECTORY = "pipe/download/";

    public void collectLoanCntFile(String outPutDirectory, String targetDate) {

        log.info("[{}] is start",this.getClass().getSimpleName());

        // csv file을 서버로부터 다운 받기 위해 SSL을 모두 신뢰 한다.
        setupTrustAllSSLContext();

        libraryFindService.getLibrariesWithLimit(50).forEach(
            library -> {
                try {
                    Optional<String> url =
                        LoanCntUrlCrawler.getDownloadUrl(library.getLibCd(), targetDate);

                    if (url.isPresent()) {
                        downloadFile(url.get(), configureFileName(
                            outPutDirectory,
                            library.getLibNm(),
                            targetDate)
                        );
                    }
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }
        );

        log.info("[{}] is completed",this.getClass().getSimpleName());
    }

    public static String getDefaultDirectory(){
        return DEFAULT_DIRECTORY;
    }

    private String configureFileName(String outPutDirectory, String libNm, String date) {
        return outPutDirectory + "/"
            + String.join(" ", libNm, date)
            + String.join(".", DEFAULT_EXTENSION);
    }

    private void downloadFile(String siteUrl, String fileName) throws IOException {
        InputStream in = new BufferedInputStream(new URL(siteUrl).openStream());
        OutputStream out = new FileOutputStream(fileName);

        byte[] data = new byte[1024];
        int count;

        while ((count = in.read(data, 0, 1024)) != -1) {
            out.write(data, 0, count);
        }
        out.close();
        in.close();
        log.info("[{}] download complete",fileName);
    }

    private void setupTrustAllSSLContext() {

        try {
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[]{generateAllTrustManager()},
                new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //모든 SSL 연결을 신뢰 한다.
    private X509TrustManager generateAllTrustManager() {
        return new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
    }

}
