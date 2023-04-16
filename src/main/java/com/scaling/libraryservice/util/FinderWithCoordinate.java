package com.scaling.libraryservice.util;

import com.scaling.libraryservice.entity.Library;
import com.scaling.libraryservice.repository.LibraryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

// 유저가 사용자 위치 정보 제공을 동의 했을 때,
@RequiredArgsConstructor
@Component
public class FinderWithCoordinate implements LibraryFinder {

    private List<Library> libraries;

    private final LibraryRepository libraryRepository;

    @Override
    public List<Library> findAroundLibraries(Location location) {

        if(libraries == null){

            libraries = libraryRepository.findAll();
        }

        Library library1 = libraries.get(0);
        Library library2 = libraries.get(1);

        System.out.println(haversineDistance(library1.getLibLa(),library1.getLibLo(),library2.getLibLa(),library2.getLibLo()));

        return null;
    }

    // 입력 받은 위/경도 값을 가지고, 두 좌표 사이의 거리를 계산하는 메소드
    public double haversineDistance(double lat1, double lon1,double lat2, double lon2){
        final int earthRadiusKm = 6371;
        double latDiff = Math.toRadians(lat2 - lat1);
        double lonDiff = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2) +
            Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(lonDiff / 2) * Math.sin(lonDiff / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return earthRadiusKm * c;
    }

}
