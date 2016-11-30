package com.fhaachen.swe.freespace.ressources;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by thomas on 27.11.2016.
 */
@Path("Benutzer")
public class BenutzerREST {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postBenutzer(){
        //Entweder einen neuen Benutzeranlagen, oder dessen bisherigen Daten zurück geben ...
        return Response.status(Response.Status.NOT_IMPLEMENTED).entity("Hier können Benutzer erstellt werden!").build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value="/{param}")
    public Response putBenutzer(@PathParam(value="param") String id, String json){
        return Response.status(Response.Status.NOT_IMPLEMENTED).entity("hier können Benutzer geändert werden!").build();
    }

}
