package com.fhaachen.swe.freespace.rest;

import com.fhaachen.swe.freespace.main.Benutzer;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by thomas on 27.11.2016.
 */
@Path( value = "/veranstaltung")
public class VeranstaltungREST {
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVeranstaltungsliste(String json){
        Benutzer.test();
        return Response.status(Response.Status.NOT_IMPLEMENTED).entity("hier ensteht die Veranstaltungsliste").build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postVeranstaltung(String json){
        return Response.status(Response.Status.NOT_IMPLEMENTED).entity("hier können veranstaltungen angelegt werden!").build();
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value="/{param}")
    public Response getVeranstaltungID(@PathParam(value="param") String id, String json){
        return Response.status(Response.Status.NOT_IMPLEMENTED).entity("Veranstaltung mit der ID" + id).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value="/{param}")
    public Response putVeranstltungID(@PathParam(value="param") String id, String json){
        return Response.status(Response.Status.NOT_IMPLEMENTED).entity("hier können Veranstaltungen geändert werden!").build();
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value="/{param}")
    public Response deleteVeranstaltungID(@PathParam(value="param") String id, String json){
        return Response.status(Response.Status.NOT_IMPLEMENTED).entity("hier können Veranstaltungen gelöscht werden!" + json).build();
    }
}
