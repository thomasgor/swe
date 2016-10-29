package com.swe.gruppe4.mockup2;

/**
 * Created by Kiesa on 28.10.2016.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by Kiesa on 30.04.2016.
 * this is a custom adapter that contains/ manages PrivateContacts (friends)
 */
class FriendListAdapter extends ArrayAdapter<PrivateContact> {

    private ArrayList<PrivateContact> friendInfoList = new ArrayList<PrivateContact>();
    /**
     *  add PrivateContact object  to the adapter
     */
    public void add(PrivateContact friend) {
        super.add(friend);
        friendInfoList.add(friend);

    }



    public PrivateContact getItem(int index) {
        return this.friendInfoList.get(index);
    }

    public FriendListAdapter(Context context , int textViewResourceId){
        super(context,textViewResourceId);
    }

    /**
     *  this method is used to populate the friendslist in the FriendsListActivity
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PrivateContact friendObj = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.friends_box, parent, false);
        }
        TextView nickName = (TextView) convertView.findViewById(R.id.friendsName);
        nickName.setText(friendObj.getNickName());
        TextView roomName = (TextView) convertView.findViewById(R.id.friend_room);
        roomName.setText(friendObj.getFriendRoom());
        ImageView profilePicture = (ImageView) convertView.findViewById(R.id.profileImg);
        profilePicture.setImageResource(friendObj.getProfilePicture());
        return convertView;
    }
}
