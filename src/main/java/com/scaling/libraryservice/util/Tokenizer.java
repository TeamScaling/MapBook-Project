package com.scaling.libraryservice.util;

import java.util.List;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Tokenizer {

    private final Komoran komoran;

    public List<String> tokenize(String target){

        KomoranResult analyzeResultList = komoran.analyze(target);

        List<Token> tokenList = analyzeResultList.getTokenList();

        List<String> nounList =
            tokenList.stream().filter(i -> i.getPos().equals("NNP") || i.getPos().equals("NNG"))
                .map(Token::getMorph).toList();

        return nounList;
    }

}
