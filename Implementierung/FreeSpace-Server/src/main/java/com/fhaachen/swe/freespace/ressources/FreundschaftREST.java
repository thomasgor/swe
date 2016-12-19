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
        return Freundschaft.getFreundschaften(benutzerID);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postFreundschaft(String json, @Context SecurityContext context){
        String benutzerID = context.getUserPrincipal().getName();
        return Freundschaft.postFreundschaft(benutzerID, json);
    }


    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value="/{param}")
    public Response putFreundschaftID(@PathParam(value="param") String id, @Context SecurityContext context){
        String benutzerID = context.getUserPrincipal().getName();
        return Freundschaft.putFreundschaft(benutzerID, id);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value="/{param}")
    public Response deleteFreundschaftID(@PathParam(value="param") String id, @Context SecurityContext context){
        String benutzerID = context.getUserPrincipal().getName();
        return Freundschaft.deleteFreundschaft(benutzerID, id);
    }
}
