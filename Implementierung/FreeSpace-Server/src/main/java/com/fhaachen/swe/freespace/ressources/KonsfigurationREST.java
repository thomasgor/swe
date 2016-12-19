package com.fhaachen.swe.freespace.ressources;

import com.sun.research.ws.wadl.Response;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

/**
 * Created by thomasgorgels on 19.12.16.
 */

@RolesAllowed({"admin"})
@Path("/konfiguration")
public class KonsfigurationREST {


    @GET
    @Produces(MediaType.TEXT_HTML)
    public javax.ws.rs.core.Response getKonfiguration(){
        return javax.ws.rs.core.Response.ok("hallo123").build();
    }

}
