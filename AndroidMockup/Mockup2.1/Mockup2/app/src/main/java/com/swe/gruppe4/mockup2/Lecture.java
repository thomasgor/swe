package com.swe.gruppe4.mockup2;

/**
 * Created by Kiesa on 29.10.2016.
 */

public class Lecture {

    private String lectureName;
    private String lectureDate;
    private String lectureRoom;

    public Lecture(String name , String date , String room){
        super();
        lectureDate = date;
        lectureName = name;
        lectureRoom = room;
    }

    public String getLectureRoom() {
        return lectureRoom;
    }

    public void setLectureRoom(String lectureRoom) {
        this.lectureRoom = lectureRoom;
    }

    public String getLectureDate() {
        return lectureDate;
    }

    public void setLectureDate(String lectureDate) {
        this.lectureDate = lectureDate;
    }

    public String getLectureName() {
        return lectureName;
    }

    public void setLectureName(String lectureName) {
        this.lectureName = lectureName;
    }
}
