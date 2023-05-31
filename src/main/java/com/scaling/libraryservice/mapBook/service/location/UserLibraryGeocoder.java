package com.scaling.libraryservice.mapBook.service.location;

import com.scaling.libraryservice.mapBook.dto.LibraryDto;
import com.scaling.libraryservice.mapBook.dto.ReqMapBookDto;
import com.scaling.libraryservice.mapBook.exception.LocationException;
import com.scaling.libraryservice.mapBook.repository.LibraryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserLibraryGeocoder implements LocationResolver {

    private final LibraryRepository libraryRepo;

    /**
     * 주변 대출 가능 도서관 찾기 요청 Dto에 담긴 위치 정보를 지역 코드로 변환 한다.
     *
     * @param reqMapBookDto 위치 정보를 참고할 사용자 요청 Dto
     * @throws LocationException 사용자 요청에 담긴 위치 정보가 유효하지 않을 경우.
     */
    @Override
    public void resolve(ReqMapBookDto reqMapBookDto) throws LocationException {
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

}
