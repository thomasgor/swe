package com.fhaachen.swe.freespace.ressources;

import com.fhaachen.swe.freespace.main.Weg;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Die Klasse WegREST ist die Schnittstelle von HTTP-Request und Server-Logik. Es wird die HTTP-Methoden GET
 * als REST-Service realisiert, mit dem Pfad http://-Server Domain Namespace-/weg
 * {@link Weg Server-Logik}
 *
 * @author Simon Catley
 * @version 1.0
 */

@Path( value = "/weg")
@RolesAllowed({"user", "professor"})
public class WegREST {

    /**
     * Diese Methode realisiert den HTTP-GET-Request. Sie erhaelt als Übergabeparameter zwei Strings, die die ID's des
     * Startraumes(startID)und des Zielraumes(zielID) enthalten.
     * Liefert ein Response-Objekt mit HTTP-Statuscode und einem Json-String mit dem kürzesten weg von start nach ziel
     * als geordnete Liste an den Aufrufer zurück.
     *
     * @return Response-Objekt mit HTTP-Statuscode und Json-String mit dem kürzesten weg von start nach ziel als geordnete Liste
     */

    @GET
        @Path(value="/{param1}/{param2}")
        @Produces(MediaType.APPLICATION_JSON)
        public Response getWeg(@PathParam(value="param1") String startID ,@PathParam(value="param2") String zielID){
            return Weg.getWeg(startID, zielID);
        }
}
