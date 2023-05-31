package com.scaling.libraryservice.commons.updater.service;

import com.scaling.libraryservice.commons.api.apiConnection.KakaoBookConn;
import com.scaling.libraryservice.commons.updater.dto.BookApiDto;
import com.scaling.libraryservice.commons.updater.entity.UpdateBook;
import com.scaling.libraryservice.commons.updater.repository.BookUpdateRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Book 데이터 셋을 최신화 하는 역할을 합니다. 최신화가 필요한 데이터 목록을 조회 한 뒤, 외부 API의 도움을 받아
 * 데이터를 갱신 합니다.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BookUpdateService {

    private final BookUpdateRepository bookUpdateRepo;

    private final KakaoBookApiService kakaoBookApiService;

    @Transactional
    public void UpdateBookFromApi(int limit, int nThreads) {

        List<UpdateBook> bookList = getBooks(limit);

        if (bookList.isEmpty()) {
            return;
        }


        log.info("Book Updater is starting from [{}]", bookList.get(0).getId());

        Map<String, UpdateBook> bookMap = generateBookMap(bookList);

        List<KakaoBookConn> kakaoBookConns = bookList.stream()
            .map(KakaoBookConn::new).toList();

        List<BookApiDto> bookApiDtoList = kakaoBookApiService.getBookMulti(
            kakaoBookConns, nThreads);

        updateBookEntity(bookApiDtoList, bookMap);
    }

    /**
     * DB에서 가져올 최신화가 필요한 도서 목록의 제한 개수를 인자로 받아,
     * 해당 도서 목록을 가져옵니다.
     * @param limit DB에서 가져올 도서 목록의 제한 개수
     * @return 최신화가 필요한 도서 목록
     */

    private List<UpdateBook> getBooks(int limit) {

        return bookUpdateRepo.findBooksWithLimit(limit);
    }

    /**
     * 외부 API로부터 최신화 데이터를 제공받지 못한 도서 목록을 DB에서 삭제합니다.
     * @param lastId 마지막으로 업데이트된 도서 id 번호
     */
    private void deleteNotFoundBook(long lastId) {
        bookUpdateRepo.deleteNotFoundBook(lastId);
    }

    /**
     * 외부 API로부터 도서 상세 정보 데이터를 받아와, DB의 도서 데이터를 최신화합니다.
     * 외부 API에서 해당 도서 정보를 제공하지 않는 도서는 DB에서 삭제 처리됩니다.
     */



    private Map<String, UpdateBook> generateBookMap(List<UpdateBook> bookList){

        Map<String, UpdateBook> bookMap = new HashMap<>();
        bookList.forEach(b -> bookMap.put(b.getIsbn(),b));

        return bookMap;
    }

    private void updateBookEntity(List<BookApiDto> bookApiDtoList,Map<String, UpdateBook> map ){

        long last = 1;

        for (BookApiDto dto : bookApiDtoList) {

            UpdateBook updateBook = map.get(dto.getIsbn());
            if (updateBook != null) {
                updateBook.setTitle(dto.getTitle());
                updateBook.setAuthor(dto.getAuthors());
                updateBook.setContent(dto.getContents());
                updateBook.setBookImg(dto.getThumbnail());
                updateBook.setPublisher(dto.getPublisher());
                updateBook.setAuthor(dto.getAuthors());
                updateBook.setDatatime(dto.getDateTime());

                last = updateBook.getId();
            }
        }

        deleteNotFoundBook(last);
        log.info("book update is completed until [{}]", last);
    }

}
