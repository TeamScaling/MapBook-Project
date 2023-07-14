package com.scaling.libraryservice.mapBook.service;

import com.scaling.libraryservice.commons.timer.Timer;
import com.scaling.libraryservice.mapBook.dto.LibraryDto;
import com.scaling.libraryservice.mapBook.exception.LocationException;
import com.scaling.libraryservice.mapBook.repository.HasBookAreaRepository;
import com.scaling.libraryservice.mapBook.repository.LibraryHasBookRepository;
import com.scaling.libraryservice.mapBook.repository.LibraryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

/**
 * 사용자의 지역 코드와 국제 도서 표준 번호(ISBN13)를 이용하여 사용자 주변의 도서관 데이터를 조회하고 반환하는 서비스 클래스입니다.
 *
 * <p>이 클래스는 두 가지 주요 기능을 제공합니다:</p>
 * <ul>
 * <li>특정 도서가 사용자 주변 도서관에 소장되어 있는지 확인하고, 소장 도서관 정보를 반환합니다.</li>
 * <li>사용자 주변 도서관 정보를 반환합니다.</li>
 * </ul>
 *
 * <p>이 클래스는 {@link LibraryRepository}, {@link LibraryHasBookRepository},
 * {@link HasBookAreaRepository}를 사용하여 도서관 정보와 도서 소장 정보를 조회합니다.</p>
 *
 * <p>{@link #getNearByLibraries(String, Integer)} 메소드는 지역 코드와 ISBN13을 이용하여 사용자 주변의 도서관 데이터를 반환하며,
 * {@link #getNearByLibraries(Integer)} 메소드는 지역 코드만을 이용하여 사용자 주변의 도서관 데이터를 반환합니다.</p>
 *
 * <p>또한, {@link #isSupportedArea(Integer)} 메소드를 이용하여 지정된 지역 코드가 도서 소장 가능 지역인지 판단할 수 있습니다.</p>
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class LibraryFindService {
    private final LibraryRepository libraryRepo;
    private final LibraryHasBookRepository libraryHasBookRepo;
    private final HasBookAreaRepository hasBookAreaRepo;


    /**
     * 국제 도서 표준 번호(isbn13)과 지역 코드를 사용해 사용자 주변의 도서관 데이터를 반환 한다.
     *
     * @param isbn13 검색할 도서의 국제 도서 표준 변호
     * @param areaCd 특정 지역 단위에 대한 지역 코드
     * @return 사용자 주변 도서관 정보 Dto를 담는 List
     * @throws LocationException 사용자의 위치 정보가 대한민국 범위 밖일 경우
     */
    @Timer
    public List<LibraryDto> getNearByLibraries(@NonNull String isbn13, Integer areaCd)
        throws LocationException {

        return isSupportedArea(areaCd) ?
            getNearByHasBookLibraries(isbn13,areaCd) :
            getNearByLibraries(areaCd);
    }

    /**
     * 지역 코드 방식의 사용자 요청일 경우 주변 도서관을 찾아서 반환 한다.
     *
     * @param areaCd 특정 지역 단위에 대한 지역 코드
     * @return 지역 코드를 통해 찾은 주변 도서관 정보 Dto를 담은 List
     */
    public List<LibraryDto> getNearByLibraries(Integer areaCd) {

        return libraryRepo.findAllByAreaCd(areaCd).stream()
            .map(LibraryDto::new)
            .toList();
    }


    List<LibraryDto> getNearByHasBookLibraries(String isbn13,Integer areaCd) {

        log.info("This is support Area");

        return libraryHasBookRepo.findHasBookLibraries(isbn13, areaCd)
            .stream()
            .map(l -> new LibraryDto(l, "Y",true))
            .toList();
    }

    /** 주어진 지역 코드가 소장 가능 도서관 서비스 지역이면 true를 반환 한다.
     *
     * @param areaCd 특정 지역 단위에 대한 지역 코드
     * @return areaCd를 지원 중인 도서관 코드 목록에서 찾을 수 있다면 true, 그렇지 않다면 false
     */
    private boolean isSupportedArea(Integer areaCd) {

        return hasBookAreaRepo.findById(areaCd).isPresent();
    }

    public List<LibraryDto> getAllLibraries(){

        return libraryRepo.findAll().stream().map(LibraryDto::new).toList();
    }

}
