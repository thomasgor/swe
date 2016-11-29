package com.fhaachen.swe.freespace.rest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by thomas on 27.11.2016.
 */
@Path( value = "/tag")
public class TagREST {

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTagListe(String json){

        return Response.status(Response.Status.NOT_IMPLEMENTED).entity("hier ensteht die Tagliste").build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postTag(String json){
        return Response.status(Response.Status.NOT_IMPLEMENTED).entity("Hier können Tags erzeugt werden").build();
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value="/{param}")
    public Response deleteTagID(@PathParam(value="param") String id, String json){
        return Response.status(Response.Status.NOT_IMPLEMENTED).entity("Hier können tags gelöscht werden" + id).build();
    }
}
