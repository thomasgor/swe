package com.fhaachen.swe.freespace.main;

import org.javalite.activejdbc.annotations.Table;

import java.util.List;

/**
 * Created by thomas on 27.11.2016.
 * Edited by simon on 04.12.2016.
 */
@Table("Tag")
public class Tag extends Datenbank {

    public static String getTagListe(){
        connect();
        String json = Tag.findAll().toJson(true);
        disconnect();
        return json;
    }

    public static String postTag(String tagName) {
        connect();
        String json = Tag.createIt("name", tagName).toJson(true);
        disconnect();
        return json;
    }

    public static String deleteTag(int tagID) {
        connect();
        Tag tag = Tag.findFirst("id = ?", tagID);
        tag.deleteCascade();
        String json = tag.toJson(true);
        disconnect();
        return json;
    }
}
