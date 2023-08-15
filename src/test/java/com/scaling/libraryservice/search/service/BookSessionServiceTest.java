package com.scaling.libraryservice.search.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.scaling.libraryservice.search.dto.BookDto;
import com.scaling.libraryservice.search.dto.MetaDto;
import com.scaling.libraryservice.search.dto.MetaDtoFactory;
import com.scaling.libraryservice.search.dto.RespBooksDto;
import com.scaling.libraryservice.search.dto.RespBooksDtoFactory;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;

@ExtendWith(MockitoExtension.class)
class BookSessionServiceTest {
    
    @InjectMocks
    BookSessionService bookSessionService;
    
    @Test @DisplayName("Session에 도서 검색 응답 결과를 저장 할 수 있다")
    public void keepBooksInSession(){
        /* given */
        MetaDto metaDto = MetaDtoFactory.createOneMetaDto("태엽 감는 새 연대기");
        BookDto bookDto = BookDto.builder().title("태엽 감는 새 연대기").build();
        HttpSession httpSession = new MockHttpSession();

        RespBooksDto respBooksDto = RespBooksDtoFactory.createSessionRespBookDto(metaDto,bookDto);
        
        /* when */

        bookSessionService.keepBooksInSession(httpSession,respBooksDto,3);
        Object keepingBook = httpSession.getAttribute("태엽 감는 새 연대기");

        /* then */

        assertNotNull(keepingBook);
    }

    @Test @DisplayName("session에 저장된 검색 결과와 일치하는 검색어를 반환 할 수 있다")
    public void getBook_InSession(){
        /* given */

        String userQuery = "태엽 감는 새 연대기";

        MetaDto metaDto = MetaDtoFactory.createOneMetaDto(userQuery);
        BookDto bookDto = BookDto.builder().title(userQuery).build();
        HttpSession httpSession = new MockHttpSession();

        RespBooksDto respBooksDto = RespBooksDtoFactory.createSessionRespBookDto(metaDto,bookDto);

        bookSessionService.keepBooksInSession(httpSession,respBooksDto,3);

        /* when */

        Optional<RespBooksDto> bookDtoFromSession = bookSessionService.getBookDtoFromSession(
            userQuery, httpSession);

        /* then */

        assertNotNull(bookDtoFromSession);

    }

    @Test @DisplayName("session에 저장된 검색 결과와 일치하지 않으면 빈값을 반환 한다")
    public void not_getBook_InSession(){
        /* given */

        String userQuery = "태엽 감는 새 연대기";
        String anotherQuery = "아프니까 청춘이다";

        MetaDto metaDto = MetaDtoFactory.createOneMetaDto(userQuery);
        BookDto bookDto = BookDto.builder().title(userQuery).build();
        HttpSession httpSession = new MockHttpSession();

        RespBooksDto respBooksDto = RespBooksDtoFactory.createSessionRespBookDto(metaDto,bookDto);

        bookSessionService.keepBooksInSession(httpSession,respBooksDto,3);

        /* when */

        Optional<RespBooksDto> bookDtoFromSession = bookSessionService.getBookDtoFromSession(
            anotherQuery, httpSession);

        /* then */

        assertEquals(bookDtoFromSession,Optional.empty());

    }
    
}