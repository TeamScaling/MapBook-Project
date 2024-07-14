package com.scaling.libraryservice.search.service;

import static com.scaling.libraryservice.search.dto.RespBooksDtoFactory.createSessionRespBookDto;

import com.scaling.libraryservice.search.dto.MetaDtoFactory;
import com.scaling.libraryservice.search.dto.RespBooksDto;
import com.scaling.libraryservice.search.engine.util.SubTitleRemover;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.HttpSession;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class BookSessionService {

    public void keepBooksInSession(HttpSession httpSession, RespBooksDto respBooksDto, int interval) {
        httpSession.setMaxInactiveInterval(interval);

        // 동일한 제목이 있다면 첫번째 제목의 도서 데이터 남기고 나머지 중복된 도서는 저장하지 않음.
        Map<String, RespBooksDto> bookMap = respBooksDto.getDocuments()
            .stream()
            .collect(Collectors.toMap(
                bookDto -> SubTitleRemover.removeSubTitle(bookDto.getTitle()),
                bookDto -> createSessionRespBookDto(respBooksDto.getMeta(), bookDto),
                (oldValue, newValue) -> oldValue));
        bookMap.forEach(httpSession::setAttribute);
    }

    public Optional<RespBooksDto> getBookDtoFromSession(String query, HttpSession httpSession) {
        query = SubTitleRemover.removeSubTitle(query);
        RespBooksDto respBook = (RespBooksDto) httpSession.getAttribute(query);

        if (respBook != null) {
            respBook.getMeta().changeQueryToUserQuery(query);
            return Optional.of(respBook);
        } else {
            return Optional.empty();
        }
    }

}
