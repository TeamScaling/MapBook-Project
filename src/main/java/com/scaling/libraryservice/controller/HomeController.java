package com.scaling.libraryservice.controller;

import com.scaling.libraryservice.dto.RespBooksDto;
import com.scaling.libraryservice.service.BookSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@RequiredArgsConstructor
@Controller
public class HomeController {

	private final BookSearchService searchService;

	@GetMapping("/books")
	public ModelAndView home(){
		return new ModelAndView("main");
	}

//	@GetMapping("/books/search")
//	public ModelAndView result(@RequestParam(value = "query", required = false) String query,
//		@RequestParam(value = "page", defaultValue = "0") int page,
//		@RequestParam(value = "size", defaultValue = "10") int size) {
//		ModelAndView modelAndView = new ModelAndView("search-result");
//		if (query != null) {
//			RespBooksDto searchResult = searchService.searchBookPage(query, page, size);
//			modelAndView.addObject("searchResult", searchResult);
//			modelAndView.addObject("totalPages", searchResult.getMeta().getTotalPages());
//			modelAndView.addObject("size", searchResult.getMeta().getTotalElements());
//		}
//		return modelAndView;
//	}
	@GetMapping("/books/search")
	public ModelAndView result(@RequestParam(value = "query", required = false) String query,
		@RequestParam(value = "page", defaultValue = "0") int page,
		@RequestParam(value = "size", defaultValue = "10") int size) {
		ModelAndView modelAndView = new ModelAndView("search-result");
		if (query != null) {
			RespBooksDto searchResult = searchService.searchBooks(query, page, size);
			modelAndView.addObject("searchResult", searchResult);
			modelAndView.addObject("totalPages", searchResult.getMeta().getTotalPages());
			modelAndView.addObject("size", searchResult.getMeta().getTotalElements());

		}
		return modelAndView;
	}
}
