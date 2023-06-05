package com.scaling.libraryservice.recommend.dto;

import com.scaling.libraryservice.search.entity.Book;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RecommendBookDtoTest {

	@Test
	@DisplayName("RecommendBookDto 생성")
	void construct_recommendBookDto(){
		// given
		Long id = 1L;
		String title = "아프니까 청춘이다";
		String content = "아프니까 청춘이다 :인생 앞에 홀로 선 젊은 그대에게";
		String author = "김난도";
		String isbn = "9788965700036";
		String bookImg = "http://image.aladin.co.kr/product/832/50/cover/8965700035_3.jpg";
		Integer loanCnt = 2;
		Book book = new Book(id, title, content, author, isbn, bookImg, loanCnt);

		// when
		RecommendBookDto recommendBookDto = new RecommendBookDto(book);

		// then
		Assertions.assertEquals(title, recommendBookDto.getTitle());

	}

}
