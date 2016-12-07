package com.fhaachen.swe.freespace.ressources;

import com.fhaachen.swe.freespace.main.Freundschaft;
import org.javalite.activejdbc.LazyList;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

/**
 * Created by thomas on 27.11.2016.
 */
@RolesAllowed({"user", "professor"})
@Path("/freundschaft")
public class FreundschaftREST {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFreundschaften(@Context SecurityContext context){
        String benutzerID = context.getUserPrincipal().getName();
        String response = Freundschaft.getFreundschaften(benutzerID);
        return Response.ok(response,MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postFreundschaft(){
        return Response.status(Response.Status.NOT_IMPLEMENTED).entity("Hier können freundschaften erstellt werden!").build();
    }
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path(value="/{param}")
    public Response putFreundschaftID(@PathParam(value="param") String id){
        return Response.status(Response.Status.NOT_IMPLEMENTED).entity("Hier können freundschaften geändert werden!").build();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value="/{param}")
    public Response deleteFreundschaftID(@PathParam(value="param") String id){
        return Response.status(Response.Status.NOT_IMPLEMENTED).entity("Hier können freundschaften gelöscht werden!").build();
    }
}
