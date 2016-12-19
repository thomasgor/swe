package com.fhaachen.swe.freespace.ressources;

import com.fhaachen.swe.freespace.main.Benutzer;
import com.fhaachen.swe.freespace.main.Konfiguration;
import org.javalite.common.Base64;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
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
    public Response login(@FormParam("user") String user, @FormParam("pw") String pw, @CookieParam("Basic") NewCookie c)
    {
        String html = new String();
        @SuppressWarnings("Since15") String base = Base64.getEncoder().encodeToString((user+":"+pw).getBytes());
        NewCookie cookie = new NewCookie("Basic", base, "/", "","FreeSpace-Server", 600,false);

        try {
            //Login erfolgreich
            if (Benutzer.istAdministrator(user, pw)) {
                html = Konfiguration.fileToString("admin/einstellungen.html");


            } else {
                html = Konfiguration.getLoginFehlerHTML();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return Response.ok(html, MediaType.TEXT_HTML).cookie(cookie).build();
    }
}

