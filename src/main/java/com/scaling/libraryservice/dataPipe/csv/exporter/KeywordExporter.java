package com.scaling.libraryservice.dataPipe.csv.exporter;

import com.scaling.libraryservice.dataPipe.csv.util.CsvFileWriter;
import com.scaling.libraryservice.dataPipe.vo.KeywordVo;
import com.scaling.libraryservice.search.repository.BookRepoQueryDsl;
import com.scaling.libraryservice.search.engine.filter.SimpleFilter;
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

    private Page<String> page;

    public KeywordExporter(CsvFileWriter<KeywordVo> csvFileWriter, BookRepoQueryDsl bookRepository) {
        super(csvFileWriter);
        this.bookRepository = bookRepository;
        this.simpleFilter = new SimpleFilter(null);
        this.page = Page.empty();
    }

    @Override
    List<KeywordVo> exportVoWithOption(Pageable pageable, String outputName, boolean option) {
        this.page = bookRepository.findTitleToken(pageable);

        Set<KeywordVo> bookTokens = new HashSet<>();

        page.getContent()
            .forEach(str -> Arrays.stream(str.split(" "))
                .map(simpleFilter::filtering)
                .filter(s -> !s.isBlank())
                .forEach(keyword -> bookTokens.add(new KeywordVo(keyword))));

        return bookTokens.stream().toList();
    }


    @Override
    Page<String> renewPage() {
        return page;
    }
}
