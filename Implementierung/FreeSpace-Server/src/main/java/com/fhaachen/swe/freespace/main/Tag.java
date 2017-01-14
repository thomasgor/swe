package com.fhaachen.swe.freespace.main;

import com.fhaachen.swe.freespace.Antwort;
import com.fhaachen.swe.freespace.JsonHelper;
import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.annotations.Table;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Klasse Tag beinhaltet die Logik um die HTTP-Methoden GET, POST, DELETE des Restservices umzusetzen.
 * Die Logik wurde anhand des Systementwurfs umgesetzt.
 *
 * @author Simon Catley
 * @version 1.2
 */
@Table("Tag")
public class Tag extends Datenbank {

    /**
     * Erzeut eine Liste aller auf dem Server Befindlichen Tags und wandelt diese in ein String-Objekt im
     * Json-Format um. Sollten keine Tags vorhanden sein, wird eine leere Liste umgewandelt.
     * Liefert bei Erfolg ein Response-Objekt mit HTTP-Statuscode OK zurück und dem Strin-Objekt. Anderenfalls
     * ein Response-Objekt mit dem Fehlercode Internal Server Error als HTTP-Statucode und ohne String-Objekt.
     *
     * @return Liste aller Tags als String im Json-Format
     */

    public static Response  getTagListe(){
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

    /**
     * Liefert eine Liste aller Tags der Datenbank als LayzyList(activeJDBC). Falls nicht auf die Datenbank zugegriffen
     * werden kann, oder keine Tags auf der Datenbank vorhanden sind, wird null zurückgegeben.
     *
     * @return Liste aller Tags auf der Datenbank, oder null
     */

    public static LazyList getTagList() {
        connect();
        LazyList<Tag> tags = null;
        try {
            tags = Tag.findAll();
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
        tags.load();
        disconnect();
        return tags;
    }

    /**
     * Sucht einen Tag auf der Datenbank mit der ID tagID und gibt diesen als String im Json-Format zurück. Falls kein Tag
     * mit der tagID existiert, oder nicht auf die Datenbank zugegriffen werden kann, wird null zurückgegeben.
     *
     * @param tagID ID des gesuchten Tags
     * @return Den Tag-Eintrag aus der Datenbank als String im Json-Format, oder null
     */

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

    /**
     * Löscht den Tag-Eintrag mit der ID tagID aus der Datenbank. Falls kein Tag mit der ID tagID existiert, wird ein
     * Response-Objekt mit HTTP-Statuscode Bad Request zurückgegeben, falls nicht auf den Server zugegriffen werden kann,
     * ein Response-Objekt mit HTTP-Statuscode Internal Server Error und bei Erfolg ein Response-Objekt mit
     * HTTP-Statuscode OK und dem gelöschten Tag-Eintrag als String im Json-Format.
     *
     * @param tagID ID des zu löschenden Tags
     * @return Response-Objekt mit HTTP-Statuscode und bei Erfolg mit dem gelöschten Element als String im Json-Format
     */

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

                Sitzung s = Sitzung.first("raum = ? AND hasTag = 1",element.get("id"));

                if(s!=null){
                    s.set("hasTag",0).saveIt();
                }

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

    /**
     * Erstellt einen neuen Tag-Eintrag mit dem Namen, der aus dem String json entnommen wird. Dieser ist im Json-Format.
     * Falls kein, ein leerer, oder ein bereits in der Datenbank vorhandener Name übermittelt wurde, wird ein
     * Response-Objekt mit HTTP-Statuscode Bad Request zurückgegeben.
     * Falls nicht auf den Server zugegriffen werden kann, ein Response-Objekt mit HTTP-Statuscode Internal Server Error
     * und bei Erfolg ein Response-Objekt mit HTTP-Statuscode OK und dem neu erstellten Tag-Eintrag als String im Json-Format.
     *
     * @param json String im Json-Format, der den Namen des zu erstellenden Tags enthält
     * @return Response-Objekt mit HTTP-Statuscode und bei Erfolg mit dem neu erstellten Tag als String im Json-Format
     */

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

    /**
     * Löscht die Tags
     * @param ids Enthält die Liste der zu löschenden Tags
     * @return true, wenn das Löschen erfolgreich war
     */

    public static boolean deleteTags(List<String> ids){
        try {

            if(ids != null) {
                connect();

                for (String i : ids) {
                    System.out.println("Lösche Tag:" + i);
                    Tag t = Tag.findById(Integer.parseInt(i));
                    LazyList<Raum> raum = Raum.find("tag = ?", Integer.parseInt(t.get("id").toString()));

                    for (Raum element : raum) {
                        System.out.println("Raume bei Tag " + i);
                        element.set("tag", null).saveIt();

                        Sitzung s = Sitzung.first("raum = ? AND hasTag = 1",element.get("id"));

                        if(s!=null){
                            s.set("hasTag",0).saveIt();
                        }
                    }

                    System.out.println("Lösche endgültig Tag:" + i);
                    t.delete();
                }
                disconnect();
            }

        }catch(Exception e){
            e.printStackTrace();
            disconnect();
            return false;
        }
        return true;
    }

    /**
     * Fügt einen neuen Tag hinzu
     * @param tagname Der Name des Tag, welcher neu hinzugefügt werden soll
     * @return true, wenn das Anlegen erfolgreich war
     */

    public static boolean addNeuerTag(String tagname) {
        connect();
        //prüfen ob Tag existiert
        try {
            if (tagname == null || tagname == "" || Tag.findFirst("name = ?", tagname) != null) {
                return false;
            }
            Tag.createIt("name", tagname);
        } catch(Exception e) {
            e.printStackTrace();
            disconnect();
            return false;
        }
        disconnect();
        return true;
    }

}
