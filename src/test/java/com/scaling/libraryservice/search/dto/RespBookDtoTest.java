package com.scaling.libraryservice.search.dto;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class RespBookDtoTest {

	@Test
	@DisplayName("MetaDto, List<BookDto>로 ReqBookDto 생성자 생성")
	void RespBookDto(){

		//given
		long totalPages = 10;
		long totalElements = 100;
		long currentPage =  1;
		long pageSize = 10;
		MetaDto metaDto = new MetaDto(totalPages, totalElements, currentPage, pageSize);

		List<BookDto> bookDtoList = new ArrayList<>();
		bookDtoList.add(new BookDto(1L, "아프니까 청춘이다", "내용1", "김난도", "9791185032528", "이미지1"));
		bookDtoList.add(new BookDto(2L, "살다보니 인문학 - 정현태와 '인문학' 득템", "내용2", "정현태", "9791185032528", "이미지2"));

		// when
		RespBooksDto respBooksDto = new RespBooksDto(metaDto, bookDtoList);

		// then
		Assertions.assertEquals(pageSize, respBooksDto.getMeta().getTotalPages());
		Assertions.assertEquals(bookDtoList.get(0).getTitle(), respBooksDto.getDocuments().get(0).getTitle());

	}

	@Test
	@DisplayName("MetaDto, Page<BookDto>로 ReqBookDto 생성자 생성")
	void construct_respBookDto_byMeatDto_and_pageBookDto(){

		// given
		long totalPages = 10;
		long totalElements = 100;
		long currentPage =  1;
		long pageSize = 10;
		MetaDto metaDto = new MetaDto(totalPages, totalElements, currentPage, pageSize);

		List<BookDto> bookDtoList = new ArrayList<>();
		bookDtoList.add(new BookDto(1L, "아프니까 청춘이다", "내용1", "김난도", "9791185032528", "이미지1"));
		bookDtoList.add(new BookDto(2L, "살다보니 인문학 - 정현태와 '인문학' 득템", "내용2", "정현태", "9791185032528", "이미지2"));

		Pageable pageable = Pageable.ofSize(10).withPage(0);

		Page<BookDto> page = new PageImpl<>(bookDtoList, pageable, bookDtoList.size());

		//when
		RespBooksDto respBooksDto = new RespBooksDto(metaDto, page);

		// then
		Assertions.assertEquals(10, respBooksDto.getMeta().getPageSize());

	}

}
