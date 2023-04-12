package com.scaling.libraryservice.service;

import com.scaling.libraryservice.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookLibraryService {

    private final BookRepository bookRepository;

    private final String SEONGNAM_RANGE = "LBRRY_CD >= 26200 and LBRRY_CD <=26246";

    public void getLigCodes(){



    }


}
