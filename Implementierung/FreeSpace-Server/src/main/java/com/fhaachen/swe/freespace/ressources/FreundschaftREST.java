package com.fhaachen.swe.freespace.ressources;

import com.fhaachen.swe.freespace.Antwort;
import com.fhaachen.swe.freespace.main.Freundschaft;
import com.fhaachen.swe.freespace.main.JsonHelper;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by thomas on 27.11.2016.
 * Edited by simon on 04.12.2016.
 */
@Path("/freundschaft")
public class FreundschaftREST {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFreundschaftsliste(){
        String answer = Freundschaft.getFreundschaftsListe();
        if(answer != "" || answer != null) {
            return Response.ok(answer, MediaType.APPLICATION_JSON).build();
        } else {
            return Antwort.INTERNAL_SERVER_ERROR;
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postFreundschaft(String json){
        String benutzer = JsonHelper.getAttribute(json, "benutzer");
        String freund = JsonHelper.getAttribute(json, "freund");
        int status = Integer.valueOf(JsonHelper.getAttribute(json, "status"));
        String answer = Freundschaft.postFreundschaft(benutzer, freund, status);
        if(answer != "" || answer != null) {
            return Response.ok(answer, MediaType.APPLICATION_JSON).build();
        } else {
            return Antwort.INTERNAL_SERVER_ERROR;
        }
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putFreundschaftID(String json){
        String benutzer = JsonHelper.getAttribute(json, "benutzer");
        String freund = JsonHelper.getAttribute(json, "freund");
        int status = Integer.valueOf(JsonHelper.getAttribute(json, "status"));
        String answer = Freundschaft.putFreundschaft(benutzer, freund, status);
        if(answer != "" || answer != null) {
            return Response.ok(answer, MediaType.APPLICATION_JSON).build();
        } else {
            return Antwort.INTERNAL_SERVER_ERROR;
        }
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteFreundschaftID(String json){
        String benutzer = JsonHelper.getAttribute(json,"benutzer");
        String freund = JsonHelper.getAttribute(json,"freund");
        String answer = Freundschaft.deleteFreundschaft(benutzer, freund) + Freundschaft.deleteFreundschaft(freund, benutzer);
        if(answer != "" || answer != null) {
            return Response.ok(answer, MediaType.APPLICATION_JSON).build();
        } else {
            return Antwort.INTERNAL_SERVER_ERROR;
        }
    }
}
