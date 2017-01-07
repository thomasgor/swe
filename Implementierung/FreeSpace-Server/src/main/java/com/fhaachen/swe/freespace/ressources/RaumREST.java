package com.fhaachen.swe.freespace.ressources;

import com.fhaachen.swe.freespace.Antwort;
import com.fhaachen.swe.freespace.JsonHelper;
import com.fhaachen.swe.freespace.Server;
import com.fhaachen.swe.freespace.main.Raum;
import com.fhaachen.swe.freespace.main.Sitzung;

import javax.annotation.security.RolesAllowed;
import javax.imageio.ImageIO;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Die Klasse RaumREST ist die Schnittstelle von HTTP-Request und Server-Logik. Es werden die HTTP-Methoden GET und
 * PUT als REST-Service realisiert, mit dem Pfad http://-Server Domain Namespace-/raum
 * {@link Raum Server-Logik}
 *
 * @author Patrick Wueller
 * @version 1.4
 */

@Path( value = "/raum")
@RolesAllowed({"user", "professor"})
public class RaumREST {

    /**
     * Diese Methode realisiert den HTTP-GET-Request. Sie liefert ein Response-Objekt mit einem Json-String, welcher
     * alle auf dem Server befindlichen Räume enthaelt, an den Aufrufer zurück.
     *
     * @return Response-Objekt mit Json-String, der alle Raeme enthaelt
     */

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRaum(){

        String json = Raum.getRaum();
        if(json == null){
            return Antwort.BAD_REQUEST;
        }

        return Response.ok(json,MediaType.APPLICATION_JSON).build();
        //return Response.status(Response.Status.NOT_IMPLEMENTED).entity("Hier ensteht die Raumliste").build();
    }

    /**
     * Diese Methode realisiert den HTTP-GET-Request. Sie erhaelt als Uebergabeparameter einen String id, welcher die ID des
     * gesuchten Raumes enthaelt und liefert ein Response-Objekt mit einem Json-String, welcher
     * das Raum-Objekt enthaelt, an den Aufrufer zurück.
     *
     * @return Response-Objekt mit Json-String, der das gesuchte Raum-Objekt enthaelt
     */

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value="/{param}")
    public Response getRaumdetails(@PathParam(value="param") String id){

        String json = Raum.getRaumdetails(id);
        if(json == null) return Antwort.BAD_REQUEST;

        return Response.ok(Raum.getRaumdetails(id), MediaType.APPLICATION_JSON).build();
        //return Response.status(Response.Status.NOT_IMPLEMENTED).entity("Raumdetails von " + id).build();
    }

    /**
     * Die Realisierung des PUT-Request. Setzt den Tag eines Raumes den im Json-String json enthaltenen Tag, falls der über
     * den SecurityContext context erhaltenen angemeldete Benutzer eine Sitzung in dem Raum mit der ID id hat und Tagholder ist.
     * Liefert ein Response-Objekt mit einem Json-String mit geaendertem Raum-Objekt an den Aufrufer zurück.
     *
     * @param id ID des Raumes
     * @param json Json-String mit neuem Tag
     * @param context SecurityContext des angemeldeten Benutzers
     * @return Response-Objekt mit HTTP-Statuscode und Json-String mit geaendertem Raum
     */

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value="/{param}")
    public Response putRaumdetails(@PathParam(value="param") String id, String json, @Context SecurityContext context) {
        String tag = JsonHelper.getAttribute(json,"tag");
        String username = context.getUserPrincipal().getName(); // BenutzerID
        String answer = null;


        //Prüfe ob ein tag angegeben ist
        if(!json.contains("\"tag\":")){
            return Antwort.INTERNAL_SERVER_ERROR;
        }


        boolean raumHasTagHolder = Sitzung.raumHasTag(id);

        if(Sitzung.hasSessionInRoom(id, username)) {
            //Gibt es einen Tagholder?
            if (raumHasTagHolder) {

                boolean isAllowed = Sitzung.istTagBesitzer(username, id);

                // User hat keine Berechtigung tag zu setzen
                if (!isAllowed) {
                    //Überliefert dennoch das Raumobjekt
                    System.out.println("User hat keine Berechtigung: aber eigentlicher raum wird zurückgegeben");
                    String s = Raum.getRaumdetails(id);
                    return Response.status(Response.Status.FORBIDDEN).entity(s).build();
                }


                if(tag.equals("null"))
                    tag = null;
                //Ich bin der Tagholder
                answer = Raum.putRaumID(id, tag, username);

            } else {
                if (Sitzung.setHasTag(username)) {
                    answer = Raum.putRaumID(id, tag, username);
                }
            }
        } else {
            return Antwort.NO_ACTIVE_SESSION;
        }


        //Es existiert kein Raum mit der ID
        if(answer == null){
            return Antwort.INTERNAL_SERVER_ERROR;
        }

        return Response.ok(answer, MediaType.APPLICATION_JSON).build();
    }

    /**
     * Diese Methode realisiert den HTTP-GET-Request. Sie erhaelt als Uebergabeparameter einen String id, welcher die ID des
     * gesuchten Raumes enthaelt und liefert ein Response-Objekt mit einem Byte-Array, welches das Foto des Raumes enthaelt,
     * an den Aufrufer zurück.
     *
     * @return Response-Objekt mit Byte-Array, das das gesuchte Raumfoto enthaelt
     */

    @GET
    @Produces("image/png")
    @Path(value="/{param}/foto")
    public Response getRaumfoto(@PathParam(value="param") String id) {
        File foto = new File("fotos/"+ id +".jpg");
        try {
           BufferedImage image = ImageIO.read(foto);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            byte[] imageData = baos.toByteArray();
            System.out.println(Server.BASE_URI);


          return Response.ok(imageData, "image/png").build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Antwort.INTERNAL_SERVER_ERROR;
    }


}
