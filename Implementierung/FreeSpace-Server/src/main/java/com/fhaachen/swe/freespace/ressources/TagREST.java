package com.fhaachen.swe.freespace.ressources;

import com.fhaachen.swe.freespace.Antwort;
import com.fhaachen.swe.freespace.main.Tag;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by thomas on 27.11.2016.
 */
@Path( value = "/tag")
@RolesAllowed({"user", "professor"})
public class TagREST {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTagListe(){
        String response = Tag.getTag();
        if(response != null){
            return Response.ok(response, MediaType.APPLICATION_JSON).build();
        }
        return Antwort.INTERNAL_SERVER_ERROR;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postTag(String json){
        String response = Tag.postTag(json);
        if (response != null) {
            return Antwort.CREATED;
            //Falls Antwortcode CREATED nicht ausreicht:
            //return Response.ok(response, MediaType.APPLICATION_JSON).build();
        }
        return Antwort.INTERNAL_SERVER_ERROR;
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value="/{param}")
    public Response deleteTagID(@PathParam(value="param") String id){
        String response = Tag.deleteTag(id);
        if (response != null) {
            return Response.ok(response, MediaType.APPLICATION_JSON).build();
        }
        return Antwort.INTERNAL_SERVER_ERROR;
    }
}
