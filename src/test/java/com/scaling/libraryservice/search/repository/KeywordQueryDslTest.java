package com.scaling.libraryservice.search.repository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.scaling.libraryservice.TestConfig;
import com.scaling.libraryservice.search.entity.Keyword;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
@Import(TestConfig.class)
class KeywordQueryDslTest {

    @Autowired
    KeywordQueryDsl keywordQueryDsl;

    @Test @DisplayName("Keyword table에 존재하는 명사 키워드를 찾아 올 수 있다.")
    public void getKeywords() {
        /* given */
        String keyword1 = "자바";
        String keyword2 = "정석";
        String keyword3 = "spring";
        String keyword4 = "wkqk";


        /* when */

        var result = keywordQueryDsl.getKeywords(keyword1,keyword2,keyword3,keyword4);

        /* then */
        assertTrue(result.stream().anyMatch(word -> word.getKeyword().equals(keyword1)));
        assertTrue(result.stream().anyMatch(word -> word.getKeyword().equals(keyword2)));
        assertTrue(result.stream().anyMatch(word -> word.getKeyword().equals(keyword3)));
        assertFalse(result.stream().anyMatch(word -> word.getKeyword().equals(keyword4)));
    }

    @Test @DisplayName("keyword가 없으면 빈 결과를 반환 할 수 있다.")
    public void getKeywords_empty() {
        /* given */

        /* when */

        List<Keyword> sut = keywordQueryDsl.getKeywords();

        /* then */

        assertTrue(sut.isEmpty());
    }

}