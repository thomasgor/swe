package com.fhaachen.swe.freespace.ressources;

import com.fhaachen.swe.freespace.main.Benutzer;
import com.fhaachen.swe.freespace.main.Konfiguration;
import org.javalite.common.Base64;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.List;

/**
 * Die Klasse KonfigurationREST ist die Schnittstelle von HTTP-Request und Server-Logik. Es werden die HTTP-Methoden GET und
 * POST als REST-Service realisiert, mit dem Pfad http://-Server Domain Namespace-/konfiguration
 * {@link Konfiguration Server-Logik}
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
    public Response getKonfiguration(@CookieParam("Basic") NewCookie cookie,@Context HttpHeaders headers) {

        Map<String, Cookie> cookies = headers.getCookies();
        Cookie myCookie = cookies.get("Basic");
        String cookieValue = "";

        if (myCookie != null) {
            cookieValue = myCookie.getValue();
        }

        String html = null;
        try {
            html = Konfiguration.fileToString("admin/login.html");
            if (!Benutzer.istAdministrator(cookieValue)) {
                html = Konfiguration.fileToString("admin/login.html");
            } else {
                html = Konfiguration.getEinstellungenHTML();
                return Response.ok(html, MediaType.TEXT_HTML).cookie(cookie).build();
            }
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
         * @param headers Enthält die HTTP headers zum überprüfen aller angelegten Cookies
         * @return Response-Objekt mit HTML-Seite, die entweder Erfolg oder Fehler anzeigt
         */

        @POST
        @Produces(MediaType.TEXT_HTML)
        public Response login (@FormParam("user") String user, @FormParam("pw") String pw, @FormParam("action") String
        action, @FormParam("input_masterpassword_1") String master1, @FormParam("input_masterpassword_2") String
        master2, @FormParam("input_altespasswort") String altMaster,
                @FormParam("input_sitzungsintervall") String intervall, @FormParam("input_tag") String
        neuerTag, @FormParam("tags") List < String > tags, @Context HttpHeaders headers){
            String html = "";

            if (action.equals("login")) {

                @SuppressWarnings("Since15") String base = Base64.getEncoder().encodeToString((user + ":" + pw).getBytes());
                NewCookie cookie = new NewCookie("Basic", base, "/", "", "FreeSpace-Server", 600, false);
                try {
                    //Login erfolgreich
                    if (Benutzer.istAdministrator(user, pw)) {
                        html = Konfiguration.getEinstellungenHTML();
                    } else {
                        html = Konfiguration.getLoginFehlerHTML();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return Response.ok(html, MediaType.TEXT_HTML).cookie(cookie).build();

            } else if (action.equals(("logout"))) {

                html = Konfiguration.getLogoutHTML();
                System.out.println("LOGOUT");
                NewCookie cookie = new NewCookie("Basic", "deleted", "/", "", "FreeSpace-Server", 0, false);
                return Response.ok(html, MediaType.TEXT_HTML).cookie(cookie).build();

            } else if (action.equals("speichern")) {
                System.out.println("intervall" + intervall);
                System.out.println("master1:" + master1 + " master2:" + master2);
                System.out.println("altmaster:" + altMaster);
                System.out.println("neuerTag" + neuerTag);
                System.out.println("zu löschen" + tags.toString());


                try {
                    Map<String, Cookie> cookies = headers.getCookies();
                    Cookie myCookie = cookies.get("Basic");
                    String cookieValue = "";
                    if (myCookie != null) {
                        cookieValue = myCookie.getValue();
                    }
             /*   byte[] code =  Base64.getDecoder().decode(cookieValue);
                String klartext = new String(code); // UTF8 als zweiten parameter mitgeben
                String[] userAndPw = klartext.split(":");
                System.out.println("Klartext:"+ klartext); */
                    //Login erfolgreich
                    if (Benutzer.istAdministrator(cookieValue)) { //userAndPw[0],userAndPw[1]
                        if (!Konfiguration.setKonfiguration(altMaster, master1, master2, tags, neuerTag, intervall)) {
                            System.out.println("Fehlerhafte HTML");
                            html = Konfiguration.getEinstellungen_FehlerhaftHTML();
                            return Response.ok(html, MediaType.TEXT_HTML).build();
                        }

                        html = Konfiguration.getEinstellungenSaved();
                        //return Response.ok(html, MediaType.TEXT_HTML).cookie(myCookie).build();

                    } else {
                        html = Konfiguration.fileToString("admin/login.html");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return Response.ok(html, MediaType.TEXT_HTML).build();
            }

            return Response.ok(html, MediaType.TEXT_HTML).build();
        }

}

