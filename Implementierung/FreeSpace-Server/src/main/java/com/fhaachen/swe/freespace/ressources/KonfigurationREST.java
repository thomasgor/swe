package com.fhaachen.swe.freespace.ressources;

import com.fhaachen.swe.freespace.Antwort;
import com.fhaachen.swe.freespace.main.Benutzer;
import com.fhaachen.swe.freespace.main.Konfiguration;
import org.javalite.common.Base64;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.util.List;

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
    public Response login(@FormParam("user") String user, @FormParam("pw") String pw, @FormParam("action") String action,@FormParam("input_masterpassword_1") String master1, @FormParam("input_masterpassword_2") String master2, @FormParam("input_altespasswort") String altMaster,
                          @FormParam("input_sitzungsintervall") String intervall, @FormParam("input_tag") String neuerTag, @FormParam("tags") List<String> tags)
    {
        String html ="";

        if(action.equals("login")){

            @SuppressWarnings("Since15") String base = Base64.getEncoder().encodeToString((user+":"+pw).getBytes());
            NewCookie cookie = new NewCookie("Basic", base, "/", "","FreeSpace-Server", 600,false);

            try {
                //Login erfolgreich
                if (Benutzer.istAdministrator(user, pw)) {
                    html = Konfiguration.getEinstellungenHTML();
                } else {
                    html = Konfiguration.getLoginFehlerHTML();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return Response.ok(html, MediaType.TEXT_HTML).cookie(cookie).build();

        }else if(action.equals(("logout"))){
            html = Konfiguration.getLogoutHTML();
            return Response.ok(html, MediaType.TEXT_HTML).build();

        }else if(action.equals("speichern")){
            System.out.println("intervall" + intervall);
            System.out.println("master1:" + master1 + " master2:" + master2);
            System.out.println("altmaster:" + altMaster);
            System.out.println("neuerTag" + neuerTag);
            System.out.println("zu löschen" + tags.toString());
            html = Konfiguration.getEinstellungenSaved();
            return Response.ok(html, MediaType.TEXT_HTML).build();
        }

        return Response.ok(html, MediaType.TEXT_HTML).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public Response putKonfiguration(@FormParam("input_masterpassword_1") String master1, @FormParam("input_masterpassword_2") String master2, @FormParam("input_altespasswort") String altMaster,
                                     @FormParam("input_sitzungsintervall") String intervall, @FormParam("input_tag") String neuerTag, @FormParam("tags") List<String> tags) {
        //if(setKonfiguration(altMaster, master1, master2, tags, neuerTag, intervall)) {
        //    return Response.ok(Konfiguration.getEinstellungenSaved(), MediaType.TEXT_HTML).build();
        //}
        return Antwort.INTERNAL_SERVER_ERROR;
    }

}

