package com.example.poiassessment.resource;

import com.example.poiassessment.dto.PointRequestDto;
import com.example.poiassessment.service.PointService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/points")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PointResource {

    @Inject
    PointService pointService;

    @POST
    @Path("/")
    public Response createPoint(PointRequestDto pointRequestDto) {
        pointService.createPoint(pointRequestDto);

        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    @Path("/closest")
    public Response getClosestPoint(
            @QueryParam("lat") double lat,
            @QueryParam("lon") double lon) {

        return Response.ok(pointService.getClosestPoint(lat, lon)).build();
    }

    @GET
    @Path("/higher-counter")
    public Response getHigherCounterPoints(
            @QueryParam("threshold") Integer threshold) {

        return Response.ok(pointService.getHigherCounterPoints(threshold)).build();
    }

    @GET
    @Path("/{pointId}")
    public Response getPoint(
            @PathParam("pointId") Long pointId) {

        return Response.ok(pointService.getPoint(pointId)).build();
    }

    @GET
    @Path("/")
    public Response getAllPoints() {

        return Response.ok(pointService.getAll()).build();
    }

}