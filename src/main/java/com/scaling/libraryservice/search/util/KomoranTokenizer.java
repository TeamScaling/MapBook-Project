package com.scaling.libraryservice.search.util;

import java.util.List;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 한글 제목을 토크화 하는 클래스입니다.
 */
@Component
@RequiredArgsConstructor
public class KomoranTokenizer implements TitleTokenizer {

    private final Komoran komoran;

    /**
     * 주어진 한글 제목을 토큰화하여 명사만 추출합니다.
     *
     * @param target 토큰화하고자 하는 한글 제목
     * @return 추출된 명사가 담긴 List
     */
    @Override
    public List<String> tokenize(String target) {

        KomoranResult analyzeResultList = komoran.analyze(target);

        List<Token> tokens = analyzeResultList.getTokenList();

        List<String> nounList =
            tokens.stream().filter(
                    i -> i.getPos().equals("NNP") || i.getPos().equals("NNG"))
                .map(Token::getMorph).toList();

        return nounList.stream().distinct().filter(non -> non.length() > 1).toList();
    }

}