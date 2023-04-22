package com.scaling.libraryservice.mapBook.service;

import com.scaling.libraryservice.mapBook.dto.LibraryDto;
import com.scaling.libraryservice.mapBook.dto.LibraryMetaDto;
import com.scaling.libraryservice.mapBook.repository.LibraryMetaRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LibraryMetaService {

    private final LibraryMetaRepository libraryMetaRepo;

    // viewSearch.html에서 참고할 도서관 메타 정보.
    public List<LibraryMetaDto> getLibraryMeta() {

        return libraryMetaRepo.findAll().stream().map(LibraryMetaDto::new).toList();
    }

}
