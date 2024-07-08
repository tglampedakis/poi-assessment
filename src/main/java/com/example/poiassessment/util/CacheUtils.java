package com.example.poiassessment.util;

public class CacheUtils {

    public static Long generateCoordsCacheKey(double lat, double lon) {
        return (long) (lat * 1000000 + lon * 1000000);
    }
}
