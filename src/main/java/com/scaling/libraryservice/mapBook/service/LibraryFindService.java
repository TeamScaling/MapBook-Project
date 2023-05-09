package com.scaling.libraryservice.mapBook.service;

import com.scaling.libraryservice.commons.timer.Timer;
import com.scaling.libraryservice.mapBook.dto.LibraryDto;
import com.scaling.libraryservice.mapBook.dto.ReqMapBookDto;
import com.scaling.libraryservice.mapBook.exception.LocationException;
import com.scaling.libraryservice.mapBook.repository.HasBookAreaRepository;
import com.scaling.libraryservice.mapBook.repository.LibraryHasBookRepository;
import com.scaling.libraryservice.mapBook.repository.LibraryRepository;
import com.scaling.libraryservice.mapBook.util.HaversineCalculater;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
@Setter
@Getter
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

        log.info("This is supported Area");

        String isbn13 = reqMapBookDto.getIsbn();
        Integer areaCd = reqMapBookDto.getAreaCd();

        List<LibraryDto> result
            = libraryHasBookRepo.findHasBookLibraries(isbn13, areaCd)
            .stream()
            .map(l -> new LibraryDto(l, "Y",true))
            .toList();

        if (result.isEmpty()) {
            log.info(areaCd + " 이 지역의 도서관 중 소장 하는 도서관 없음");

            return getNearByLibraries(areaCd).stream().toList();
        }

        return result;
    }


    /**
     * 주변 대출 가능 도서관 찾기 요청 Dto에 담긴 위치 정보를 지역 코드로 변환 한다.
     *
     * @param reqMapBookDto 위치 정보를 참고할 사용자 요청 Dto
     * @return 위치 정보에 의해 변환 된 커스텀 지역 코드
     * @throws LocationException 사용자 요청에 담긴 위치 정보가 유효하지 않을 경우.
     */
    public void outPutAreaCd(ReqMapBookDto reqMapBookDto) throws LocationException {

        if (!reqMapBookDto.isValidCoordinate()) {
            throw new LocationException("잘못된 위치 정보");
        }

        Integer areaCd = findNearestLibrary(reqMapBookDto).getAreaCd();

        reqMapBookDto.updateAreaCd(areaCd);
    }

    /**
     * 사용자 요청 중 위/경도 데이터를 사용하여 가장 가까운 도서관을 찾아 반환 한다.
     *
     * @param reqMapBookDto 위치 정보를 참고할 사용자 요청 Dto
     * @return 사용자와 가장 가까운 도서관 정보를 담은 Dto
     * @throws LocationException 위/경도 데이터를 가지고 가장 가까운 도서관 찾는데 실패할 경우
     */
    private LibraryDto findNearestLibrary(ReqMapBookDto reqMapBookDto)
        throws LocationException {

        return libraryRepo.findAll().stream().min((l1, l2) -> {

            double d1 = HaversineCalculater.calculateDistance(
                reqMapBookDto.getLat(), reqMapBookDto.getLon(), l1.getLibLat(), l1.getLibLon());

            double d2 = HaversineCalculater.calculateDistance(
                reqMapBookDto.getLat(), reqMapBookDto.getLon(), l2.getLibLat(), l2.getLibLon());

            return Double.compare(d1, d2);
        }).map(LibraryDto::new)
            .orElseThrow(() -> new LocationException("최단 거리 도서관 찾기 실패"));
    }

    /** 주어진 지역 코드가 소장 가능 도서관 서비스 지역이면 true를 반환 한다.
     *
     * @param reqMapBookDto 위치 정보를 참고할 사용자 요청 Dto
     * @return areaCd를 지원 중인 도서관 코드 목록에서 찾을 수 있다면 true, 그렇지 않다면 false
     */
    private boolean isSupportedArea(ReqMapBookDto reqMapBookDto) {

        Objects.requireNonNull(reqMapBookDto);

        boolean isSupported = hasBookAreaRepo.findById(reqMapBookDto.getAreaCd()).isPresent();

        reqMapBookDto.setSupportedArea(true);

        return isSupported;
    }

}
