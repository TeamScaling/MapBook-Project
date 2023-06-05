package com.scaling.libraryservice.recommend.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ReqRecommendDtoTest {

	@Test
	@DisplayName("ReqRecommendBookDto 생성")
	void construct_reqRecommendBookDto() {
		String query = "아프니까 청춘이다.";

		ReqRecommendDto reqRecommendDto = new ReqRecommendDto(query);

		Assertions.assertEquals(query, reqRecommendDto.getQuery());

	}
}
