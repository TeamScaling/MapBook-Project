package com.scaling.libraryservice.mapBook.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.scaling.libraryservice.commons.caching.CacheKey;
import com.scaling.libraryservice.commons.caching.CustomCacheManager;
import com.scaling.libraryservice.commons.caching.CustomCacheable;
import com.scaling.libraryservice.commons.timer.Timer;
import com.scaling.libraryservice.mapBook.dto.LibraryDto;
import com.scaling.libraryservice.mapBook.dto.ReqMapBookDto;
import com.scaling.libraryservice.mapBook.entity.Library;
import com.scaling.libraryservice.mapBook.exception.LocationException;
import com.scaling.libraryservice.mapBook.repository.LibraryHasBookRepository;
import com.scaling.libraryservice.mapBook.repository.LibraryMetaRepository;
import com.scaling.libraryservice.mapBook.repository.LibraryRepository;
import com.scaling.libraryservice.mapBook.util.HaversineCalculater;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
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

    private static List<Library> libraries;
    private final LibraryRepository libraryRepo;
    private final LibraryMetaRepository libraryMetaRepo;
    private final LibraryHasBookRepository libraryHasBookRepo;
    private static List<Integer> hbSupportedArea;
    private final CustomCacheManager<List<LibraryDto>> cacheManager;

    @PostConstruct
    private void init() {

        // DB에 담겨진 lib_info (도서관 정보)를 빈이 생성될 때, 함께 List로 가져온다.
        libraries = libraryRepo.findAll();
        hbSupportedArea = libraryHasBookRepo.findSupportedArea();

        Cache<CacheKey, List<LibraryDto>> libraryCache = Caffeine.newBuilder()
            .expireAfterWrite(1, TimeUnit.HOURS)
            .maximumSize(1000)
            .build();

        cacheManager.registerCaching(libraryCache, this.getClass());
    }

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

        if (reqMapBookDto.getAreaCd() == null) {
            reqMapBookDto.updateAreaCd();
        }

        return isSupportedArea(reqMapBookDto.getAreaCd()) ?
            getNearByHasBookLibraries(reqMapBookDto.getIsbn(), reqMapBookDto.getAreaCd()) :
            getNearByAllLibraries(reqMapBookDto);
    }

    /**
     * 주소 선택 방식의 사용자 요청일 경우 주변 도서관을 찾아서 반환 한다.
     *
     * @param reqMapBookDto 위치 정보를 참고할 사용자 요청 Dto
     * @return 사용자 주변 도서관 정보 Dto를 담는 List
     */
    private List<LibraryDto> getNearByAllLibraries(ReqMapBookDto reqMapBookDto) {

        return libraries.stream()
            .filter(l -> Objects.equals(l.getAreaCd(), reqMapBookDto.getAreaCd()))
            .map(LibraryDto::new)
            .toList();
    }

    /**
     * 지역 코드 방식의 사용자 요청일 경우 주변 도서관을 찾아서 반환 한다.
     *
     * @param areaCd 특정 지역 단위에 대한 지역 코드
     * @return 지역 코드를 통해 찾은 주변 도서관 정보 Dto를 담은 List
     */
    public List<LibraryDto> getNearByAllLibraries(Integer areaCd) {

        return libraries.stream()
            .filter(l -> Objects.equals(l.getAreaCd(), areaCd))
            .map(LibraryDto::new)
            .toList();
    }

    /**
     * 도서 번호와 지역 코드를 통해 해당 도서를 소장 하고 있는 주변 도서관 목록을 반환 한다.
     *
     * @param isbn13 찾고자 하는 도서의 13자리 국제 표준 도서 번호
     * @param areaCd 일정 규모로 묶은 지역에 대한 커스텀 코드
     * @return 도서 번호와 지역 코드를 통해 검색 된 도서 소장 도서관 정보 Dto를 담는 List
     */
    @CustomCacheable
    List<LibraryDto> getNearByHasBookLibraries(String isbn13, Integer areaCd) {

        List<LibraryDto> result
            = libraryHasBookRepo.findHasBookLibraries(Double.parseDouble(isbn13), areaCd)
            .stream().map(l -> new LibraryDto(l, "Y")).toList();

        if (result.isEmpty()) {
            log.info(areaCd + " 이 지역의 도서관 중 소장 하는 도서관 없음");

            return libraries.stream().filter(l -> Objects.equals(l.getAreaCd(), areaCd))
                .map(l -> new LibraryDto(l, "N"))
                .toList();
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
    public static Integer outPutAreaCd(ReqMapBookDto reqMapBookDto) throws LocationException {

        if (reqMapBookDto.isAddressRequest()) {
            return findNearestLibraryWithAddress(reqMapBookDto).getAreaCd();
        } else {
            if (!reqMapBookDto.isValidCoordinate()) {
                throw new LocationException("잘못된 위치 정보");
            }
            return findNearestLibraryWithCoordinate(reqMapBookDto).getAreaCd();
        }
    }

    /**
     * 사용자 요청 중 위/경도 데이터를 사용하여 가장 가까운 도서관을 찾아 반환 한다.
     *
     * @param reqMapBookDto 위치 정보 방식의 사용자 요청
     * @return 사용자와 가장 가까운 도서관 정보를 담은 Dto
     * @throws LocationException 위/경도 데이터를 가지고 가장 가까운 도서관 찾는데 실패할 경우
     */
    private static LibraryDto findNearestLibraryWithCoordinate(ReqMapBookDto reqMapBookDto)
        throws LocationException {

        return libraries.stream().map(LibraryDto::new).min((l1, l2) -> {

            double d1 = HaversineCalculater.calculateDistance(
                reqMapBookDto.getLat(), reqMapBookDto.getLon(), l1.getLibLat(), l1.getLibLon());

            double d2 = HaversineCalculater.calculateDistance(
                reqMapBookDto.getLat(), reqMapBookDto.getLon(), l2.getLibLat(), l2.getLibLon());

            return Double.compare(d1, d2);
        }).orElseThrow(() -> new LocationException("최단 거리 도서관 찾기 실패"));
    }

    /**
     * 지역 코드가 담긴 사용자 요청을 통해 가장 가까운 도서관을 찾는다.
     *
     * @param reqMapBookDto 지역 코드가 담긴 사용자 요청 Dto
     * @return 가장 가까운 도서관 정보를 담은 Dto
     * @throws LocationException - 지역 코드를 통해 가장 가까운 도서관을 찾는데 실패할 경우
     */
    private static LibraryDto findNearestLibraryWithAddress(ReqMapBookDto reqMapBookDto)
        throws LocationException {

        Objects.requireNonNull(reqMapBookDto);

        // oneArea - '도/특별시/광역시' / twoArea - '시/군/구'
        return libraries.stream().map(LibraryDto::new)
            .filter(i -> i.getOneAreaNm().equals(reqMapBookDto.getOneArea())
                & i.getTwoAreaNm().equals(reqMapBookDto.getTwoArea())).findFirst().orElseThrow(
                () -> new LocationException("지역 코드로 가장 가까운 도서관 찾기 실패")
            );
    }

    /**
     * 등록된 전국 도서관 데이터 목록을 반환 한다.
     * @return 등록 된 전국 도서관 정보 Dto를 담은 List
     */

    public static List<LibraryDto> getAllLibraries() {

        return libraries.stream().map(LibraryDto::new).toList();
    }

    /** 주어진 지역 코드가 소장 가능 도서관 서비스 지역이면 true를 반환 한다.
     *
     * @param areaCd 소장 가능 도서관을 이용 할 수 있는지 탐색할 지역 코드
     * @return areaCd를 지원 중인 도서관 코드 목록에서 찾을 수 있다면 true, 그렇지 않다면 false
     */
    private boolean isSupportedArea(Integer areaCd) {

        return hbSupportedArea.stream()
            .anyMatch(i -> Objects.equals(i, areaCd));
    }

}
