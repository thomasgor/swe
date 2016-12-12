package com.fhaachen.swe.freespace.ressources;

import com.fhaachen.swe.freespace.Antwort;
import com.fhaachen.swe.freespace.main.Sitzung;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

/**
 * Created by thomas on 27.11.2016.
 */
@Path( value = "/sitzung")
@RolesAllowed({"user", "professor"})
public class SitzungREST {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSitzung(@Context SecurityContext context){
        String benutzerID = context.getUserPrincipal().getName();
        return Sitzung.getSitzungById(benutzerID);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postSitzung(String json, @Context SecurityContext context){
        String benutzerID = context.getUserPrincipal().getName();
        return Sitzung.postSitzung(json, benutzerID);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value="/{param}")
    public Response deleteSitzungID(@PathParam(value="param") String id){
        return Sitzung.deleteSitzung(id);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value="/{param}")
    public Response putSitzungID(@PathParam(value="param") String id, String json){
        return Sitzung.putSitzung(id, json);
    }
}
