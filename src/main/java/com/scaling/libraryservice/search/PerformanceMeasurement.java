package com.scaling.libraryservice.search;

public class PerformanceMeasurement {

    private long startTime;
    private long endTime;

    public void start() {
        startTime = System.currentTimeMillis();
    }

    public void end() {
        endTime = System.currentTimeMillis();
    }

    public long getElapsedTime(){
        return endTime - startTime;
    }
}

// 모듈화의 장점?! 뭐가 뭐가
//