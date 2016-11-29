package com.fhaachen.swe.freespace.ressources;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by thomas on 27.11.2016.
 */
@Path( value = "/sitzung")
@RolesAllowed({"user", "professor"})
public class SitzungREST {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSitzung(String json){

        return Response.status(Response.Status.NOT_IMPLEMENTED).entity("get Sitzung").build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postSitzung(String json){
        return Response.status(Response.Status.NOT_IMPLEMENTED).entity("neue Sitzung").build();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value="/{param}")
    public Response deleteSitzungID(@PathParam(value="param") String id, String json){
        return Response.status(Response.Status.NOT_IMPLEMENTED).entity("Delete Sitzung" + id).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value="/{param}")
    public Response putSitzungID(@PathParam(value="param") String id, String json){
        return Response.status(Response.Status.NOT_IMPLEMENTED).entity("Sitzung ändern!" + id).build();
    }
}
