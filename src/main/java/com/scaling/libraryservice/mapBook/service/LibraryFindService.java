package com.scaling.libraryservice.mapBook.service;

import com.scaling.libraryservice.aop.Timer;
import com.scaling.libraryservice.mapBook.dto.LibraryDto;
import com.scaling.libraryservice.mapBook.dto.LibraryMetaDto;
import com.scaling.libraryservice.mapBook.dto.ReqMapBookDto;
import com.scaling.libraryservice.mapBook.exception.LocationException;
import com.scaling.libraryservice.mapBook.repository.LibraryMetaRepository;
import com.scaling.libraryservice.mapBook.repository.LibraryRepository;
import com.scaling.libraryservice.mapBook.util.HaversineCalculater;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    private static List<LibraryDto> libraries;
    private final LibraryRepository libraryRepo;
    private final LibraryMetaRepository libraryMetaRepo;

    @PostConstruct
    private void init() {

        // DB에 담겨진 lib_info (도서관 정보)를 빈이 생성될 때, 함께 List로 가져온다.
        libraries = libraryRepo.findAll().stream().map(LibraryDto::new).toList();
    }

    // 사용자의 위치 정보 또는 주소 선택 정보에 따라 사용자 주변의 도서관을 반환 한다.
    @Timer
    public List<LibraryDto> getNearByLibraries(ReqMapBookDto userLocation)
        throws LocationException {

        LibraryDto nearestLibrary;

        if (userLocation.isAddressRequest()) {
            nearestLibrary = findNearestLibraryWithAddress(userLocation);
        } else {
            if (!userLocation.isValidCoordinate()) {
                throw new LocationException("잘못된 위치 정보");
            }
            nearestLibrary = findNearestLibraryWithCoordinate(userLocation);
        }
        return getNearByLibraries(nearestLibrary.getAreaCd());
    }

    // 사용자 근처의 도서관이 아닌 선택한 지역의 도서관
    public List<LibraryDto> getNearByLibraries(int areaCd) throws LocationException {

        List<LibraryDto> result
            = libraries.stream().filter(l -> l.getAreaCd() == areaCd).toList();

        if (result.isEmpty()) {
            throw new LocationException("[존재하지 않는 area Code] area Code : " + areaCd);
        }
        return result;
    }

    // viewSearch.html에서 참고할 도서관 메타 정보.
    public List<LibraryMetaDto> getLibraryMeta() {

        return libraryMetaRepo.findAll().stream().map(LibraryMetaDto::new).toList();
    }

    public List<LibraryDto> getAllLibraries() {

        return new ArrayList<>(libraries);
    }


    public static Integer outPutAreaCd(ReqMapBookDto reqMapBookDto) {

        if (reqMapBookDto.isAddressRequest()) {
            return findNearestLibraryWithAddress(reqMapBookDto).getAreaCd();
        } else {
            if (!reqMapBookDto.isValidCoordinate()) {
                throw new LocationException("잘못된 위치 정보");
            }
            return findNearestLibraryWithCoordinate(reqMapBookDto).getAreaCd();
        }
    }


    // 사용자의 위치 정보와 가장 거리가 가까운 도서관을 찾는다.
    public static LibraryDto findNearestLibraryWithCoordinate(ReqMapBookDto userLocation)
        throws LocationException {

        return libraries.stream().min((l1, l2) -> {

            double d1 = HaversineCalculater.calculateDistance(
                userLocation.getLat(), userLocation.getLon(), l1.getLibLat(), l1.getLibLon());

            double d2 = HaversineCalculater.calculateDistance(
                userLocation.getLat(), userLocation.getLon(), l2.getLibLat(), l2.getLibLon());

            return Double.compare(d1, d2);
        }).orElseThrow(() -> new LocationException("최단 거리 도서관 찾기 실패"));
    }

    // 사용자가 위치 정보 대신 주소 방식을 선택 했을 때 호출 된다.
    private static LibraryDto findNearestLibraryWithAddress(ReqMapBookDto userLocation) {

        Objects.requireNonNull(userLocation);

        // oneArea - '도/특별시/광역시' / twoArea - '시/군/구'
        return libraries.stream()
            .filter(i -> i.getOneAreaNm().equals(userLocation.getOneArea())
                & i.getTwoAreaNm().equals(userLocation.getTwoArea())).findFirst().orElseThrow();
    }

}
