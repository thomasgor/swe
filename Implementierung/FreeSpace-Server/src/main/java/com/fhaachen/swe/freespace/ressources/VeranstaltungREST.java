package com.fhaachen.swe.freespace.ressources;

import com.fhaachen.swe.freespace.main.Benutzer;
import com.fhaachen.swe.freespace.main.JsonHelper;
import com.fhaachen.swe.freespace.main.Veranstaltung;
import com.fhaachen.swe.freespace.Antwort;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by thomas on 27.11.2016.
 * Edited by simon on 04.12.2016.
 */
@Path( value = "/veranstaltung")
@DeclareRoles({"user", "professor"})
public class VeranstaltungREST {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response getVeranstaltungsListe(){
        String answer = Veranstaltung.getVeranstaltungsListe();
        if(answer != "" || answer != null) {
            return Response.ok(answer, MediaType.APPLICATION_JSON).build();
        } else {
            return Antwort.INTERNAL_SERVER_ERROR;
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("professor")
    public Response postVeranstaltung(String json){
        String benutzer = JsonHelper.getAttribute(json,"benutzer");
        int von = Integer.valueOf(JsonHelper.getAttribute(json,"von"));
        int bis = Integer.valueOf(JsonHelper.getAttribute(json,"bis"));
        int datum = Integer.valueOf(JsonHelper.getAttribute(json,"datum"));
        int raumID = Integer.valueOf(JsonHelper.getAttribute(json,"raum"));
        String answer = Veranstaltung.postVeranstaltung(benutzer, von, bis, datum, raumID);
        if(answer != "" || answer != null) {
            return Response.ok(answer, MediaType.APPLICATION_JSON).build();
        } else {
            return Antwort.INTERNAL_SERVER_ERROR;
        }
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value="/{param}")
    @PermitAll
    public Response getVeranstaltungByID(@PathParam(value="param") String id){
        String answer = Veranstaltung.getVeranstaltungByID(Integer.valueOf(id));
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
    @RolesAllowed("professor")
    public Response putVeranstaltung(@PathParam(value="param") String id, String json){
        int veranstaltungID = Integer.valueOf(id);
        int von = Integer.valueOf(JsonHelper.getAttribute(json,"von"));
        int bis = Integer.valueOf(JsonHelper.getAttribute(json,"bis"));
        int datum = Integer.valueOf(JsonHelper.getAttribute(json,"datum"));
        int raumID = Integer.valueOf(JsonHelper.getAttribute(json,"raum"));
        String answer = Veranstaltung.putVeranstaltung(veranstaltungID, von, bis, datum, raumID);
        if(answer != "" || answer != null) {
            return Response.ok(answer, MediaType.APPLICATION_JSON).build();
        } else {
            return Antwort.INTERNAL_SERVER_ERROR;
        }
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value="/{param}")
    @RolesAllowed("professor")
    public Response deleteVeranstaltung(@PathParam(value="param") String id){
        String answer = Veranstaltung.deleteVeranstaltung(Integer.valueOf(id));
        if(answer != "" || answer != null) {
            return Response.ok(answer, MediaType.APPLICATION_JSON).build();
        } else {
            return Antwort.INTERNAL_SERVER_ERROR;
        }
    }
}