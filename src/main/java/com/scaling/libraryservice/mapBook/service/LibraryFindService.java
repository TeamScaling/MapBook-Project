package com.scaling.libraryservice.mapBook.service;

import com.scaling.libraryservice.aop.Timer;
import com.scaling.libraryservice.mapBook.dto.LibraryDto;
import com.scaling.libraryservice.mapBook.dto.ReqMapBookDto;
import com.scaling.libraryservice.mapBook.entity.LibraryMeta;
import com.scaling.libraryservice.mapBook.exception.LocationException;
import com.scaling.libraryservice.mapBook.repository.LibraryMetaRepository;
import com.scaling.libraryservice.mapBook.repository.LibraryRepository;
import com.scaling.libraryservice.mapBook.util.HaversineCalculater;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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

    private List<LibraryDto> libraries;
    private final LibraryRepository libraryRepo;
    private final LibraryMetaRepository libraryMetaRepo;

    @PostConstruct
    private void init() {

        // DB에 담겨진 lib_info (도서관 정보)를 빈이 생성될 때, 함께 List로 가져온다.
        this.libraries = libraryRepo.findAll().stream().map(LibraryDto::new).toList();
    }

    // 사용자의 위치 정보 또는 주소 선택 정보에 따라 사용자 주변의 도서관을 반환 한다.
    @Timer
    public List<LibraryDto> findNearByLibraries(ReqMapBookDto userLocation)
        throws LocationException {

        // 사용자의 위치 정보(위도/경도)가 없고, 찾고자 하는 도서관 지역을 선택 했을 때,
        if (userLocation.isAddressRequest()) {

            return findNearestLibraryWithAddress(userLocation);
        } else {

            if (!userLocation.isValidCoordinate()) {
                throw new LocationException("잘못된 위치 정보");
            }

            // 사용자가 위치 정보 방식으로 요청 했을 때, 전달 받은 사용자의 위도/경도를 기준으로 가장 가까운 도서관을 찾는다.
            LibraryDto nearestLibrary
                = findNearestLibraryWithCoordinate(userLocation);

            return getNearByLibraries(nearestLibrary.getAreaCd());
        }
    }

    // 사용자 근처의 도서관이 아닌 선택한 지역의 도서관
    public List<LibraryDto> findLibraries(int areaCd) throws LocationException {

        var result
            = libraries.stream().filter(l -> l.getAreaCd() == areaCd).toList();

        if (result.isEmpty()) {
            throw new LocationException("[존재하지 않는 area Code] area Code : " + areaCd);
        }

        return result;
    }

    // viewSearch.html에서 참고할 도서관 메타 정보.
    public List<LibraryMeta> getLibraryMeta() {

        return libraryMetaRepo.findAll();
    }

    // 사용자의 위치 정보와 가장 거리가 가까운 도서관을 찾는다.
    @Timer
    public LibraryDto findNearestLibraryWithCoordinate(ReqMapBookDto userLocation)
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
    private List<LibraryDto> findNearestLibraryWithAddress(ReqMapBookDto userLocation) {

        Objects.requireNonNull(userLocation);

        return getNearByLibraries(userLocation.getOneArea(), userLocation.getTwoArea());
    }

    // oneArea - '도/특별시/광역시' / twoArea - '시/군/구'
    private List<LibraryDto> getNearByLibraries(String oneArea, String twoArea) {

        return libraries.stream()
            .filter(i -> i.getOneAreaNm().equals(oneArea)
                & i.getTwoAreaNm().equals(twoArea))
            .toList();
    }

    // 사용자 근처의 도서관 areaCd - 도/특별시/광역시 + 시/군/구를 합쳐서 만든 지역 code
    private List<LibraryDto> getNearByLibraries(int areaCd) {

        return libraries.stream()
            .filter(i -> i.getAreaCd() == areaCd).toList();
    }


}
