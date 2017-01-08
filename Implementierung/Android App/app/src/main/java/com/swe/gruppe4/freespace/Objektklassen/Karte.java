package com.swe.gruppe4.freespace.Objektklassen;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * <p>Überschrift: Struktur von Karte </p>
 * <p>Beschreibung:
 * </p>
 * <p>Copyright: Merlin Copyright (c) 2016</p>
 * <p>Organisation: FH Aachen, FB05 </p>
 *
 * @author Merlin
 * @version 1
 *          Created on 22.11.2016.
 */
public class Karte implements Serializable {
    private static ArrayList<RoomEnterance> rooms;

    public Karte(){
        rooms = new ArrayList<RoomEnterance>();
    }

    public ArrayList<RoomEnterance> getRooms() {
        return rooms;
    }

    public void addRoomEnterance(int id, String name, float x,float y){
        rooms.add(new RoomEnterance(id,name,x,y));
    }

    public void setRooms(ArrayList<RoomEnterance> rooms) {
        this.rooms = rooms;
    }

}
