package com.scaling.libraryservice.util;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

// 유저가 사용자 위치 정보 제공을 동의 했을 때,
@RequiredArgsConstructor
@Component
public class CoordinateFinder {

    public Optional<? extends Location> findNearestLocation(List<? extends Location> locations,
        Location target) {

        return locations.stream().min((l1, l2) -> {

            double d1 = haversineDistance(target.getLat(), target.getLon(), l1.getLat(),
                l1.getLon());

            double d2 = haversineDistance(target.getLat(), target.getLon(), l2.getLat(),
                l2.getLon());

            return Double.compare(d1, d2);
        });
    }

    // 입력 받은 위/경도 값을 가지고, 두 좌표 사이의 거리를 계산하는 메소드
    private double haversineDistance(double lat1, double lon1, double lat2, double lon2) {
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
