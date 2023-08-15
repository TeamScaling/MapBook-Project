package com.scaling.libraryservice.batch.preSortBook.chunk;

import com.scaling.libraryservice.search.dto.BookDto;
import com.scaling.libraryservice.search.engine.TitleAnalyzer;
import com.scaling.libraryservice.search.engine.TitleQuery;
import com.scaling.libraryservice.search.engine.filter.SimpleFilter;
import com.scaling.libraryservice.search.engine.filter.StopWordFilter;
import com.scaling.libraryservice.search.entity.Book;
import com.scaling.libraryservice.search.entity.SortBook;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.batch.item.ItemProcessor;

public class PreSortBookItemProcessor implements ItemProcessor<Book, SortBook> {

    private final TitleAnalyzer titleAnalyzer;

    private final SimpleFilter simpleFilter;

    public PreSortBookItemProcessor(TitleAnalyzer titleAnalyzer) {
        this.titleAnalyzer = titleAnalyzer;
        this.simpleFilter = new SimpleFilter(new StopWordFilter(null),false);
    }

    @Override
    public SortBook process(Book item) throws Exception {

        BookDto bookDto = new BookDto(item);

        if (bookDto.getTitleToken().isBlank()) {
            TitleQuery titleQuery = titleAnalyzer.analyze(bookDto.getTitle(), false);

            String titleToken = titleQuery.getNnToken().trim();
            String author = simpleFilter.filtering(bookDto.getAuthor());

            Set<String> tokens = new HashSet<>();
            tokens.addAll(Arrays.asList(titleToken.split(" ")));
            tokens.addAll(Arrays.asList(author.split(" ")));

            bookDto.setTitleToken(String.join(" ", tokens));
        }

        return new SortBook(bookDto);
    }

    private String authorAndTitleToken(BookDto bookDto) {
        SimpleFilter simpleFilter = new SimpleFilter(new StopWordFilter(null),false);

        String titleTokens = bookDto.getTitleToken().isBlank() ? "" : bookDto.getTitleToken();
        String author = simpleFilter.filtering(bookDto.getAuthor());

        Set<String> distinctTokens = Stream.of(titleTokens, author)
            .flatMap(token -> Arrays.stream(token.split(" ")))
            .collect(Collectors.toCollection(LinkedHashSet::new));

        return String.join(" ", distinctTokens);
    }
}
