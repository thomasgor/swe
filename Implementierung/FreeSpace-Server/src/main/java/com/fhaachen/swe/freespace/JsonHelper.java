package com.fhaachen.swe.freespace;

import org.codehaus.jackson.map.ObjectMapper;
import java.io.IOException;
import java.util.Map;

/**
 * Created by pwueller on 06.12.2016.
 */

public class JsonHelper {

    public static Map toMap(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, Map.class);
        } catch (IOException e) { throw new RuntimeException(e); }
    }
    public static Map[] toMaps(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, Map[].class);
        } catch (IOException e) { throw new RuntimeException(e); } }


    public static String getAttribute(String json, String attibut){
        Map tmp = toMap(json);
        return String.valueOf(tmp.get(attibut));
    }

    public static Object getAttribute(String json, String attibut1, String attibut2){
        Map tmp = JsonHelper.toMap(json);

        String tag = String.valueOf(tmp.get(attibut1));

        Map m = (Map)tmp.get(attibut1);
        String name = String.valueOf(m.get(attibut2));
        return name;
    }

    //Rückgabe:{"foto":"http://fotourl","id":1,"raumnummer":"G101","tag":{"id":1,"name":"Ruhe"},"teilnehmer_max":32}
    public static String getJsonStringFromMap(Map m){
        try {
            ObjectMapper mapper = new ObjectMapper();
            //   string a = mapper.writeValue();
            return mapper.writeValueAsString(m);

        }catch(Exception e){
            return null;
        }
       // return "";
    }

}
