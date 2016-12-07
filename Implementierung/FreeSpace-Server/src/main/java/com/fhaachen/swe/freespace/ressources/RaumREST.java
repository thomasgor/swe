package com.fhaachen.swe.freespace.ressources;

import com.fhaachen.swe.freespace.Antwort;
import com.fhaachen.swe.freespace.main.JsonHelper;
import com.fhaachen.swe.freespace.main.Raum;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by thomas on 27.11.2016.
 * Edited by simon on 04.12.2016.
 */

@Path( value = "/raum")
public class RaumREST {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRaumListe(){
        String answer = Raum.getRaumListe();
        if(answer != "" || answer != null) {
            return Response.ok(answer, MediaType.APPLICATION_JSON).build();
        } else {
            return Antwort.INTERNAL_SERVER_ERROR;
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value="/{param}")
    public Response getRaumByID(@PathParam(value="param") String id){
        String answer = Raum.getRaumByID(Integer.valueOf(id));
        if(answer != "" || answer != null) {
            return Response.ok(answer, MediaType.APPLICATION_JSON).build();
        } else {
            return Antwort.INTERNAL_SERVER_ERROR;
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value="/{param}")
    public Response putRaum(@PathParam(value="param") String id, String json) {
        int raumID = Integer.valueOf(id);
        int tagID = Integer.valueOf(JsonHelper.getAttribute(json,"tag"));
        String answer = Raum.putRaum(raumID, tagID);
        if(answer != "" || answer != null) {
            return Response.ok(answer, MediaType.APPLICATION_JSON).build();
        } else {
            return Antwort.INTERNAL_SERVER_ERROR;
        }
    }
}