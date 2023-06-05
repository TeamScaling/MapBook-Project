package com.scaling.libraryservice.recommend.dto;

import com.scaling.libraryservice.search.entity.Book;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RespRecommendTest {

	@Test
	@DisplayName("RespRecommendBookDto 생성")
	void construct_respRecommendBookDto() {
		Long id = 1L;
		String title = "아프니까 청춘이다";
		String content = "아프니까 청춘이다 :인생 앞에 홀로 선 젊은 그대에게";
		String author = "김난도";
		String isbn = "9788965700036";
		String bookImg = "http://image.aladin.co.kr/product/832/50/cover/8965700035_3.jpg";
		Integer loanCnt = 2;
		Book book = new Book(id, title, content, author, isbn, bookImg, loanCnt);

		RecommendBookDto recommendDto = new RecommendBookDto(book);
		List<RecommendBookDto> recommendBookDtos = new ArrayList<>();

		recommendBookDtos.add(recommendDto);

		RespRecommend respBooksDto = new RespRecommend(recommendBookDtos);

		Assertions.assertEquals(title, respBooksDto.getRecommendBooks().get(0).getTitle());

	}

}
