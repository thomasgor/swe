package com.fhaachen.swe.freespace.ressources;

import com.fhaachen.swe.freespace.Antwort;
import com.fhaachen.swe.freespace.JsonHelper;
import com.fhaachen.swe.freespace.Server;
import com.fhaachen.swe.freespace.main.Raum;
import com.fhaachen.swe.freespace.main.Sitzung;
import com.fhaachen.swe.freespace.main.Tag;

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
import java.util.Map;

/**
 * Created by thomas on 27.11.2016.
 */

@Path( value = "/raum")
@RolesAllowed({"user", "professor"})
public class RaumREST {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRaum(){
        //TODO: NULL überprüfen
        String json = Raum.getRaum();
        if(json == null){
            return Antwort.BAD_REQUEST;
        }

        return Response.ok(json,MediaType.APPLICATION_JSON).build();
        //return Response.status(Response.Status.NOT_IMPLEMENTED).entity("Hier ensteht die Raumliste").build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value="/{param}")
    public Response getRaumdetails(@PathParam(value="param") String id){

        //TODO: NULL überprüfen
        String json = Raum.getRaumdetails(id);
        if(json == null) return Antwort.BAD_REQUEST;

        return Response.ok(Raum.getRaumdetails(id), MediaType.APPLICATION_JSON).build();
        //return Response.status(Response.Status.NOT_IMPLEMENTED).entity("Raumdetails von " + id).build();
    }


    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value="/{param}")
    public Response putRaumdetails(@PathParam(value="param") String id, String json, @Context SecurityContext context) {
        String tag = JsonHelper.getAttribute(json,"tag");
        String username = context.getUserPrincipal().getName(); // BenutzerID
        boolean isAllowed = Sitzung.istTagBesitzer(username,id);

        // User hat keine Berechtigung tag zu setzen
        if(!isAllowed) {
            //Überliefert dennoch das Raumobjekt
            System.out.println("User hat keine Berechtigung: aber eigentlicher raum wird zurückgegeben");
            String s = Raum.getRaumdetails(id);
            return Response.status(Response.Status.FORBIDDEN).entity(s).build();
        }

        String answer = Raum.putRaumID(id,tag);

        //Es existiert kein Raum mit der ID
        if(answer == null){
            return Antwort.INTERNAL_SERVER_ERROR;
        }

        return Response.ok(answer, MediaType.APPLICATION_JSON).build();
    }


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
