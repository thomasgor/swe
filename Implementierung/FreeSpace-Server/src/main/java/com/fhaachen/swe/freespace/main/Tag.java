package com.fhaachen.swe.freespace.main;

import com.fhaachen.swe.freespace.Antwort;
import com.fhaachen.swe.freespace.JsonHelper;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by thomas on 27.11.2016.
 */
@Table("Tag")
public class Tag extends Datenbank {

    public static Response getTagListe(){
        String antwort = "[]";
        connect();
        try {
            LazyList<Tag> tags = Tag.findAll();
            if (tags != null) {
                antwort = tags.toJson(true);
            }
        } catch(Exception e){
            e.printStackTrace();
            return Antwort.INTERNAL_SERVER_ERROR;
        }
        disconnect();
        return Response.ok(antwort, MediaType.APPLICATION_JSON).build();
    }

    public static String getTagById(String tagID) {
        String antwort = null;
        connect();
        try{
            Tag tag = Tag.findById(tagID);
            if(tag != null){
                antwort = tag.toJson(true);
            } else {
                return null;
            }
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
        disconnect();
        return antwort;
    }

    public static Response deleteTag(String tagID) {
        connect();
        String antwort = null;
        //prüfen ob Tag existiert
        try {
            Tag tag = Tag.findById(Integer.parseInt(tagID));
            if (tag == null) {
                return Antwort.BAD_REQUEST;
            }
            //Foreign Keys entfernen
            LazyList<Raum> raum = Raum.find("tag = ?", Integer.parseInt(tagID));
            for (Raum element: raum) {
                element.set("tag", null).saveIt();
            }
            tag.deleteCascade();
            antwort = tag.toJson(true);
        } catch(Exception e) {
            e.printStackTrace();
            return Antwort.INTERNAL_SERVER_ERROR;
        }
        disconnect();
        return Response.ok(antwort, MediaType.APPLICATION_JSON).build();
    }

    public static Response postTag(String json) {
        connect();
        String antwort = null;
        String tagName = JsonHelper.getAttribute(json, "name");
        //prüfen ob Tag existiert
        try {
            if (tagName == null || tagName == "" || Tag.findFirst("name = ?", tagName) != null) {
                return Antwort.BAD_REQUEST;
            }
            antwort = Tag.createIt("name", tagName).toJson(true);
        } catch(Exception e) {
            e.printStackTrace();
            return Antwort.INTERNAL_SERVER_ERROR;
        }
        disconnect();
        return Response.ok(antwort,MediaType.APPLICATION_JSON).build();
    }
}
