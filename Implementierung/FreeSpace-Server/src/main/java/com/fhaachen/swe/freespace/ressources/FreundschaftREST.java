package com.fhaachen.swe.freespace.ressources;

import com.fhaachen.swe.freespace.Antwort;
import com.fhaachen.swe.freespace.JsonHelper;
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
        if (response != null) {
            return Response.ok(response, MediaType.APPLICATION_JSON).build();
        }
        return Antwort.INTERNAL_SERVER_ERROR;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value="/{param}")
    public Response postFreundschaft(@PathParam(value="param") String id, @Context SecurityContext context){
        String benutzerID = context.getUserPrincipal().getName();
        String response = Freundschaft.postFreundschaft(benutzerID, id);
        if (response != null) {
            return Antwort.CREATED;
            //Falls Antwortcode CREATED nicht ausreicht:
            //return Response.ok(response, MediaType.APPLICATION_JSON).build();
        }
        return Antwort.INTERNAL_SERVER_ERROR;
    }


    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value="/{param}")
    public Response putFreundschaftID(@PathParam(value="param") String id, @Context SecurityContext context){
        String benutzerID = context.getUserPrincipal().getName();
        String response = Freundschaft.putFreundschaft(benutzerID, id);
        if (response != null) {
            return Response.ok(response, MediaType.APPLICATION_JSON).build();
        }
        return Antwort.INTERNAL_SERVER_ERROR;
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value="/{param}")
    public Response deleteFreundschaftID(@PathParam(value="param") String id, @Context SecurityContext context){
        String benutzerID = context.getUserPrincipal().getName();
        String response = Freundschaft.deleteFreundschaft(benutzerID, id);
        if (response != null) {
            return Response.ok(response, MediaType.APPLICATION_JSON).build();
        }
        return Antwort.INTERNAL_SERVER_ERROR;
    }
}
