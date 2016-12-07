package com.fhaachen.swe.freespace.ressources;

import com.fhaachen.swe.freespace.Antwort;
import com.fhaachen.swe.freespace.main.Benutzer;
import com.fhaachen.swe.freespace.main.JsonHelper;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by thomas on 27.11.2016.
 * Edited by simon on 04.12.2016.
 */
@Path("Benutzer")
public class BenutzerREST {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postBenutzer(String json){
        String id = JsonHelper.getAttribute(json, "id");
        String email = JsonHelper.getAttribute(json,"email");
        String token = JsonHelper.getAttribute(json,"token");
        String name = JsonHelper.getAttribute(json,"name");
        String vorname = JsonHelper.getAttribute(json,"vorname");
        String foto = JsonHelper.getAttribute(json,"foto");
        int istProfessor = Integer.valueOf(JsonHelper.getAttribute(json,"istProfessor"));
        String answer = Benutzer.postBenutzer(id, email, token, name, vorname, foto, istProfessor);
        if(answer != "" && answer != null) {
            return Response.ok(answer, MediaType.APPLICATION_JSON).build();
        } else {
            return Antwort.INTERNAL_SERVER_ERROR;
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value="/{param}")
    public Response putBenutzer(@PathParam(value="param") String id, String json){
        String email = JsonHelper.getAttribute(json,"email");
        String token = JsonHelper.getAttribute(json,"token");
        String name = JsonHelper.getAttribute(json,"name");
        String vorname = JsonHelper.getAttribute(json,"vorname");
        String foto = JsonHelper.getAttribute(json,"foto");
        int istProfessor = Integer.valueOf(JsonHelper.getAttribute(json,"istProfessor"));
        String answer = Benutzer.putBenutzer(id, email, token, name, vorname, foto, istProfessor);
        if(answer != "" && answer != null) {
            return Response.ok(answer, MediaType.APPLICATION_JSON).build();
        } else {
            return Antwort.INTERNAL_SERVER_ERROR;
        }
    }

}
