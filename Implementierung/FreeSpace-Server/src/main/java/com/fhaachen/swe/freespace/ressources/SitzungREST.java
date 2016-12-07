package com.fhaachen.swe.freespace.ressources;

import com.fhaachen.swe.freespace.Antwort;
import com.fhaachen.swe.freespace.main.JsonHelper;
import com.fhaachen.swe.freespace.main.Sitzung;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by thomas on 27.11.2016.
 * Edited by simon on 04.12.2016.
 */
@Path( value = "/sitzung")
public class SitzungREST {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSitzungsListe(){
        String answer = Sitzung.getSitzungsListe();
        if(answer != "" || answer != null) {
            return Response.ok(answer, MediaType.APPLICATION_JSON).build();
        } else {
            return Antwort.INTERNAL_SERVER_ERROR;
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postSitzung(String json){
        int benutzerID = Integer.valueOf(JsonHelper.getAttribute(json,"benutzer"));
        int raumID = Integer.valueOf(JsonHelper.getAttribute(json,"raum"));
        int endzeit = Integer.valueOf(JsonHelper.getAttribute(json,"endzeit"));
        String answer = Sitzung.postSitzung(benutzerID, raumID, endzeit);
        if(answer != "" || answer != null) {
            return Response.ok(answer, MediaType.APPLICATION_JSON).build();
        } else {
            return Antwort.INTERNAL_SERVER_ERROR;
        }
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value="/{param}")
    public Response deleteSitzung(@PathParam(value="param") String id){
        String answer = Sitzung.deleteSitzung(Integer.valueOf(id));
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
    public Response putSitzung(@PathParam(value="param") String id, String json){
        int sitzungsID = Integer.valueOf(id);
        int raum = Integer.valueOf(JsonHelper.getAttribute(json,"raum"));
        int endzeit = Integer.valueOf(JsonHelper.getAttribute(json,"endzeit"));
        String answer = Sitzung.putSitzung(sitzungsID, endzeit);
        if(answer != "" || answer != null) {
            return Response.ok(answer, MediaType.APPLICATION_JSON).build();
        } else {
            return Antwort.INTERNAL_SERVER_ERROR;
        }
    }
}