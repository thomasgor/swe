package com.fhaachen.swe.freespace.ressources;

import com.fhaachen.swe.freespace.main.Tag;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Die Klasse TagREST ist die Schnittstelle von HTTP-Request und Server-Logik. Es werden die HTTP-Methoden GET,
 * POST und DELETE als REST-Service realisiert, mit dem Pfad http://-Server Domain Namespace-/tag
 * {@link Tag Server-Logik}
 *
 * @author Simon Catley
 * @version 1.0
 */

@Path( value = "/tag")
@RolesAllowed({"user", "professor"})
public class TagREST {

    /**
     * Diese Methode realisiert den HTTP-GET-Request.Sie liefert ein Response-Objekt mit HTTP-Statuscode und einem
     * Json-String mit allen Tags, die auf der Datenbank gespeichert sind, an den Aufrufer zurück.
     *
     * @return Response-Objekt mit HTTP-Statuscode und Json-String mit einer Liste aller Tags
     */

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTagListe(){
        return Tag.getTagListe();
    }

    /**
     * Die Realisierung des POST-Request. Erstellt einen neuen Tageintrag in der Datenbank. Der Name des neuen Tags ist
     * im String json im Json-Format enthalten. Liefert ein Response-Objekt mit HTTP-Statuscode und einem Json-String
     * mit erstelltem Tag an den Aufrufer zurück.
     *
     * @param json String im Json-Format mit dem Namen des neuen Tag
     * @return Response-Objekt mit HTTP-Statuscode und Json-String mit erstelltem Tag
     */

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    public Response postTag(String json){
        return Tag.postTag(json);
    }

    /**
     * Die Realisierung des DELETE-Request. Löscht den Tageintrag aus der Datenbank, mit der in dem String id
     * enthaltene ID. Liefert ein Response-Objekt mit HTTP-Statuscode und einem Json-String mit gelöschtem
     * Sitzungsseintrag an den Aufrufer zurück.
     *
     * @param id ID des zu löschenden Tags
     * @return Response-Objekt mit HTTP-Statuscode und Json-String mit gelöschtem Tageintrag
     */

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    @Path(value="/{param}")
    public Response deleteTagID(@PathParam(value="param") String id){
        return Tag.deleteTag(id);
    }
}
