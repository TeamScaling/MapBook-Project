package com.scaling.libraryservice.commons.data.exporter;

import com.scaling.libraryservice.commons.data.vo.KeywordVo;
import com.scaling.libraryservice.commons.data.CsvWriter;
import com.scaling.libraryservice.search.repository.BookRepoQueryDsl;
import com.scaling.libraryservice.search.util.filter.SimpleFilter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class KeywordExporter extends ExporterService<KeywordVo, String> {

    private final BookRepoQueryDsl bookRepository;

    private final SimpleFilter simpleFilter;

    public KeywordExporter(CsvWriter<KeywordVo> csvWriter, BookRepoQueryDsl bookRepository) {
        super(csvWriter);
        this.bookRepository = bookRepository;
        this.simpleFilter = new SimpleFilter(null);
    }

    @Override
    List<KeywordVo> analyzeAndExportBooks(Page<String> page, Pageable pageable, String outputName) {

        page = bookRepository.findTitleToken(pageable);

        Set<KeywordVo> bookTokens = new HashSet<>();

        page.getContent()
            .forEach(str -> Arrays.stream(
                    str.split(" "))
                .map(simpleFilter::filtering)
                .filter(s -> !s.isBlank())
                .forEach(token ->
                    bookTokens.add(new KeywordVo(token))));

        return bookTokens.stream().toList();
    }
}
