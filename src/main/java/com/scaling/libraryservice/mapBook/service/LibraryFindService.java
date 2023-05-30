package com.scaling.libraryservice.mapBook.service;

import com.scaling.libraryservice.commons.timer.Timer;
import com.scaling.libraryservice.mapBook.dto.LibraryDto;
import com.scaling.libraryservice.mapBook.dto.ReqMapBookDto;
import com.scaling.libraryservice.mapBook.exception.LocationException;
import com.scaling.libraryservice.mapBook.repository.HasBookAreaRepository;
import com.scaling.libraryservice.mapBook.repository.LibraryHasBookRepository;
import com.scaling.libraryservice.mapBook.repository.LibraryRepository;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class LibraryFindService {
    private final LibraryRepository libraryRepo;
    private final LibraryHasBookRepository libraryHasBookRepo;
    private final HasBookAreaRepository hasBookAreaRepo;


    /**
     * 주변 대출 가능 도서관 찾기 요청 Dto에서 위치 정보를 참고하여, 사용자 주변의 도서관 데이터를 반환 한다.
     *
     * @param reqMapBookDto 위치 정보를 참고할 사용자 요청 Dto
     * @return 사용자 주변 도서관 정보 Dto를 담는 List
     * @throws LocationException 사용자의 위치 정보가 대한민국 범위 밖일 경우
     */
    @Timer
    public List<LibraryDto> getNearByLibraries(ReqMapBookDto reqMapBookDto)
        throws LocationException {

        Objects.requireNonNull(reqMapBookDto);

        return isSupportedArea(reqMapBookDto) ?
            getNearByHasBookLibraries(reqMapBookDto) :
            getNearByLibraries(reqMapBookDto.getAreaCd());
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


    List<LibraryDto> getNearByHasBookLibraries(ReqMapBookDto reqMapBookDto) {

        log.info("This is support Area");

        String isbn13 = reqMapBookDto.getIsbn();
        int areaCd = reqMapBookDto.getAreaCd();

        return libraryHasBookRepo.findHasBookLibraries(isbn13, areaCd)
            .stream()
            .map(l -> new LibraryDto(l, "Y",true))
            .toList();
    }

    /** 주어진 지역 코드가 소장 가능 도서관 서비스 지역이면 true를 반환 한다.
     *
     * @param reqMapBookDto 위치 정보를 참고할 사용자 요청 Dto
     * @return areaCd를 지원 중인 도서관 코드 목록에서 찾을 수 있다면 true, 그렇지 않다면 false
     */
    private boolean isSupportedArea(ReqMapBookDto reqMapBookDto) {

        Objects.requireNonNull(reqMapBookDto);

        boolean isSupported = hasBookAreaRepo.findById(reqMapBookDto.getAreaCd()).isPresent();

        if(isSupported) reqMapBookDto.setSupportedArea(true);

        return isSupported;
    }

}
