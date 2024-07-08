package com.example.poiassessment.service;

import com.example.poiassessment.dto.ClosestPointDto;
import com.example.poiassessment.dto.PointDto;
import com.example.poiassessment.dto.PointRequestDto;

import java.util.List;

public interface PointService {

    void createPoint(PointRequestDto pointRequestDto);

    ClosestPointDto getClosestPoint(double lat, double lon);

    List<PointDto> getHigherCounterPoints(Integer threshold);

    PointDto getPoint(Long pointId);

    List<PointDto> getAll();

}
