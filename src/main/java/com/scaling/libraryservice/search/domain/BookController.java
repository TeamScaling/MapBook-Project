package com.scaling.libraryservice.search.domain;

import com.scaling.libraryservice.search.dto.SearchResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("/books")
@RequiredArgsConstructor
@RestController
public class BookController {

    private final BookService bookService;

    @GetMapping("")
    public ModelAndView home(){
        return new ModelAndView("main");
    }

    @GetMapping("/search")
    public ModelAndView searchBook(Model model, @RequestParam(value = "query") String title_nm) {
        SearchResponseDto searchResponseDto = bookService.searchBook(title_nm);
        model.addAttribute("books", searchResponseDto.getDocuments());
        return new ModelAndView("search");
    }
}

// 페이징
//        @RequestParam(value = "page", defaultValue = "0") int page