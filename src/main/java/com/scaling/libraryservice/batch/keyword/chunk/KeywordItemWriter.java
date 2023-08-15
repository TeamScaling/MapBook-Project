package com.scaling.libraryservice.batch.keyword.chunk;

import com.scaling.libraryservice.search.entity.Keyword;
import com.scaling.libraryservice.search.repository.KeywordRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class KeywordItemWriter implements ItemWriter<Keyword> {

    private final KeywordRepository keywordRepository;

    @Override
    @Transactional
    public void write(List<? extends Keyword> items) {
        keywordRepository.saveAll(items);
    }
}
