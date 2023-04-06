package com.scaling.libraryservice.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MetaDto {

  private long totalPages;

  private long totalElements;

  private long currentPage;

  private long pageSize;

}
