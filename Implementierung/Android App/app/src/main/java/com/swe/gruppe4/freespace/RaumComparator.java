package com.swe.gruppe4.freespace;

import com.swe.gruppe4.freespace.Objektklassen.Raum;

import java.util.Comparator;

/**
 * Created by Marco on 10.12.2016.
 */

public class RaumComparator implements Comparator<Raum> {

    @Override
    public int compare(Raum raum, Raum raum2) {
        return raum.getRaumname().compareTo(raum2.getRaumname());
    }
}
