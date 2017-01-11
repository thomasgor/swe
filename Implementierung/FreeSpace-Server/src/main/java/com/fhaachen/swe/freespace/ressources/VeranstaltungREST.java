package com.fhaachen.swe.freespace.ressources;

import com.fhaachen.swe.freespace.Antwort;
import com.fhaachen.swe.freespace.JsonHelper;
import com.fhaachen.swe.freespace.main.Veranstaltung;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

/**
 * Die Klasse VeranstaltungREST ist die Schnittstelle von HTTP-Request und Server-Logik. Es werden die HTTP-Methoden GET,
 * POST, PUT und DELETE als REST-Service realisiert, mit dem Pfad http://-Server Domain Namespace-/veranstaltung
 * {@link Veranstaltung Server-Logik}
 *
 * @author Thomas Gorgels
 * @version 1.2
 */

@Path( value = "/veranstaltung")
@RolesAllowed("professor")
public class VeranstaltungREST {

    /**
     * Diese Methode realisiert den HTTP-GET-Request. Sie erhaelt als Übergabeparameter einen SecurityContext context
     * aus welcher die ID des über Basic Authentication angemeldeten Benutzers entnommen werden kann. Es sind nur Benutzer,
     * die als Professoren markiert sind erlaubt.
     * Liefert ein Response-Objekt mit HTTP-Statuscode und einem Json-String mit den Veranstaltungen des angemeldeten
     * Professors an den Aufrufer zurück.
     *
     * @param context SecurityContext des angemeldeten Benutzers
     * @return Response-Objekt mit HTTP-Statuscode und Json-String mit den Veranstaltungen des angemeldeten Benutzers
     */

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVeranstaltungsliste(@Context SecurityContext context){
        String professorID = context.getUserPrincipal().getName();
        String response = Veranstaltung.getVeranstaltungsListe(professorID);
        return Response.ok(response, MediaType.APPLICATION_JSON).build();
    }

    /**
     * Diese Methode realisiert den HTTP-POST-Request. Sie erhaelt als Übergabeparameter einen SecurityContext context
     * aus welcher die ID des über Basic Authentication angemeldeten Benutzers entnommen werden kann. Es sind nur Benutzer,
     * die als Professoren markiert sind erlaubt. Asserdem erhaelt sie einen Json-String, welchen die hinzuzufuegenden
     * Veranstaltung enthaelt.
     * Liefert bei Erfolg ein Response-Objekt mit HTTP-Statuscode und einem Json-String mit der neu erstellten Veranstaltung des
     * angemeldeten Professors an den Aufrufer zurück. Anderenfalls den Statuscode Room Blocked.
     *
     * @param json die hinzuzufuegende Veranstaltung
     * @param context SecurityContext des angemeldeten Benutzers
     * @return Response-Objekt mit HTTP-Statuscode und Json-String mit der erstellten Veranstaltung
     */

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postVeranstaltung(String json, @Context SecurityContext context){
        String professorID = context.getUserPrincipal().getName();
        String response = Veranstaltung.postVeranstaltung(json, professorID);

        if(response == null){
            return Antwort.ROOM_BLOCKED;
        }
        return Response.ok(response, MediaType.APPLICATION_JSON).build();
    }

    /**
     * Diese Methode realisiert den HTTP-GET-Request. Sie erhaelt als Übergabeparameter einen SecurityContext context
     * aus welcher die ID des über Basic Authentication angemeldeten Benutzers entnommen werden kann. Es sind nur Benutzer,
     * die als Professoren markiert sind erlaubt. Sie erhaelt ausserdem die ID der gesuchten Veranstaltung im String id.
     * Liefert ein Response-Objekt mit HTTP-Statuscode und einem Json-String mit der gesuchten Veranstaltung des angemeldeten
     * Professors an den Aufrufer zurück.
     *
     * @param id ID der gesuchten Veranstaltung
     * @param context SecurityContext des angemeldeten Benutzers
     * @return Response-Objekt mit HTTP-Statuscode und Json-String mit der gesuchten Veranstaltung des angemeldeten Benutzers
     */

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value="/{param}")
    public Response getVeranstaltungID(@PathParam(value="param") String id ,@Context SecurityContext context){
        String professorID = context.getUserPrincipal().getName();
        String response = Veranstaltung.getVeranstaltungByID(id);

        if(response == null){
            return Antwort.NOT_FOUND;
        }

        String responseProefessorID = JsonHelper.getAttribute(response, "benutzer");

        if(!professorID.equals(responseProefessorID)){
            System.out.println("Benutzer " + professorID + " hat versucht eine Veranstaltung von "+ responseProefessorID + " zu laden");
            return Antwort.FORBIDDEN;
        }
        return Response.ok(response, MediaType.APPLICATION_JSON).build();
    }

    /**
     * Diese Methode realisiert den HTTP-PUT-Request. Sie erhaelt als Übergabeparameter einen SecurityContext context
     * aus welcher die ID des über Basic Authentication angemeldeten Benutzers entnommen werden kann. Es sind nur Benutzer,
     * die als Professoren markiert sind erlaubt. Asserdem erhaelt sie die ID der zu aendernden Veranstaltung im String id und
     * einen Json-String json, welcher die zu aendernden Daten der Veranstaltung enthaelt.
     * Liefert bei Erfolg ein Response-Objekt mit HTTP-Statuscode und einem Json-String mit der geaenderten Veranstaltung des
     * angemeldeten Professors an den Aufrufer zurück, oder den Statuscode Room Blocked/Not Found/Forbidden respektive.
     *
     * @param id Die ID der zu aendernden Veranstaltung
     * @param json Die zu aendernden Daten der Veranstaltung im Json-Format
     * @param context SecurityContext des angemeldeten Benutzers
     * @return Response-Objekt mit HTTP-Statuscode und Json-String mit der geaendernten Veranstaltung
     */

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value="/{param}")
    public Response putVeranstltungID(@PathParam(value="param") String id, String json, @Context SecurityContext context){
        String professorID = context.getUserPrincipal().getName();
        String response = Veranstaltung.putVeranstaltungByID(id, json, professorID);

        if(response.equals("ROOM_BLOCKED")){
            return Antwort.ROOM_BLOCKED;
        }

        if(response.equals("NOT_FOUND")){
            return Antwort.NOT_FOUND;
        }

        if(response.equals("FORBIDDEN")){
            return Antwort.FORBIDDEN;
        }
        return Response.ok(response, MediaType.APPLICATION_JSON).build();
    }

    /**
     * Diese Methode realisiert den HTTP-DELETE-Request. Sie erhaelt als Übergabeparameter einen SecurityContext context
     * aus welcher die ID des über Basic Authentication angemeldeten Benutzers entnommen werden kann. Es sind nur Benutzer,
     * die als Professoren markiert sind erlaubt. Asserdem erhaelt sie einen String id, welchern die ID der zu
     * loeschenden Veranstaltung enthaelt.
     * Liefert bei Erfolg ein Response-Objekt mit HTTP-Statuscode und einem Json-String mit der geloeschten Veranstaltung des
     * angemeldeten Professors an den Aufrufer zurück. Anderenfalls den Statuscode Not Found bzw. Forbidden.
     *
     * @param id ID der zu loeschenden Veranstaltung
     * @param context SecurityContext des angemeldeten Benutzers
     * @return Response-Objekt mit HTTP-Statuscode und Json-String mit der geloeschten Veranstaltung
     */

    @DELETE
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path(value="/{param}")
    public Response deleteVeranstaltungID(@PathParam(value="param") String id, @Context SecurityContext context){
        String professorID = context.getUserPrincipal().getName();
        String response = Veranstaltung.deleteVeranstaltung(id, professorID);

        if(response.equals("NOT_FOUND")){
            return Antwort.NOT_FOUND;
        }

        if(response.equals("FORBIDDEN")){
            return Antwort.FORBIDDEN;
        }
        return Response.ok().build();
    }
}
