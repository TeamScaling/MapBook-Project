package com.scaling.libraryservice.batch.loanCnt.util;

import com.scaling.libraryservice.batch.aop.BatchLogging;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.lang.NonNull;

public class LibraryCatalogUrlCrawler {

    //강남구립논현도서관 장서/대출 목록
    private static final String TARGET_URL = "https://data4library.kr/openDataV?libcode=";
    private static final String COMMON_DOWNLOAD_URL = "https://www.data4library.kr";

    // 도서관 장서 목록을 다운로드 할 수 있는 Url를 조사하여 반환 합니다.
    @BatchLogging
    public static Optional<String> getDownloadUrl(int libCode, String date) throws IOException {
        Connection conn = Jsoup.connect(TARGET_URL + libCode);
        AtomicReference<String> result = new AtomicReference<>();

        // 원하는 data-url 데이터를 얻기 위한 과정
        conn.get().select("table tr")
            .forEach(element -> {
                Element link_td = element.selectFirst("td.link_td");
                if (isContainsDateLine(link_td, date) && isExistTextTypeInElement(element)) {
                    result.set(COMMON_DOWNLOAD_URL + getDetailUrl(element));
                }
            });

        return Optional.ofNullable(result.get());
    }

    private static String getDetailUrl(@NonNull Element element) {
        return Objects.requireNonNull(element.selectFirst("a.download_link.text_type"))
            .attr("data-url");
    }

    private static boolean isContainsDateLine(Element link_td, String date) {
        return link_td != null && link_td.text().contains(date);
    }

    private static boolean isExistTextTypeInElement(Element element) {
        return element.selectFirst("a.download_link.text_type") != null;
    }

}
