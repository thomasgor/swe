package com.swe.gruppe4.freespace;

import com.swe.gruppe4.freespace.Objektklassen.Raum;

import java.util.Comparator;

/**
 * Definiert die Ordnung zwischen den Raumobjekten. Die Räume sollen alphanumerisch geordnet werden.
 * Daher wird alphanumerisch nach Raumname sortiert.
 *
 * @author Marco Linnartz
 * @version 1.0
 */

public class RaumComparator implements Comparator<Raum> {

    @Override
    public int compare(Raum raum, Raum raum2) {
        return raum.getRaumname().compareTo(raum2.getRaumname());
    }
}
