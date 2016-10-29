package com.swe.gruppe4.mockup2;

/**
 * Created by Kiesa on 28.10.2016.
 */

public class Room {


    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomInfo) {
        this.roomName = roomName;
    }
    private String roomName;
    private String roomInfo;
    private int status;
    public boolean flag = false;



    /**
     *  This constructor creates a new Room
     *  @param roomName  the name of the Room
     */
    public Room(String roomName , int status , String roomInfo , boolean flag){
        super();
        this.roomName = roomName;
        this.status = status;
        this.roomInfo = roomInfo;
        this.flag = flag;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRoomInfo() {
        return roomInfo;
    }

    public void setRoomInfo(String roomInfo) {
        this.roomInfo = roomInfo;
    }
}
