package com.swe.gruppe4.freespace;

/**
 * Created by Kiesa on 28.10.2016.
 */

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import com.swe.gruppe4.freespace.Objektklassen.*;

/**
 * Created by Kiesa on 30.04.2016.
 * Adapterklasse un Freundschaftsanfragen anzuzeigen und zu verwalten
 */
class FriendRequestAdapter extends ArrayAdapter<Freundschaft> {

    private ArrayList<Freundschaft> friendInfoList = new ArrayList<Freundschaft>();
    Context con;
    /**
     *  Freundschaft zur Liste hinzufügen
     *  @param friend
     */
    public void add(Freundschaft friend) {
        super.add(friend);
        friendInfoList.add(friend);

    }


    /**
     *  Einzelne Freundschaft auslesen
     *  @Param index
     *  @Return Freundschaft
     */
    public Freundschaft getItem(int index) {
        return this.friendInfoList.get(index);
    }

    public FriendRequestAdapter(Context context , int textViewResourceId){
        super(context,textViewResourceId);
        con = context;
    }

    /**
     *  this method is used to populate the friendslist in the FriendsListActivity
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Freundschaft friendObj = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.friends_request_box, parent, false);
        }
        TextView nickName = (TextView) convertView.findViewById(R.id.friendsName);
        nickName.setText(friendObj.getBenutzer().getVorname() + " " + friendObj.getBenutzer().getName());

        ImageView acceptFriend = (ImageView) convertView.findViewById(R.id.acceptRequest);
        acceptFriend.setImageResource(android.R.drawable.ic_input_add);
        acceptFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new RestConnection(getContext()).freundschaftPut(friendObj.getBenutzer());
                ((Freundesliste)con).recreate();


            }
        });

        ImageView declineFriend = (ImageView) convertView.findViewById(R.id.declineRequest);
        declineFriend.setImageResource(android.R.drawable.ic_delete);
        declineFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new RestConnection(getContext()).freundschaftDelete(friendObj.getBenutzer());
                ((Freundesliste)con).recreate();

            }
        });
        return convertView;
    }

    /**
     *  Überprüfung, ob Freundschaftsanfragen vorhanden sind.
     *  @Return boolean
     */
    public boolean isEmpty(){
        return friendInfoList.isEmpty();
    }




}
