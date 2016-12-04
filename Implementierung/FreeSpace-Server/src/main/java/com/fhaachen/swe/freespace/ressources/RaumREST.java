package com.fhaachen.swe.freespace.ressources;

import com.fhaachen.swe.freespace.main.Raum;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
        return Response.ok(Raum.getRaumdetails(id), MediaType.APPLICATION_JSON).build();
        //return Response.status(Response.Status.NOT_IMPLEMENTED).entity("Raumdetails von " + id).build();
    }


    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value="/{param}")
    public Response putRaumdetails(@PathParam(value="param") String id, String json){
        System.out.println(json);
        return Response.ok(Raum.putRaumID(1,2), MediaType.APPLICATION_JSON).build();
    }
}
