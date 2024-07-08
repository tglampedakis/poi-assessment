package com.example.poiassessment.service;

import com.example.poiassessment.dto.ClosestPointDto;
import com.example.poiassessment.dto.PointDto;
import com.example.poiassessment.dto.PointRequestDto;
import com.example.poiassessment.dto.entity.Poi;
import com.example.poiassessment.exception.BadRequestException;
import com.example.poiassessment.exception.PointNotFoundException;
import com.example.poiassessment.repository.PoiRepository;
import com.example.poiassessment.util.CacheUtils;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class PointServiceImpl implements PointService {

    private final CacheManager cacheManager;
    private final Cache<Long, Poi> pointsCache;

    @Inject
    PoiRepository poiRepository;

    public PointServiceImpl() {
        cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache("pointsCache", CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, Poi.class, ResourcePoolsBuilder.heap(1000)))
                .build();

        cacheManager.init();
        pointsCache = cacheManager.getCache("pointsCache", Long.class, Poi.class);
    }

    @Override
    public void createPoint(PointRequestDto pointRequestDto) {
        if (pointRequestDto.getName() == null || pointRequestDto.getLatitude() == null || pointRequestDto.getLongitude() == null) {
            throw new BadRequestException("All request fields must have a value.");
        }

        poiRepository.savePoint(pointRequestDto);
    }

    @Override
    public ClosestPointDto getClosestPoint(double lat, double lon) {
        Long cacheKey = CacheUtils.generateCoordsCacheKey(lat, lon);

        // return the cached entity - if already cached
        if (pointsCache.get(cacheKey) != null) return getBuiltClosestPoint(pointsCache.get(cacheKey));

        List<Poi> nearbyPoints = poiRepository.retrieveClosestPoint(lat, lon);

        if (!nearbyPoints.isEmpty()) {
            Poi superPoint = nearbyPoints.get(0);

            poiRepository.incrementPointCounter(superPoint);

            // cache
            pointsCache.put(cacheKey, superPoint);

            return getBuiltClosestPoint(superPoint);
        }

        return ClosestPointDto.builder().build();
    }

    private static ClosestPointDto getBuiltClosestPoint(Poi superPoint) {
        return ClosestPointDto.builder()
                .name(superPoint.getName())
                .build();
    }

    @Override
    public List<PointDto> getHigherCounterPoints(Integer threshold) {
        List<Poi> pointsEntityList = poiRepository.retrieveHigherCounterPoints(threshold);

        return pointsEntityList.stream()
                .map(this::getBuiltPoint)
                .collect(Collectors.toList());
    }

    @Override
    public PointDto getPoint(Long pointId) {
        List<Poi> points = poiRepository.retrievePoint(pointId);

        if (points.isEmpty()) throw new PointNotFoundException("Point not found with id: " + pointId);

        return getBuiltPoint(points.get(0));
    }

    @Override
    public List<PointDto> getAll() {
        return poiRepository.retrieveAll().stream()
                .map(this::getBuiltPoint)
                .collect(Collectors.toList());
    }

    private PointDto getBuiltPoint(Poi point) {
        return PointDto.builder()
                .id(point.getId())
                .name(point.getName())
                .latitude(point.getLatitude())
                .longitude(point.getLongitude())
                .counter(point.getCounter())
                .build();
    }
}
