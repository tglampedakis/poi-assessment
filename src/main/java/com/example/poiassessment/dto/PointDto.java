package com.example.poiassessment.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PointDto {

    private Long id;
    private String name;
    private Double latitude;
    private Double longitude;
    private Integer counter;

}
