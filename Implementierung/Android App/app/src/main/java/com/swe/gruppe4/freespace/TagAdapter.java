package com.swe.gruppe4.freespace;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Dient zum erstellen der Tagliste. Hier werden die Tagobjekt in die Listview
 * eingefügt
 *
 * @author Marco Linnartz
 * @version 1.0
 */

public class TagAdapter extends ArrayAdapter<com.swe.gruppe4.freespace.Objektklassen.Tag> {
    private ArrayList<com.swe.gruppe4.freespace.Objektklassen.Tag> tagArrayList = new ArrayList<>();
    private ArrayList<String> checkedTags = new ArrayList<>();

    /**
     * Fügt Tags in Liste für Anzeige hinzu
     * @param tag
     */
    public void add(com.swe.gruppe4.freespace.Objektklassen.Tag tag) {
        super.add(tag);
        tagArrayList.add(tag);
    }

    /**
     * Liefert den Tag am entsprechenden Index
     * @param index
     * @return
     */
    public com.swe.gruppe4.freespace.Objektklassen.Tag getItem(int index) {
        return this.tagArrayList.get(index);
    }


    public TagAdapter(Context context, int resource) {
        super(context, resource);
    }

    /**
     * Liefert eine Liste der Tags, die gecheckt sind
     * @return
     */
    public ArrayList<String> getCheckedTags() {
        return checkedTags;
    }


    /**
     * Fügt die einzelnen Tags in die ListView ein.
     * Fägt einen Listener auf die Checkboxen ein. Wenn ein Eintrag ausgewählt wird,
     * wird diese in die Liste der gecheckten Tags hinzugefügt. Wenn der Eintrag abgewählt wird,
     * wird der Eintrage aus der Liste der gecheckten Tags entfernt
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        com.swe.gruppe4.freespace.Objektklassen.Tag tagObj = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tag_box_check, parent, false);
        } else{
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tag_box_check, parent, false);
        }

        final CheckBox tagCheckBox = (CheckBox) convertView.findViewById(R.id.tagCheckBox2);
        assert tagObj != null;
        tagCheckBox.setText(tagObj.getName());
        HashSet<String> currSelectedFilter;
        SharedPreferences sharedPref = getContext().getSharedPreferences("com.swe.gruppe4.freespace.roomfilter", Context.MODE_PRIVATE);
        if(sharedPref.contains("filterTags")) {
            currSelectedFilter = (HashSet<String>) sharedPref.getStringSet("filterTags", new HashSet<String>());
        } else {
            currSelectedFilter = new HashSet<>();
        }

        tagCheckBox.setChecked(currSelectedFilter.contains(tagObj.getName()));
        if(currSelectedFilter.contains(tagObj.getName())) {
            checkedTags.add(tagObj.getName());
        }

        tagCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    checkedTags.add(tagCheckBox.getText().toString());
                }else{
                    checkedTags.remove(tagCheckBox.getText().toString());
                }
            }
        });

        return convertView;
    }
}
