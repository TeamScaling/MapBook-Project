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
        List.of("은", "는", "을", "를", "이", "가", "의", "에", "로", "으로", "과", "와", "도", "에서", "만", "이나",
            "나", "까지", "부터", "에게", "보다", "께", "처럼", "이라도", "라도", "으로서", "로서", "조차", "만큼", "같이",
            "마저", "이나마", "나마", "한테", "더러", "에게서", "한테서", "께서", "이야", "이라야")
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

    public String removeEngStopWord(String query){
        return Arrays.stream(query.split("\\s+"))
            .filter(word -> !ENG_STOP_WORDS.contains(word))
            .collect(Collectors.joining(" "));
    }

    public String removeKAuthrStopWord(String query){
        return Arrays.stream(query.split("\\s+"))
            .filter(word -> !AUTHR_STOP_WORDS.contains(word))
            .collect(Collectors.joining(" "));
    }

}
