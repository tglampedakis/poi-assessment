package com.example.poiassessment.dto;

import lombok.Data;

@Data
public class ErrorResponseDto {
    
    private String errorCode;
    private String errorMessage;

    public ErrorResponseDto(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
