package com.scaling.libraryservice.mapBook.util;

import static org.junit.jupiter.api.Assertions.*;

import com.scaling.libraryservice.mapBook.service.location.HaversineCalculater;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.junit.jupiter.api.Test;

class HaversineCalculaterTest {

    @Test
    void calculateDistance() {
        /* given */
        double distance = 178.429517;

        double lat1 = 37.247687;
        double lon1 = 126.604069;

        double lat2 = 38.74273402531946;
        double lon2 = 127.3437713197453;

        /* when */
        var result = HaversineCalculater.calculateDistance(lat1,lon1,lat2,lon2);

        BigDecimal bd1 = new BigDecimal(distance);
        bd1 = bd1.setScale(6, RoundingMode.HALF_UP);

        BigDecimal bd2 = new BigDecimal(result);
        bd2 = bd2.setScale(6, RoundingMode.HALF_UP);

        /* then */

        assertEquals(bd1,bd2);
    }
}