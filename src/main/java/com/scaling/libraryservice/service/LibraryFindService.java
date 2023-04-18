package com.scaling.libraryservice.service;

import com.scaling.libraryservice.aop.Timer;
import com.scaling.libraryservice.dto.LibraryDto;
import com.scaling.libraryservice.dto.ReqMapBookDto;
import com.scaling.libraryservice.exception.LocationException;
import com.scaling.libraryservice.repository.LibraryRepository;
import com.scaling.libraryservice.util.HaversineCalculater;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class LibraryFindService {

    private List<LibraryDto> libraries;
    private final LibraryRepository libraryRepo;

    @PostConstruct
    private void init() {

        // DB에 담겨진 lib_info (도서관 정보)를 빈이 생성될 때, 함께 List로 가져온다.
        this.libraries = libraryRepo.findAll().stream().map(LibraryDto::new).toList();
    }

    @Timer
    public List<LibraryDto> findNearByLibraries(ReqMapBookDto userLocation) {

        // 사용자의 위치 정보(위도/경도)가 없고, 찾고자 하는 도서관 지역을 선택 했을 때,
        if (userLocation.isAddressRequest()) {

            return findNearestLibraryWithAddress(userLocation);
        } else {

            // 사용자가 위치 정보 방식으로 요청 했을 때, 전달 받은 사용자의 위도/경도를 기준으로 가장 가까운 도서관을 찾는다.
            LibraryDto nearestLibrary
                = findNearestLibraryWithCoordinate(userLocation).orElseThrow(LocationException::new);

            return getNearByLibraries(nearestLibrary.getOneAreaNm(),nearestLibrary.getTwoAreaNm());
        }

    }

    @Timer
    public Optional<? extends LibraryDto> findNearestLibraryWithCoordinate(ReqMapBookDto userLocation) {

        // 사용자의 위치 정보와 가장 거리가 가까운 도서관을 찾는다.
        return libraries.stream().min((l1, l2) -> {

            double d1 = HaversineCalculater.calculateDistance(
                userLocation.getLat(), userLocation.getLon(), l1.getLibLat(), l1.getLibLon());

            double d2 = HaversineCalculater.calculateDistance(
                userLocation.getLat(), userLocation.getLon(), l2.getLibLat(), l2.getLibLon());

            return Double.compare(d1, d2);
        });
    }

    // 사용자가 위치 정보 대신 주소 방식을 선택 했을 때, 호출 된다.
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


}
