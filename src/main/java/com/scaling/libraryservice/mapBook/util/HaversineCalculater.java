package com.scaling.libraryservice.mapBook.util;

import com.scaling.libraryservice.commons.timer.Timer;
import lombok.RequiredArgsConstructor;

// 사용자의 위도/경도를 가지고 사용자와 가장 가까운 도서관을 찾기 위한 클래스
@RequiredArgsConstructor
public class HaversineCalculater {

    @Timer
    // 입력 받은 위/경도 값을 가지고, haversine 공식을 통해 거리를 계산 한다.
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
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
