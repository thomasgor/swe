package com.swe.gruppe4.mockup2;

/**
 * Created by Kiesa on 28.10.2016.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;
import com.swe.gruppe4.mockup2.Objektklassen.*;

/**
 * Created by Kiesa on 30.04.2016.
 * this is a custom adapter that contains/ manages PrivateContacts (friends)
 */
class FriendListAdapter extends ArrayAdapter<Freundschaft> {

    private ArrayList<Freundschaft> friendInfoList = new ArrayList<Freundschaft>();
    /**
     *  add PrivateContact object  to the adapter
     */
    public void add(Freundschaft friend) {
        super.add(friend);
        friendInfoList.add(friend);

    }



    public Freundschaft getItem(int index) {
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
        Freundschaft friendObj = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.friends_box, parent, false);
        }
        TextView nickName = (TextView) convertView.findViewById(R.id.friendsName);
        nickName.setText(friendObj.getBenutzer().getVorname() + " " + friendObj.getBenutzer().getName());
        TextView roomName = (TextView) convertView.findViewById(R.id.friend_room);
        roomName.setText(friendObj.getRaum().getRaumname());
        ImageView profilePicture = (ImageView) convertView.findViewById(R.id.profileImg);
        new FriendListAdapter.LoadRoomImage(profilePicture).execute(friendObj.getBenutzer().getFotoURL());
        ImageView deleteFriend = (ImageView) convertView.findViewById(R.id.friendDelete);
        deleteFriend.setImageResource(android.R.drawable.ic_menu_delete);
        //deleteFriend.setOnClickListener();
        return convertView;
    }

    private class LoadRoomImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        LoadRoomImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
