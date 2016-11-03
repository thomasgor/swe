package com.swe.gruppe4.mockup2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.R.drawable.ic_menu_delete;

/**
 * Created by Kiesa on 29.10.2016.
 */

public class LectureAdapter extends ArrayAdapter<Lecture> {

    private ArrayList<Lecture> lectureList = new ArrayList<Lecture>();
    /**
     *  add Room object  to the adapter
     */
    public void add(Lecture lecture) {
        super.add(lecture);
        lectureList.add(lecture);

    }



    public Lecture getItem(int index) {
        return this.lectureList.get(index);
    }

    public LectureAdapter(Context context , int textViewResourceId){
        super(context,textViewResourceId);
    }

    /**
     *  this method is used to populate the Roomlist in the RoomActivity
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Lecture lectureObj = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.lecture_box, parent, false);
        }
        TextView lectureName = (TextView) convertView.findViewById(R.id.lecture_name);
        lectureName.setText(lectureObj.getLectureName());
        TextView lectureDate = (TextView) convertView.findViewById(R.id.lecture_date);
        lectureDate.setText(lectureObj.getLectureDate());
        TextView lectureRoom = (TextView) convertView.findViewById(R.id.lecture_room);
        lectureRoom.setText(lectureObj.getLectureRoom());
        ImageView editButton = (ImageView) convertView.findViewById(R.id.imageView2);
        ImageView deleteButton = (ImageView) convertView.findViewById(R.id.imageView3);
        editButton.setImageResource(android.R.drawable.ic_menu_edit);
        deleteButton.setImageResource(android.R.drawable.ic_menu_delete);
        return convertView;
    }

}
