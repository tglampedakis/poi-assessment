package com.example.poiassessment.resource;

import com.example.poiassessment.dto.PointDto;
import com.example.poiassessment.dto.PointRequestDto;
import com.example.poiassessment.service.PointService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PointResourceTest {

    @Mock
    private PointService pointService;

    @InjectMocks
    private PointResource pointResource;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePoint() {
        PointRequestDto pointRequestDto = new PointRequestDto();
        pointRequestDto.setName("Test Point");
        pointRequestDto.setLatitude(1.0);
        pointRequestDto.setLongitude(2.0);

        Response response = pointResource.createPoint(pointRequestDto);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
    }

    @Test
    void testGetClosestPoint() {
        double lat = 45.0;
        double lon = 90.0;

        Response response = pointResource.getClosestPoint(lat, lon);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    void testGetHigherCounterPoints() {
        int threshold = 5;

        PointDto pointDto = PointDto.builder()
                .id(1L)
                .name("Test Point 1")
                .latitude(10.0)
                .longitude(10.0)
                .counter(3)
                .build();


        PointDto pointDto2 = PointDto.builder()
                .id(2L)
                .name("Test Point 2")
                .latitude(12.0)
                .longitude(12.0)
                .counter(12)
                .build();

        List<PointDto> expectedPoints = Arrays.asList(pointDto, pointDto2);

        when(pointService.getHigherCounterPoints(threshold)).thenReturn(expectedPoints);

        Response response = pointResource.getHigherCounterPoints(threshold);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    void testGetPoint() {
        Long pointId = 1L;

        PointDto pointDto = PointDto.builder()
                .id(1L)
                .name("Test Point 1")
                .latitude(10.0)
                .longitude(10.0)
                .counter(3)
                .build();

        when(pointService.getPoint(pointId)).thenReturn(pointDto);

        Response response = pointResource.getPoint(pointId);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(pointDto, response.getEntity());
    }

    @Test
    void testGetAllPoints() {
        PointDto pointDto = PointDto.builder()
                .id(1L)
                .name("Test Point 1")
                .latitude(10.0)
                .longitude(10.0)
                .counter(3)
                .build();


        PointDto pointDto2 = PointDto.builder()
                .id(2L)
                .name("Test Point 2")
                .latitude(12.0)
                .longitude(12.0)
                .counter(12)
                .build();

        List<PointDto> mockPointDtoList = Arrays.asList(pointDto, pointDto2);

        when(pointService.getAll()).thenReturn(mockPointDtoList);

        Response response = pointResource.getAllPoints();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(mockPointDtoList, response.getEntity());
    }
}
