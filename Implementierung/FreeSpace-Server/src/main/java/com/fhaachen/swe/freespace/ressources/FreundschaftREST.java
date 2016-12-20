package com.fhaachen.swe.freespace.ressources;


import com.fhaachen.swe.freespace.main.Freundschaft;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

/**
 * Die Klasse FreundschaftREST ist die Schnittstelle von HTTP-Request und Server-Logik. Es werden die HTTP-Methoden GET,
 * POST, PUT und DELETE als REST-Service realisiert, mit dem Pfad http://-Server Domain Namespace-/freundschaft
 *
 * @author Simon Catley
 * @version 1.1
 */

@RolesAllowed({"user", "professor"})
@Path("/freundschaft")
public class FreundschaftREST {

    /**
     * Diese Methode realisiert den HTTP-GET-Request. Sie erhaelt als Übergabeparameter einen SecurityContext context
     * aus welcher die ID des über Basic Authentication angemeldeten Benutzers entnommen werden kann.
     * Liefert ein Response-Objekt mit HTTP-Statuscode und einem Json-String mit den Freundschaften des angemeldeten
     * Benutzers an den Aufrufer zurück.
     *
     * @param context SecurityContext des angemeldeten Benutzers
     * @return Response-Objekt mit HTTP-Statuscode und Json-String mit den Freundschaften des Aufrufers
     */

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFreundschaften(@Context SecurityContext context){
        String benutzerID = context.getUserPrincipal().getName();
        return Freundschaft.getFreundschaften(benutzerID);
    }

    /**
     * Die Realisierung des POST-Request. Erstellt einen neuen Freundschaftseintrag in der Datenbank für den angemeldeten
     * Benutzer, dessen ID über den SecurityContext context ermittelt wird. Des weiteren erhält diese Methode einen String json
     * im Json-Format, welcher die Email des hinzuzufügenden Freundes enthält. Liefert ein Response-Objekt mit
     * HTTP-Statuscode und einem Json-String mit erstelltem Freundschaftseintrag an den Aufrufer zurück.
     *
     * @param json String im Json-Format mit Email-Adresse des hinzuzufügenden Freundes
     * @param context SecurityContext des angemeldeten Benutzers
     * @return Response-Objekt mit HTTP-Statuscode und Json-String mit erstelltem Freundschaftseintrag
     */

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postFreundschaft(String json, @Context SecurityContext context){
        String benutzerID = context.getUserPrincipal().getName();
        return Freundschaft.postFreundschaft(benutzerID, json);
    }

    /**
     * Die Realisierung des PUT-Request. Aendert den Status einer bestehenden Freundschaft in der Datenbank von 0 auf 1
     * (Freundschaftsannahme). Die gesuchte Freundschaft ist zwischen dem Benutzer mit der ID, die im Überabeparameter id
     * enthalten ist und dem angemeldeten Benutzer. Liefert ein Response-Objekt mit
     * HTTP-Statuscode und einem Json-String mit geaendertem Freundschaftseintrag an den Aufrufer zurück.
     *
     * @param id ID des Freundschaft-Anfragenden
     * @param context SecurityContext des angemeldeten Benutzers
     * @return Response-Objekt mit HTTP-Statuscode und Json-String mit geaendertem Freundschaftseintrag
     */

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value="/{param}")
    public Response putFreundschaftID(@PathParam(value="param") String id, @Context SecurityContext context){
        String benutzerID = context.getUserPrincipal().getName();
        return Freundschaft.putFreundschaft(benutzerID, id);
    }

    /**
     * Die Realisierung des DELETE-Request. Löscht die Freundschaft zwischen dem Benutzer mit der ID, die im Überabeparameter id
     * enthalten ist und dem angemeldeten Benutzer(auch Freundschaftsablehnung). Liefert ein Response-Objekt mit
     * HTTP-Statuscode und einem Json-String mit gelöschtem Freundschaftseintrag an den Aufrufer zurück.
     *
     * @param id ID des Freundes
     * @param context SecurityContext des angemeldeten Benutzers
     * @return Response-Objekt mit HTTP-Statuscode und Json-String mit gelöschtem Freundschaftseintrag
     */

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value="/{param}")
    public Response deleteFreundschaftID(@PathParam(value="param") String id, @Context SecurityContext context){
        String benutzerID = context.getUserPrincipal().getName();
        return Freundschaft.deleteFreundschaft(benutzerID, id);
    }
}
