package com.fhaachen.swe.freespace.main;

import com.fhaachen.swe.freespace.JsonHelper;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by thomas on 27.11.2016.
 */
@Table("Tag")
public class Tag extends Datenbank {

    public static String getTag(){
        String json = null;
        connect();
        LazyList<Tag> tags = Tag.findAll();
        if(tags != null){
            json = tags.toJson(true);
        }
        disconnect();
        return json;
    }

    public static String getTagById(String TagId){
        String json = null;
        connect();
        Tag tag = Tag.findById(TagId);

        if(tag != null){
            json = tag.toJson(true);
        }
        disconnect();
        return json;
    }

    public static String deleteTag(String tagID) {
        connect();
        String antwort = null;
        //prüfen ob Tag existiert
        try {
            Tag tag = Tag.findById(tagID);
            if (tag == null) {
                return null;
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
            return null;
        }
        disconnect();
        return antwort;
    }

    public static String postTag(String json) {
        connect();
        String antwort = null;
        String tagName = JsonHelper.getAttribute(json, "name");
        //prüfen ob Tag existiert
        try {
            if (Tag.findFirst("name = ?", tagName) != null) {
                return null;
            }
            antwort = Tag.createIt("name", tagName).toJson(true);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
        disconnect();
        return antwort;
    }
}
