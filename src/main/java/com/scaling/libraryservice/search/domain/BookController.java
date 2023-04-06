package com.scaling.libraryservice.search.domain;

import com.scaling.libraryservice.search.dto.SearchResponseDto;
import com.scaling.libraryservice.search.entity.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/books")
@RequiredArgsConstructor
@Controller
public class BookController {

    private final BookService bookService;

    @GetMapping("")
    public String home() {
        return "main";
    }

    @GetMapping("/search")
    public String searchBook(Model model, @RequestParam(value = "query") String title_nm) {
        SearchResponseDto searchResult = bookService.searchBook(title_nm);
        List<Book> bookList = searchResult.getDocuments(); // 검색 결과 도서 리스트
        int bookCnt = searchResult.getBookCnt();
        model.addAttribute("bookList", bookList);
        model.addAttribute("bookCnt", bookCnt);
        return "search-result"; // 검색 결과 페이지
    }
}