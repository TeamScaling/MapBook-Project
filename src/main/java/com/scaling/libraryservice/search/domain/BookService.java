package com.scaling.libraryservice.search.domain;

import com.scaling.libraryservice.search.dto.SearchResponseDto;
import com.scaling.libraryservice.search.entity.Book;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class BookService {

    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    public SearchResponseDto searchBook(String title_nm){

//
//        if(title_nm.equals("") || title_nm.equals(" ")){
//            throw new IllegalArgumentException("검색어를 입력하세요.");
//        }

        long startTime = System.currentTimeMillis();

        List<Book> documents = bookRepository.findBooksByTitle_nm(title_nm);
        System.out.println(documents);
        for (Book book  : documents){
            log.info(book.getTitle_nm());
        }
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;

        System.out.println("작업에 소요된 시간: " + elapsedTime + "ms");

        return new SearchResponseDto(documents);
    }
}

//        String[] parsed = title_nm.split("\\b");
//        log.info(Arrays.toString(parsed));
//        List<Book> book = bookRepository.findByTitle_nm(title_nm);
//        if(book == null)
//            throw new NullPointerException("해당 값이 존재하지 않습니다.");
// title_nm이 들어간 책들을 다 불러오기


// 10개가 넘을 경우 페이지 형식으로 나타내기 (순서는 일단 상관 x)
//        Pageable pageable = PageRequest.of(page, 9, Sort.by("createdate").descending());
//        Page<Post> entityPage = postRepository.findByUserOrderByCreatedateDesc(user, pageable);
//                , Sort.by("createdate").descending());
