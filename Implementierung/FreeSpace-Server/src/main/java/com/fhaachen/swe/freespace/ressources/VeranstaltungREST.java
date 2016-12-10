package com.fhaachen.swe.freespace.ressources;

import com.fhaachen.swe.freespace.Antwort;
import com.fhaachen.swe.freespace.JsonHelper;
import com.fhaachen.swe.freespace.main.Veranstaltung;

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
        String response = Veranstaltung.getVeranstaltungsListe(professorID);
        return Response.ok(response, MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postVeranstaltung(String json, @Context SecurityContext context){
        String professorID = context.getUserPrincipal().getName();
        String response = Veranstaltung.postVeranstaltung(json, professorID);

        if(response == null){
            return Antwort.ROOM_BLOCKED;
        }

        return Response.ok(response, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value="/{param}")
    public Response getVeranstaltungID(@PathParam(value="param") String id ,@Context SecurityContext context){
        String professorID = context.getUserPrincipal().getName();
        String response = Veranstaltung.getVeranstaltungByID(id);
        String responseProefessorID = JsonHelper.getAttribute(response, "benutzer");

        if(response == null){
            return Antwort.NOT_FOUND;
        }

        if(!professorID.equals(responseProefessorID)){
            System.out.println("Benutzer " + professorID + " hat versucht eine Veranstaltung von "+ responseProefessorID + " zu laden");
            return Antwort.FORBIDDEN;
        }
        return Response.ok(response, MediaType.APPLICATION_JSON).build();
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
