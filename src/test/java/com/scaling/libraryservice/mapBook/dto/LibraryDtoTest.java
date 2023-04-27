package com.scaling.libraryservice.mapBook.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class LibraryDtoTest {

    @Test
    public void load(){
        /* given */

        LibraryDto dto = new LibraryDto();

        dto.setErrorCnt(100);

        LoanItemDto dto1 = new LoanItemDto();

        System.out.println(dto.getErrorCnt());
        System.out.println(dto1.getErrorCnt());


        /* when */

        /* then */
    }

}