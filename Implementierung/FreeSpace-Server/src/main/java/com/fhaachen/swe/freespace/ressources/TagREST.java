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
        return Tag.getTagListe();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postTag(String json){
        return Tag.postTag(json);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value="/{param}")
    public Response deleteTagID(@PathParam(value="param") String id){
        return Tag.deleteTag(id);
    }
}
