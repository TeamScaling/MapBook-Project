package com.scaling.libraryservice.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MetaDto {

  private int page;

  private int size;

  public MetaDto(int page, int size) {
    this.page = page;
    this.size = size;
  }

}
