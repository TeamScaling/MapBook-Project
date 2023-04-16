package com.scaling.libraryservice.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FinderWithCoordinateTest {

    @Autowired
    private FinderWithCoordinate finder;

    @Test
    public void test(){
        /* given */

        finder.findAroundLibraries(new Location());

        /* when */

        /* then */
    }

}