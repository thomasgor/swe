package com.swe.gruppe4.mockup2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Merlin on 29.10.2016.
 */

public class TagAdapter extends ArrayAdapter<Tag> {
    private ArrayList<Tag> tagArrayList = new ArrayList<>();

    public void add(Tag tag) {
        super.add(tag);
        tagArrayList.add(tag);

    }

    public Tag getItem(int index) {
        return this.tagArrayList.get(index);
    }

    public TagAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Tag tagObj = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tag_box, parent, false);
        } else{
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tag_box, parent, false);
        }

        CheckBox tag = (CheckBox) convertView.findViewById(R.id.tagCheckBox);

        assert tagObj != null;
        tag.setText(tagObj.tagName);
        tag.setChecked(tagObj.isChecked);
        return convertView;
    }
}
