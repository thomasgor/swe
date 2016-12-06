package com.fhaachen.swe.freespace.ressources;

import com.fhaachen.swe.freespace.Antwort;
import com.fhaachen.swe.freespace.JsonHelper;
import com.fhaachen.swe.freespace.main.Raum;
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

@Path( value = "/raum")
@RolesAllowed({"user", "professor"})
public class RaumREST {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRaum(){
        String json = Raum.getRaum();
        return Response.ok(json,MediaType.APPLICATION_JSON).build();
        //return Response.status(Response.Status.NOT_IMPLEMENTED).entity("Hier ensteht die Raumliste").build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value="/{param}")
    public Response getRaumdetails(@PathParam(value="param") int id){

        String json = Raum.getRaumdetails(id);
        if(json == null) return Antwort.NOT_FOUND;

        return Response.ok(Raum.getRaumdetails(id), MediaType.APPLICATION_JSON).build();
        //return Response.status(Response.Status.NOT_IMPLEMENTED).entity("Raumdetails von " + id).build();
    }


    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value="/{param}")
    public Response putRaumdetails(@PathParam(value="param") String id, String json, @Context SecurityContext context) {

      /*  System.out.println(json);
        Map tmp = JsonHelper.toMap(json);

        String tag = String.valueOf(tmp.get("tag"));
        Map m = (Map)tmp.get("tag2");

        String name = String.valueOf(m.get("name"));
        System.out.println(name); */
/*
        Map tmp = JsonHelper.toMap(json);
        System.out.println(tmp);
        System.out.println(((Map)tmp.get("tag2")).get("name")); */
        String tag = JsonHelper.getAttribute(json,"tag");
    //    System.out.println(JsonHelper.getAttribute(json,"tag2","name"));

        String username = context.getUserPrincipal().getName(); // BenutzerID

        //TODO: Implemtierung der
        boolean isAllowed = Sitzung.hasActiveSession(username);

        if(!isAllowed) {
            //Überliefert dennoch das Raumobjekt
            String s = Raum.getRaumdetails(Integer.parseInt(id));
            return Response.status(Response.Status.FORBIDDEN).entity(s).build();
        }

        String answer = Raum.putRaumID(1,Integer.parseInt(tag));
        
        if(answer == null){
            return Antwort.NOT_FOUND;
        }
        return Response.ok(answer, MediaType.APPLICATION_JSON).build();
    }
}
