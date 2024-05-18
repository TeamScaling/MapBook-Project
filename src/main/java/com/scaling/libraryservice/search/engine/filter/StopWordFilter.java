package com.scaling.libraryservice.search.engine.filter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class StopWordFilter extends AbstractTileFilter {

    private final TitleFilter nextFilter;

    private final Set<String> ENG_STOP_WORDS = new HashSet<>(
        Set.of("a", "about", "an", "are", "as", "at", "be",
            "by", "com", "de", "en", "for", "from", "how",
            "i", "in", "is", "it", "la", "of", "on", "or", "that", "the", "this", "to", "was",
            "what", "when", "where", "who", "will", "with", "und", "www"));

    private final Set<String> AUTHR_STOP_WORDS = new HashSet<>(
        Set.of("글", "그림", "옮김", "지은이", "저자", "지음", "역자", "[공]", "[지음]", "옮긴이", "저")
    );

    public StopWordFilter(TitleFilter nextFilter) {
        this.nextFilter = nextFilter;
    }

    @Override
    public String filtering(String query) {
        String removedQuery = removeEngStopWord(query);
        removedQuery = removeAuthrStopWord(removedQuery);

        return progressFilter(removedQuery, this.nextFilter);
    }

    private String removeEngStopWord(String query) {
        return Arrays.stream(query.split("\\s+"))
            .filter(word -> !ENG_STOP_WORDS.contains(word))
            .collect(Collectors.joining(" "));
    }

    private String removeAuthrStopWord(String query) {
        return Arrays.stream(query.split("\\s+"))
            .filter(word -> !AUTHR_STOP_WORDS.contains(word))
            .collect(Collectors.joining(" "));
    }

}
