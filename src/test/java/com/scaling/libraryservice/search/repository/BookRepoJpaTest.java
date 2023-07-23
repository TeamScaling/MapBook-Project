package com.scaling.libraryservice.search.repository;

import com.scaling.libraryservice.search.util.EunjeonTokenizer;
import com.scaling.libraryservice.search.util.Token;
import com.scaling.libraryservice.search.util.TitleTrimmer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

@SpringBootTest
class BookRepoJpaTest {

    @Autowired
    BookRepoJpa bookRepoJPA;

    @Autowired
    EunjeonTokenizer tokenizer;

    @Test
    public void test1() {
        /* given */

        String str = "아프니까 청춘이다";

        var tokens = tokenizer.tokenize(str);

        /* when */

        var nntokens = tokens.get(Token.NN_TOKEN);
        var etcTokens = tokens.get(Token.ETC_TOKEN);

        var result = TitleTrimmer.splitAddPlus(nntokens);

        System.out.println(result);
        var books = bookRepoJPA.searchBookComplex(String.join(" ", result),
            String.join(" ", etcTokens),PageRequest.of( 0, 10));

        /* then */

        System.out.println(books.getContent());
    }

}