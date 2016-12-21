package com.fhaachen.swe.freespace.ressources;

import com.fhaachen.swe.freespace.Antwort;
import com.fhaachen.swe.freespace.main.Benutzer;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

/**
 * Die Klasse BenutzerREST ist die Schnittstelle von HTTP-Request und Server-Logik. Es werden die HTTP-Methoden POST und
 * PUT als REST-Service realisiert, mit dem Pfad http://-Server Domain Namespace-/benutzer
 * {@link Benutzer Server-Logik}
 *
 * @author Thomas Gorgels
 * @version 1.6
 */

@Path("benutzer")
public class BenutzerREST {

    /**
     * Die Realisierung des POST-Request. Erstellt einen neuen Benutzereintrag in der Datenbank, mit der ID, die im
     * Json-String json enthalten ist. Liefert ein Response-Objekt mit HTTP-Statuscode und einem Json-String mit
     * erstelltem Benutzereintrag an den Aufrufer zurück.
     *
     * @param json String im Json-Format mit ID des neuen Benutzers
     * @return Response-Objekt mit HTTP-Statuscode und Json-String mit erstelltem Benutzereintrag
     */

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postBenutzer(String json){
        String result = null;
        try {
            result = Benutzer.postBenutzer(json);
        }catch(Exception e){
            System.out.println(e);
        }

        System.out.println(result);
        if(result == null){
            return Antwort.BAD_REQUEST;
        }
        return Response.ok(result, MediaType.APPLICATION_JSON).build();
    }

    /**
     * Die Realisierung des PUT-Request. Aendert die Flag istProfessor eines Benutzers in der Datenbank auf 1.
     * Der zu aendernde Benutzereintrag ist der des angemeldeten Benutzers und wird somit aus dem
     * SecurityContext context ermittelt. Im Übergabeparameter json wird das in der Datenbank festgehaltene Masterpasswort
     * gesendet. Liefert ein Response-Objekt mit HTTP-Statuscode und einem Json-String mit geaendertem Benutzereintrag
     * an den Aufrufer zurück.
     *
     * @param json Json-String mit Masterpasswort
     * @param context SecurityContext des angemeldeten Benutzers
     * @return Response-Objekt mit HTTP-Statuscode und Json-String mit geaendertem Benutzereintrag
     */

    @PUT
    @RolesAllowed({"user", "professor"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response putBenutzer(String json, @Context SecurityContext context){
        String benutzerID = context.getUserPrincipal().getName();
        String result = Benutzer.putBenutzer(json, benutzerID);

        if(result == null){
            return Antwort.FORBIDDEN;
        }

        return Response.ok(result, MediaType.APPLICATION_JSON).build();
    }

}
