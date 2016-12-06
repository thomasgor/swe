package com.fhaachen.swe.freespace.ressources;

import com.fhaachen.swe.freespace.main.Benutzer;
import com.fhaachen.swe.freespace.main.Veranstaltung;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

/**
 * Created by thomas on 27.11.2016.
 */

@Path( value = "/veranstaltung")
@RolesAllowed("professor")
public class VeranstaltungREST {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVeranstaltungsliste(@Context SecurityContext context){
        String professorID = context.getUserPrincipal().getName();
        return Response.ok(Veranstaltung.getVeranstaltung(professorID)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postVeranstaltung(String json){
        return Response.status(Response.Status.NOT_IMPLEMENTED).entity("hier können veranstaltungen angelegt werden!" + json).build();
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value="/{param}")
    public Response getVeranstaltungID(@PathParam(value="param") String id){
        System.out.println("Im in");
        return Response.status(Response.Status.NOT_IMPLEMENTED).entity("Veranstaltung mit der ID" + id).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value="/{param}")
    public Response putVeranstltungID(@PathParam(value="param") String id, String json){
        return Response.status(Response.Status.NOT_IMPLEMENTED).entity("hier können Veranstaltungen geändert werden!").build();
    }

    @RolesAllowed("professor")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value="/{param}")
    public Response deleteVeranstaltungID(@PathParam(value="param") String id, String json){
        return Response.status(Response.Status.NOT_IMPLEMENTED).entity("hier können Veranstaltungen gelöscht werden!" + json).build();
    }
}
