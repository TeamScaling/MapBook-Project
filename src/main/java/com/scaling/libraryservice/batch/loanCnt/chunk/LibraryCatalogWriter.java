package com.scaling.libraryservice.batch.loanCnt.chunk;

import com.scaling.libraryservice.batch.loanCnt.domain.LibraryCatalog;
import com.scaling.libraryservice.batch.bookUpdate.entity.RequiredUpdateBook;
import com.scaling.libraryservice.search.entity.Book;
import com.scaling.libraryservice.search.repository.BookRepository;
import com.scaling.libraryservice.search.repository.RequiredUpdateBookRepo;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LibraryCatalogWriter implements ItemWriter<LibraryCatalog> {

    private final BookRepository bookRepository;

    private final RequiredUpdateBookRepo requiredUpdateBookRepo;

    @Override
    @Transactional
    public void write(List<? extends LibraryCatalog> items) throws Exception {

        Map<String, Integer> libraryCatalogMap = items.stream()
            .collect(Collectors.toMap(
                LibraryCatalog::getIsbn,
                LibraryCatalog::getLoanCnt)
            );

        Set<String> isbnSet = libraryCatalogMap.keySet();

        bookRepository.findBookByIsbnIn(isbnSet).forEach(book -> {
            Integer loanCnt = libraryCatalogMap.remove(book.getIsbn());
            book.setLoanCnt(loanCnt);
        });

        saveRequiredUpdateBook(libraryCatalogMap);
    }

    private void saveRequiredUpdateBook(Map<String, Integer> libraryCatalogMap) {

        List<RequiredUpdateBook> requiredUpdateBooks = libraryCatalogMap.entrySet().stream()
            .map(entry ->
                new RequiredUpdateBook(entry.getKey(), entry.getValue()))
            .toList();

        requiredUpdateBookRepo.saveAll(requiredUpdateBooks);
    }
}