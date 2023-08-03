package com.scaling.libraryservice.dataPipe.download;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractDownLoader implements DownLoader{

    @Override
    public abstract Path downLoad(String outPutDirectory, String targetDate);

    void downloadFile(String siteUrl, String fileName) throws IOException {
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

    void setupTrustAllSSLContext() {

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
