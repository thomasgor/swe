package com.fhaachen.swe.freespace.rest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by thomas on 27.11.2016.
 */

@Path( value = "/raum")
public class RaumREST {

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRaumliste(String json){
        return Response.status(Response.Status.NOT_IMPLEMENTED).entity("Hier ensteht die Raumliste").build();
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value="/{param}")
    public Response getRaumdetails(@PathParam(value="param") String id, String json){
        return Response.status(Response.Status.NOT_IMPLEMENTED).entity("Raumdetails von " + id).build();
    }


    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value="/{param}")
    public Response putRaumdetails(@PathParam(value="param") String id, String json){
        return Response.status(Response.Status.NOT_IMPLEMENTED).entity("PUT Raumdetails von " + id).build();
    }
}
