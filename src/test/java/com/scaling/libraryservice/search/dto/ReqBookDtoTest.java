package com.scaling.libraryservice.search.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ReqBookDtoTest {

	@Test
	@DisplayName("ReqBookDto 생성자 생성")
	void ReqBookDto(){
		String query = "아프니까 청춘이다";
		int page = 0;
		int size = 10;

		ReqBookDto reqBookDto = new ReqBookDto(query, page, size);

		Assertions.assertEquals(query, reqBookDto.getQuery());
	}

}
