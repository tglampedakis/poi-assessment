package com.example.poiassessment.exception;

import com.example.poiassessment.dto.ErrorResponseDto;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable ex) {
        if (ex instanceof PointNotFoundException) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponseDto("NOT_FOUND", ex.getMessage()))
                    .build();
        } else if (ex instanceof BadRequestException) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponseDto("BAD_REQUEST", ex.getMessage()))
                    .build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponseDto("INTERNAL_SERVER_ERROR", "An unexpected error occurred."))
                    .build();
        }
    }
}
