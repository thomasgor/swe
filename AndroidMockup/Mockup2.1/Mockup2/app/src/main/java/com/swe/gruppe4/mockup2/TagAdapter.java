package com.swe.gruppe4.mockup2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.swe.gruppe4.mockup2.Objektklassen.*;

import java.util.ArrayList;

/**
 * Created by Merlin on 29.10.2016.
 */

public class TagAdapter extends ArrayAdapter<com.swe.gruppe4.mockup2.Objektklassen.Tag> {
    private ArrayList<com.swe.gruppe4.mockup2.Objektklassen.Tag> tagArrayList = new ArrayList<>();
    private ArrayList<String> checkedTags = new ArrayList<>();

    public void add(com.swe.gruppe4.mockup2.Objektklassen.Tag tag) {
        super.add(tag);
        tagArrayList.add(tag);
    }

    public com.swe.gruppe4.mockup2.Objektklassen.Tag getItem(int index) {
        return this.tagArrayList.get(index);
    }

    public TagAdapter(Context context, int resource) {
        super(context, resource);
    }

    public ArrayList<String> getCheckedTags() {
        return checkedTags;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        com.swe.gruppe4.mockup2.Objektklassen.Tag tagObj = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tag_box_check, parent, false);
        } else{
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tag_box_check, parent, false);
        }

        //RadioButton tagRadioButton = (RadioButton) convertView.findViewById(R.id.tagCheckBox);
        final CheckBox tagCheckBox = (CheckBox) convertView.findViewById(R.id.tagCheckBox2);
        assert tagObj != null;
        tagCheckBox.setText(tagObj.getName());
        tagCheckBox.setChecked(false);
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
        //tagRadioButton.setText(tagObj.getName());
        //tagRadioButton.setChecked(false);
        return convertView;
    }
}
