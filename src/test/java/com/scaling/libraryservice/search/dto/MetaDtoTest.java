package com.scaling.libraryservice.search.dto;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class MetaDtoTest {

	@Test
	@DisplayName("MetaDto로 MetaDto 생성자 생성")
	void MetaDto(){

		long totalPages = 10;
		long totalElements = 100;
		long currentPage =  1;
		long pageSize = 10;

		MetaDto metaDto = new MetaDto(totalPages, totalElements, currentPage, pageSize);

		Assertions.assertEquals(totalPages, metaDto.getTotalPages());

	}

	@Test
	@DisplayName("PageBookDto와 ReqBookDto로 MetaDto 생성자 생성")
	void test_construct_metaDto_byPageBookDto_and_ReqBookDto(){
		// given
		ReqBookDto reqBookDto = new ReqBookDto("아프니까 청춘이다", 0, 10);

		List<BookDto> bookDtoList = new ArrayList<>();
		bookDtoList.add(new BookDto(1L, "아프니까 청춘이다", "내용1", "김난도", "9791185032528", "이미지1"));
		bookDtoList.add(new BookDto(2L, "살다보니 인문학 - 정현태와 '인문학' 득템", "내용2", "정현태", "9791185032528", "이미지2"));

		Pageable pageable = Pageable.ofSize(10).withPage(0);

		Page<BookDto> page = new PageImpl<>(bookDtoList, pageable, bookDtoList.size());

		//when
		MetaDto metaDto = new MetaDto(page, reqBookDto);

		// then
		Assertions.assertEquals(10, metaDto.getPageSize());

	}

	@Test
	@DisplayName("JSONObject로 MetaDto 생성자 생성")
	void test_construct_metaDto_byJSONObject(){
		JSONObject json = new JSONObject();
		json.put("totalPages", 10);
		json.put("totalElements", 100);
		json.put("currentPage", 0);
		json.put("pageSize", 10);

		MetaDto dto = new MetaDto(json);

		Assertions.assertEquals(0, dto.getCurrentPage());
	}

}
