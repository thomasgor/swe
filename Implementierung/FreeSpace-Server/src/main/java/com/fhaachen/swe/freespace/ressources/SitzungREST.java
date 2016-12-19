package com.fhaachen.swe.freespace.ressources;

import com.fhaachen.swe.freespace.Antwort;
import com.fhaachen.swe.freespace.main.Sitzung;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

/**
 * Die Klasse SitzungREST ist die Schnittstelle von HTTP-Request und Server-Logik. Es werden die HTTP-Methoden GET,
 * POST, PUT und DELETE als REST-Service realisiert, mit dem Pfad http://-Server Domain Namespace-/sitzung
 *
 * @author Simon Catley
 * @version 1.1
 */
@Path( value = "/sitzung")
@RolesAllowed({"user", "professor"})
public class SitzungREST {


/**
 * Diese Methode realisiert den HTTP-GET-Request. Sie erhaelt als Übergabeparameter einen SecurityContext context
 * aus welcher die ID des über Basic Authentication angemeldeten Benutzers entnommen werden kann.
 * Liefert ein Response-Objekt mit HTTP-Statuscode und einem Json-String mit der Sitzung des angemeldeten
 * Benutzers an den Aufrufer zurück.
 *
 * @param context SecurityContext des angemeldeten Benutzers
 * @return Response-Objekt mit HTTP-Statuscode und Json-String mit der Sitzung des Aufrufers
 */

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSitzung(@Context SecurityContext context){
        String benutzerID = context.getUserPrincipal().getName();
        return Sitzung.getSitzungById(benutzerID);
    }
    /**
     * Die Realisierung des POST-Request. Erstellt einen neuen Sitzungseintrag in der Datenbank für den angemeldeten
     * Benutzer, dessen ID über den SecurityContext context ermittelt wird. Des weiteren erhält diese Methode einen String json
     * im Json-Format, welcher die RaumID des Raumes, indem die Sitzung erstellt werden soll, enthält.
     * Liefert ein Response-Objekt mit HTTP-Statuscode und einem Json-String mit erstelltem Sitzungseintrag an den Aufrufer zurück.
     *
     * @param json String im Json-Format mit RaumId des Raumes der Sitzung
     * @param context SecurityContext des angemeldeten Benutzers
     * @return Response-Objekt mit HTTP-Statuscode und Json-String mit erstelltem Sitzungseintrag
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postSitzung(String json, @Context SecurityContext context){
        String benutzerID = context.getUserPrincipal().getName();
        return Sitzung.postSitzung(json, benutzerID);
    }
    /**
     * Die Realisierung des DELETE-Request. Löscht den Sitzungseintrag des angemeldeten Benutzers aus der Datenbank,
     * falls die im Übergabeparameter id enthaltene id gleich der des angemeldeten Benutzers ist.
     * Liefert ein Response-Objekt mit HTTP-Statuscode und einem Json-String mit gelöschtem Sitzungsseintrag an den
     * Aufrufer zurück.
     *
     * @param id ID des Freundes
     * @param context SecurityContext des angemeldeten Benutzers
     * @return Response-Objekt mit HTTP-Statuscode und Json-String mit gelöschtem Freundschaftseintrag
     */
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value="/{param}")
    public Response deleteSitzungID(@PathParam(value="param") String id, @Context SecurityContext context){
        String benutzerID = context.getUserPrincipal().getName();
        if(!id.equals(benutzerID)){
            return Antwort.FORBIDDEN;
        }

        return Sitzung.deleteSitzung(benutzerID);
    }

    /**
     * Die Realisierung des PUT-Request. Setzt die Endzeit einer Sitzung auf den Ausgangswert zurück.
     * Die gesuchte Sitzung wird über die im Überabeparameter id enthaltene ID ermittelt. Die übermittelte ID und die des
     * angemeldeten Benutzers müssen hierbei gleich sein. Liefert ein Response-Objekt mit
     * HTTP-Statuscode und einem Json-String mit geaendertem Sitzungseintrag an den Aufrufer zurück.
     *
     * @param id ID des Sitzungsinhabers
     * @param context SecurityContext des angemeldeten Benutzers
     * @return Response-Objekt mit HTTP-Statuscode und Json-String mit geaendertem Sitzungseintrag
     */

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value="/{param}")
    public Response putSitzungID(@PathParam(value="param") String id, @Context SecurityContext context){
        String benutzerID = context.getUserPrincipal().getName();
        if(!id.equals(benutzerID)){
            return Antwort.FORBIDDEN;
        }

        return Sitzung.putSitzung(id);
    }
}
