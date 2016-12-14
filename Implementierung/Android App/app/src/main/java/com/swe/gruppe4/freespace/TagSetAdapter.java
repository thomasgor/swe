package com.swe.gruppe4.freespace;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.swe.gruppe4.freespace.Objektklassen.Tag;

import java.util.ArrayList;

/**
 * Created by Merlin on 11.12.2016.
 */

public class TagSetAdapter extends ArrayAdapter<Tag> {
    private ArrayList<Tag> tagArrayList = new ArrayList<>();
    private ArrayList<String> checkedTags = new ArrayList<>();

    private static int selected = -1;

    public void add(Tag tag) {
        super.add(tag);
        tagArrayList.add(tag);
    }

    public com.swe.gruppe4.freespace.Objektklassen.Tag getItem(int index) {
        return this.tagArrayList.get(index);
    }

    public TagSetAdapter(Context context, int resource) {
        super(context, resource);
    }

    public ArrayList<String> getCheckedTag() {
        return checkedTags;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Tag tagObj = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tag_box, parent, false);
        } else{
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tag_box, parent, false);
        }



        RadioButton tagRadioButton = (RadioButton) convertView.findViewById(R.id.tagCheckBox);
        //final CheckBox tagCheckBox = (CheckBox) convertView.findViewById(R.id.tagCheckBox2);
        assert tagObj != null;
        tagRadioButton.setText(tagObj.getName());
        tagRadioButton.setChecked(false);

        return convertView;
    }
}