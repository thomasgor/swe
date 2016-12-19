package com.fhaachen.swe.freespace.ressources;

import com.fhaachen.swe.freespace.main.Benutzer;
import com.fhaachen.swe.freespace.main.Konfiguration;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.File;
import java.io.IOException;

/**
 * Created by thomasgorgels on 19.12.16.
 */

@Path("/konfiguration")
public class KonfigurationREST {


    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response getKonfiguration(){
        String html = null;
        try {
            html = Konfiguration.fileToString("admin/login.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Response.ok(html, MediaType.TEXT_HTML).build();
    }

    @POST
    @Produces(MediaType.TEXT_HTML)
    public Response login(@FormParam("user") String user, @FormParam("pw") String pw)
    {
        String html = new String();
        try {
            if (Benutzer.istAdministrator(user, pw)) {
                html = Konfiguration.fileToString("admin/einstellungen.html");
            } else {
                html = Konfiguration.getLoginFehlerHTML();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return Response.ok(html, MediaType.TEXT_HTML).build();
    }
}
