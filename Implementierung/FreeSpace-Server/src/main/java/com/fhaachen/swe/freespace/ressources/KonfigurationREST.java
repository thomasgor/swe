package com.fhaachen.swe.freespace.ressources;

import com.fhaachen.swe.freespace.main.Benutzer;
import com.fhaachen.swe.freespace.main.Konfiguration;
import org.javalite.common.Base64;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Die Klasse KonfigurationREST ist die Schnittstelle von HTTP-Request und Server-Logik. Es werden die HTTP-Methoden GET und
 * POST als REST-Service realisiert, mit dem Pfad http://-Server Domain Namespace-/konfiguration
 *
 * @author Thomas Gorgels
 * @version 1.0
 */

@Path("/konfiguration")
public class KonfigurationREST {

    /**
     * Diese Methode realisiert den HTTP-GET-Request. Sie erhaelt als Übergabeparameter einen cookie, der die
     * Basic Authentication des Benutzers enthaelt. Liefert ein Response-Objekt mit einer HTML-Seite an den Aufrufer zurück.
     *
     * @param cookie Basic Authentication des Benutzers im Cookie
     * @return Response-Objekt mit HTML-Seite
     */

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response getKonfiguration(@CookieParam("Basic") NewCookie cookie){
        if(cookie != null){
            String[] values = cookie.toString().split(";");
            if(Konfiguration.validiereCookie(values[0])){
                String html = Konfiguration.getEinstellungenHTML();
                return Response.ok(html, MediaType.TEXT_HTML).cookie(cookie).build();
            }
        }
        String html = "";
        try {
            html = Konfiguration.fileToString("admin/login.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Response.ok(html, MediaType.TEXT_HTML).build();
    }

    /**
     * Diese Methode realisiert den HTTP-POST-Request. Sie bietet die Möglichkeit entweder ein Benutzer anzumelden, abzumelden
     * oder die Konfiguration zu aendern. Dafür erhaelt sie als Übergabeparameter Formparameter einer HTML-Formim String action
     * wird die jeweils ggewünschte Aktion deklariert. Die anderen Übergabeparameter sind je nach Aktion entweder null, oder
     * mit notwendigen Daten befüllt. Liefert ein Response-Objekt mit einer HTML-Seite an den Aufrufer zurück.
     *
     * @param user Benutzername des anzumeldenden Benutzers
     * @param pw Passwort des anzumeldenden Benutzers
     * @param action Gewünschte Aktion(Anmelden, Abmelden, Konfiguration aendern)
     * @param master1 Neues zu setzendes Masterpasswort
     * @param master2 Wiederholung des neuen Masterpasswortes
     * @param altMaster Altes Masterpasswort zur Sicherheitsabfrage
     * @param intervall Neues Sitzungsintervall
     * @param neuerTag Tagname eines neu hinzuzufügenden Tags
     * @param tags Liste zu löschender Tags
     * @return Response-Objekt mit HTML-Seite, die entweder Erfolg oder Fehler anzeigt
     */

    @POST
    @Produces(MediaType.TEXT_HTML)
    public Response login(@FormParam("user") String user, @FormParam("pw") String pw, @FormParam("action") String action,@FormParam("input_masterpassword_1") String master1, @FormParam("input_masterpassword_2") String master2, @FormParam("input_altespasswort") String altMaster,
                          @FormParam("input_sitzungsintervall") String intervall, @FormParam("input_tag") String neuerTag, @FormParam("tags") List<String> tags)
    {
        String html ="";

        if(action.equals("login")){

            @SuppressWarnings("Since15") String base = null;
            try {
                System.out.println();
                base = Base64.getEncoder().encodeToString((user+":"+pw).getBytes("ASCII"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
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
            System.out.println("LOGOUT");
            NewCookie cookie = new NewCookie("Basic", "", "/", "","FreeSpace-Server", 0,false);
            return Response.ok(html, MediaType.TEXT_HTML).cookie(cookie).build();

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
}

