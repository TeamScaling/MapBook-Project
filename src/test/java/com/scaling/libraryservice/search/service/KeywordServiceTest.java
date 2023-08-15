package com.scaling.libraryservice.search.service;

import static org.junit.jupiter.api.Assertions.*;

import com.scaling.libraryservice.search.entity.Keyword;
import com.scaling.libraryservice.search.repository.KeywordQueryDsl;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class KeywordServiceTest {

    @InjectMocks
    KeywordService keywordService;

    @Mock
    KeywordQueryDsl keywordQueryDsl;

    @Test
    public void getExistKeywords(){
        /* given */
        String[] words = new String[]{"wkqk","wjdtjr","자바","정석"};

        Keyword keyword1 = new Keyword(1L,"자바");
        Keyword keyword2 = new Keyword(2L,"정석");

        Mockito.when(keywordQueryDsl.getKeywords(words)).thenReturn(List.of(keyword1,keyword2));

        /* when */
        List<String> existKeywords = keywordService.getExistKeywords(
            List.of("wkqk", "wjdtjr", "자바", "정석"));

        /* then */
        assertTrue(existKeywords.stream().anyMatch(keyword -> keyword.equals("자바")));
        assertTrue(existKeywords.stream().anyMatch(keyword -> keyword.equals("정석")));
    }

}