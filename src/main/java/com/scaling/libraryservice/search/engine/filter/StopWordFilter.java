package com.scaling.libraryservice.search.engine.filter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class StopWordFilter extends AbstractTileFilter {

    private final TitleFilter nextFilter;
    private final HashSet<String> ENG_STOP_WORDS = new HashSet<>(
        List.of("a", "about", "an", "are", "as", "at", "be",
            "by", "com", "de", "en", "for", "from", "how",
            "i", "in", "is", "it", "la", "of", "on", "or", "that", "the", "this", "to", "was",
            "what", "when", "where", "who", "will", "with", "und", "the", "www"));

    private final HashSet<String> KOR_STOP_WORDS = new HashSet<>(
        List.of("은", "는", "을", "를", "이", "가", "의", "에", "로")
    );

    private final HashSet<String> AUTHR_STOP_WORDS = new HashSet<>(
        List.of("글","그림","옮김","지은이","저자","지음","역자","[공]","[지음]","옮긴이","저")
    );


    public StopWordFilter(TitleFilter nextFilter) {
        this.nextFilter = nextFilter;
    }

    @Override
    public String filtering(String query) {

        String removedQuery = removeEngStopWord(query);
        removedQuery = removeKAuthrStopWord(removedQuery);

        return progressFilter(removedQuery, this.nextFilter);
    }

    private String removeEngStopWord(String query){
        return Arrays.stream(query.split("\\s+"))
            .filter(word -> !ENG_STOP_WORDS.contains(word))
            .collect(Collectors.joining(" "));
    }

    private String removeKorStopWord(String query){
        return Arrays.stream(query.split("\\s+"))
            .map(splitWord ->
                KOR_STOP_WORDS.stream()
                .filter(splitWord::endsWith)
                .findFirst()
                .map(stopWord -> splitWord.substring(0, splitWord.length() - stopWord.length()))
                .orElse(splitWord))
            .collect(Collectors.joining(" "));
    }

    private String removeKAuthrStopWord(String query){
        return Arrays.stream(query.split("\\s+"))
            .filter(word -> !AUTHR_STOP_WORDS.contains(word))
            .collect(Collectors.joining(" "));
    }

}
