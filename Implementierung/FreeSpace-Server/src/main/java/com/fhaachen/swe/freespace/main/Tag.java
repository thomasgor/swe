package com.fhaachen.swe.freespace.main;

import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

import java.util.List;

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
        Tag t = Tag.findById(TagId);

        if(t != null){
            json = t.toJson(true);
        }
        disconnect();
        return json;
    }

}
