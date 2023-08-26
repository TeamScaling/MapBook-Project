package com.scaling.libraryservice.batch.bookUpdate.chunk;

import static java.util.stream.Collectors.toMap;

import com.scaling.libraryservice.batch.bookUpdate.dto.BookApiDto;
import com.scaling.libraryservice.batch.bookUpdate.entity.RequiredUpdateBook;
import com.scaling.libraryservice.commons.api.apiConnection.KakaoBookConn;
import com.scaling.libraryservice.commons.api.service.provider.DataProvider;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class BookUpdateWriter implements ItemWriter<RequiredUpdateBook> {

    private final DataProvider<BookApiDto> kakaoBookProvider;

    @Override
    @Transactional
    public void write(List<? extends RequiredUpdateBook> items) {

        Map<String, RequiredUpdateBook> bookMap = collectToMap(items);
        List<KakaoBookConn> kakaoBookConns = mapToConnections(items);

        List<BookApiDto> bookApiDtoList = kakaoBookProvider.provideDataList(
            kakaoBookConns,
            kakaoBookConns.size()
        );

        updateBookEntity(bookApiDtoList, bookMap);
    }

    private Map<String, RequiredUpdateBook> collectToMap(List<? extends RequiredUpdateBook> items) {
        return items.stream()
            .collect(
                toMap(
                    RequiredUpdateBook::getIsbn,
                    Function.identity()
                )
            );
    }

    private List<KakaoBookConn> mapToConnections(List<? extends RequiredUpdateBook> items){
        return items.stream()
            .map(KakaoBookConn::new)
            .toList();
    }

    private void updateBookEntity(List<BookApiDto> bookApiDtoList,
        Map<String, RequiredUpdateBook> bookMap) {

        updateBookData(bookApiDtoList, bookMap);
        markNotFoundFromRemaining(bookMap);
    }

    // API로 부터 받은 도서 정보를 업데이트 한다.
    private void updateBookData(List<BookApiDto> bookApiDtoList,
        Map<String, RequiredUpdateBook> bookMap) {

        bookApiDtoList.stream()
            .filter(bookApiDto -> bookMap.containsKey(bookApiDto.getIsbn()))
            .forEach(bookApiDto ->
                bookMap.remove(bookApiDto.getIsbn())
                    .updateData(bookApiDto)
            );
    }

    // API를 통해 도서 정보를 확인 했지만 존재하지 않는 도서에 대해 체킹 한다.
    private void markNotFoundFromRemaining(Map<String, RequiredUpdateBook> bookMap) {
        bookMap.values()
            .forEach(requiredUpdateBook ->
                requiredUpdateBook.setNotFound(true)
            );
    }

}