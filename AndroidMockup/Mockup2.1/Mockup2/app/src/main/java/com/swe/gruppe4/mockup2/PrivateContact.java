package com.swe.gruppe4.mockup2;

/**
 * Created by Kiesa on 28.10.2016.
 */

public class PrivateContact {


    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    private String nickName;
    private int profilePicture;
    private String friendRoom;





    /**
     *  This constructor creates a new friend
     *  @param nickName  the name of the friend
     */
    public PrivateContact(String nickName , int profilePicture , String friendRoom ){
        super();
        this.nickName = nickName;
        this.friendRoom = friendRoom;
        this.profilePicture = profilePicture;
    }

    public int getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(int profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getFriendRoom() {
        return friendRoom;
    }

    public void setFriendRoom(String friendRoom) {
        this.friendRoom = friendRoom;
    }
}
