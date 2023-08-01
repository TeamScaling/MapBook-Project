package com.scaling.libraryservice.search.engine.filter;

import com.scaling.libraryservice.search.service.KeywordService;
import com.scaling.libraryservice.search.engine.EunjeonTokenizer;
import com.scaling.libraryservice.search.engine.converter.EngToKorConverter;
import com.scaling.libraryservice.search.engine.converter.KorToEngConverter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

//한글을 영어로 잘못 쳤을 때 변환 해주는 역할을 한다.
@RequiredArgsConstructor
public class ConvertFilter extends AbstractTileFilter implements TitleFilter {

    private final TitleFilter nextFilter;
    private final KeywordService keywordService;
    private static final String ENG_REGEX = "^[a-zA-Z]+$";

    @Override
    public String filtering(String query) {
        StringJoiner joiner = new StringJoiner(" ");
        List<String> requiredCheckList = new LinkedList<>();

        Arrays.stream(query.split(" "))
            .forEach(word -> {
                    if (isEnglishWord(word)) {
                        convertAddEngCheckList(word, requiredCheckList);
                    } else {
                        joiner.add(word);
                    }
                }
            );

        addExistKeywordsToJoiner(requiredCheckList, joiner);

        return progressFilter(joiner.toString(), this.nextFilter);
    }

    private void addExistKeywordsToJoiner(List<String> requiredCheckList, StringJoiner joiner) {
        List<String> existKeywords = keywordService.getExistKeywords(requiredCheckList);

        if (!existKeywords.isEmpty()) {
            joiner.add(String.join(" ", existKeywords));
        }
    }

    private void convertAddEngCheckList(String originalWord, List<String> requiredCheckList) {

        String convertedWord = EngToKorConverter.convert(originalWord);

        requiredCheckList.add(originalWord);
        requiredCheckList.addAll(EunjeonTokenizer.getQualifiedNnTokens(convertedWord));
    }

    // 'ㅓㅁㅍㅁ' -> 'java'로 변환 해주는 메소드 (버그로 인해 일단 사용 X)
    void convertAddKorCheckList(String originalWord, List<String> requiredCheckList) {

        String convertedWord = KorToEngConverter.convert(originalWord);

        requiredCheckList.add(originalWord);
        requiredCheckList.add(convertedWord);
    }

    public static boolean isEnglishWord(String input) {
        Pattern pattern = Pattern.compile(ENG_REGEX); // 영어 단어를 확인하는 정규 표현식
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }


}
