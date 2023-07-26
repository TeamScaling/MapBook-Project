package com.scaling.libraryservice.search.util.filter;

import com.scaling.libraryservice.search.service.KeywordService;
import com.scaling.libraryservice.search.util.EunjeonTokenizer;
import com.scaling.libraryservice.search.util.converter.EngToKorConverter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;

//한글을 영어로 잘못 쳤을 때 변환 해주는 역할을 한다.
@RequiredArgsConstructor
public class ConvertFilter extends AbstractTileFilter implements TitleFilter {

    private final TitleFilter nextFilter;
    private final KeywordService keywordService;


    @Override
    public String filtering(String query) {

        StringJoiner joiner = new StringJoiner(" ");
        List<String> requiredCheckList = new LinkedList<>();

        Arrays.stream(query.split(" "))
            .forEach(word -> {
                if (isEnglishWord(word)) {
                    convertAndAddCheckList(word, requiredCheckList);
                } else {
                    joiner.add(word);
                }
            });

        joiner.add(String.join(" ", keywordService.getExistKeywords(requiredCheckList)));

        return progressFilter(joiner.toString(), this.nextFilter);
    }

    private void convertAndAddCheckList(String originalWord, List<String> requiredCheckList) {

        String convertedWord = EngToKorConverter.convert(originalWord);

        requiredCheckList.add(originalWord);
        requiredCheckList.addAll(EunjeonTokenizer.getQualifiedNnTokens(convertedWord));
    }

    public static boolean isEnglishWord(String input) {
        Pattern pattern = Pattern.compile("^[a-zA-Z]+$"); // 영어 단어를 확인하는 정규 표현식
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }


}
