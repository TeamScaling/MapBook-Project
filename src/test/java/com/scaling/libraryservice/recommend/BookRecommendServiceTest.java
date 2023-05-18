package com.scaling.libraryservice.recommend;

import com.scaling.libraryservice.recommend.cacheKey.RecommendCacheKey;
import com.scaling.libraryservice.recommend.service.RecommendService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BookRecommendServiceTest {

		@Autowired
		private RecommendService recommendService;

		@Test
		@DisplayName("추천 도서 검색")
		void test_recommend_book(){
			String title = "고양이 탐구생활 (개정판) : 고양이이에 관한 잡다한 지식 사전 - 고양이에 관한 잡다한 지식 사전";

			List<String> recommendBook = recommendService.getRecommendBook(new RecommendCacheKey(title));

			System.out.println(recommendBook.toString());

		}

}
