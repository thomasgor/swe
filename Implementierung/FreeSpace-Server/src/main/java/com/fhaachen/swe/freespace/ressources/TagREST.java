package com.fhaachen.swe.freespace.ressources;

import com.fhaachen.swe.freespace.Antwort;
import com.fhaachen.swe.freespace.main.JsonHelper;
import com.fhaachen.swe.freespace.main.Tag;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by thomas on 27.11.2016.
 * Edited by simon on 04.12.2016.
 */
@Path( value = "/tag")
public class TagREST {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTagListe(){
        String answer = Tag.getTagListe();
        if(answer != "" || answer != null) {
            return Response.ok(answer, MediaType.APPLICATION_JSON).build();
        } else {
            return Antwort.INTERNAL_SERVER_ERROR;
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postTag(String json){
        String tagName = JsonHelper.getAttribute(json,"name");
        String answer = Tag.postTag(tagName);
        if(answer != "" || answer != null) {
            return Response.ok(answer, MediaType.APPLICATION_JSON).build();
        } else {
            return Antwort.INTERNAL_SERVER_ERROR;
        }
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value="/{param}")
    public Response deleteTag(@PathParam(value="param") String id){
        String answer = Tag.deleteTag(Integer.valueOf(id));
        if(answer != "" || answer != null) {
            return Response.ok(answer, MediaType.APPLICATION_JSON).build();
        } else {
            return Antwort.INTERNAL_SERVER_ERROR;
        }
    }
}