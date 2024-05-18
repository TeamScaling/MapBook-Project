package com.scaling.libraryservice.mapBook.service.location;

import com.scaling.libraryservice.commons.timer.MeasureTaskTime;


/**
 * 위도/경도 데이터 가지고 두 위치의 거리를 Haversine 공식을 이용해 계산 한다.
 */

public class HaversineCalculater {

    /**
     * 입력 받은 두 위치의 위/경도 데이터를 통해 두 위치 간의 거리를 반환 한다. 거리 계산에는 Haversine 공식을 사용 한다.
     *
     * @param lat1 1번 위치의 위도 double 값
     * @param lon1 1번 위치의 경도 double 값
     * @param lat2 2번 위치의 위도 double 값
     * @param lon2 2번 위치의 경도 double 값
     * @return 두 위치 간의 거리
     */
    @MeasureTaskTime
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
