package com.scaling.libraryservice.search.engine.filter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class StopWordFilter extends AbstractTileFilter {

    private final TitleFilter nextFilter;
    private final HashSet<String> STOP_WORDS = new HashSet<>(
        List.of("a", "about", "an", "are", "as", "at", "be",
            "by", "com", "de", "en", "for", "from", "how",
            "i", "in", "is", "it", "la", "of", "on", "or", "that", "the", "this", "to", "was",
            "what", "when", "where", "who", "will", "with", "und", "the", "www"));

    public StopWordFilter(TitleFilter nextFilter) {
        this.nextFilter = nextFilter;
    }

    @Override
    public String filtering(String query) {
        String removedQuery = Arrays.stream(query.split("\\s+"))
            .filter(word -> !STOP_WORDS.contains(word)).collect(Collectors.joining(" "));

        return progressFilter(removedQuery, this.nextFilter);
    }
}
