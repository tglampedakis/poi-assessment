package com.example.poiassessment.service;

import com.example.poiassessment.dto.PointDto;
import com.example.poiassessment.dto.PointRequestDto;
import com.example.poiassessment.dto.entity.Poi;
import com.example.poiassessment.exception.BadRequestException;
import com.example.poiassessment.exception.PointNotFoundException;
import com.example.poiassessment.repository.PoiRepository;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PointServiceImplTest {

    @Mock
    private PoiRepository poiRepository;

    @Mock
    private Cache<Long, Poi> pointsCache;

    @InjectMocks
    private PointServiceImpl pointService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize CacheManager and pointsCache manually if they are not mocked
        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache("pointsCache", CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, Poi.class, ResourcePoolsBuilder.heap(1000)))
                .build();
        cacheManager.init();
        pointsCache = cacheManager.getCache("pointsCache", Long.class, Poi.class);

        pointService = new PointServiceImpl();
        pointService.poiRepository = poiRepository;
    }

    @Test
    void testCreatePoint_Success() {
        PointRequestDto pointRequestDto = new PointRequestDto();
        pointRequestDto.setName("Test Point");
        pointRequestDto.setLatitude(1.0);
        pointRequestDto.setLongitude(2.0);

        pointService.createPoint(pointRequestDto);

        verify(poiRepository, times(1)).savePoint(pointRequestDto);
    }

    @Test
    void testCreatePoint_BadRequest() {
        PointRequestDto pointRequestDto = new PointRequestDto();
        pointRequestDto.setName(null);
        pointRequestDto.setLatitude(1.0);
        pointRequestDto.setLongitude(2.0);

        assertThrows(BadRequestException.class, () -> pointService.createPoint(pointRequestDto));
    }

//    @Test
//    void testGetClosestPoint_Success() {
//        Poi poi = new Poi();
//
//        poi.setId(1L);
//        poi.setName("Test Point");
//        poi.setLatitude(1.0);
//        poi.setLongitude(1.0);
//        poi.setCounter(0);
//
//        Long cacheKey = CacheUtils.generateCoordsCacheKey(1.0, 1.0);
//
//        when(pointsCache.get(cacheKey)).thenReturn(poi);
//
//        ClosestPointDto closestPoint = pointService.getClosestPoint(1.0, 1.0);
//
//        assertNotNull(closestPoint);
//        assertEquals("Test Point", closestPoint.getName());
//    }

    @Test
    void testGetPoint_Success() {
        Poi poi = new Poi();

        poi.setId(1L);
        poi.setName("Test Point");
        poi.setLatitude(1.0);
        poi.setLongitude(1.0);
        poi.setCounter(0);

        when(poiRepository.retrievePoint(1L)).thenReturn(Collections.singletonList(poi));

        PointDto point = pointService.getPoint(1L);

        assertNotNull(point);
        assertEquals("Test Point", point.getName());
    }

    @Test
    void testGetPoint_Failure() {
        when(poiRepository.retrievePoint(1L)).thenReturn(Collections.emptyList());

        assertThrows(PointNotFoundException.class, () -> pointService.getPoint(1L));
    }

    @Test
    void testGetAll() {
        Poi poi = new Poi();

        poi.setId(1L);
        poi.setName("Test Point");
        poi.setLatitude(1.0);
        poi.setLongitude(1.0);
        poi.setCounter(0);

        Poi poi2 = new Poi();

        poi2.setId(1L);
        poi2.setName("Test Point 2");
        poi2.setLatitude(1.0);
        poi2.setLongitude(1.0);
        poi2.setCounter(0);

        List<Poi> poiList = Arrays.asList(poi, poi2);

        when(poiRepository.retrieveAll()).thenReturn(poiList);

        List<PointDto> points = pointService.getAll();

        assertEquals(2, points.size());

        assertEquals("Test Point", points.get(0).getName());
        assertEquals("Test Point 2", points.get(1).getName());
    }
}
