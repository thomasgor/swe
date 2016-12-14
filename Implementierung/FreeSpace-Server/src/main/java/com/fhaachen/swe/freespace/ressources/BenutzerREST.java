package com.fhaachen.swe.freespace.ressources;

import com.fhaachen.swe.freespace.Antwort;
import com.fhaachen.swe.freespace.main.Benutzer;

import javax.annotation.security.RolesAllowed;
import javax.print.attribute.standard.Media;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

/**
 * Created by thomas on 27.11.2016.
 */
@Path("benutzer")
public class BenutzerREST {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postBenutzer(String json){
        String result = null;
        try {
            result = Benutzer.postBenutzer(json);
        }catch(Exception e){
            System.out.println(e);
        }

        System.out.println(result);
        if(result == null){
            return Antwort.BAD_REQUEST;
        }
        return Response.ok(result, MediaType.APPLICATION_JSON).build();
    }

    @PUT
    @RolesAllowed({"user", "professor"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response putBenutzer(String json, @Context SecurityContext context){
        String benutzerID = context.getUserPrincipal().getName();
        String result = Benutzer.putBenutzer(json, benutzerID);

        if(result == null){
            return Antwort.FORBIDDEN;
        }

        return Response.ok(result, MediaType.APPLICATION_JSON).build();
    }

}
