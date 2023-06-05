package com.scaling.libraryservice.mapBook.dto;

import com.scaling.libraryservice.mapBook.entity.LibraryMeta;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LibraryMetaDtoTest {

	@Test
	@DisplayName("LibraryMetaDto 생성")
	void LibraryMetaDto(){
		Integer id = 1;
		Integer areaCd = 2600;
		Long count = 20L;
		String oneArea = "서울특별시";
		String twoArea = "강서구";
		LibraryMeta libraryMeta = new LibraryMeta(id, areaCd, count, oneArea, twoArea);

		LibraryMetaDto metaDto = new LibraryMetaDto(libraryMeta);

		Assertions.assertEquals(oneArea, metaDto.getOneArea());
	}
}
