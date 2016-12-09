package com.fhaachen.swe.freespace.ressources;

import com.fhaachen.swe.freespace.Antwort;
import com.fhaachen.swe.freespace.main.Weg;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Simon on 09.12.2016.
 */

@Path( value = "/weg")
@RolesAllowed({"user", "professor"})
public class WegREST {

        @GET
        @Path(value="/{param1}/{param2}")
        @Produces(MediaType.APPLICATION_JSON)
        public Response getWeg(@PathParam(value="param1") String startID ,@PathParam(value="param2") String zielID){
            String response = Weg.getWeg(startID, zielID);
            if(response != null){
                return Response.ok(response, MediaType.APPLICATION_JSON).build();
            }
            return Antwort.BAD_REQUEST;
        }
}
