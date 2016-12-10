package com.swe.gruppe4.freespace;

/**
 * Created by Merlin on 05.12.2016.
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

import com.swe.gruppe4.freespace.Objektklassen.Benutzer;

import java.io.InputStream;
import java.util.ArrayList;


public class RaumdetailsLeuteAdapter extends ArrayAdapter<Benutzer> {

    private ArrayList<Benutzer> benutzerListe = new ArrayList<Benutzer>();
    public void add(Benutzer user) {
        super.add(user);
        benutzerListe.add(user);

    }



    public Benutzer getItem(int index) {
        return this.benutzerListe.get(index);
    }

    public RaumdetailsLeuteAdapter(Context context , int textViewResourceId){
        super(context,textViewResourceId);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Benutzer benutzer = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.friends_box, parent, false);
        }
        TextView nickName = (TextView) convertView.findViewById(R.id.friendsName);
        nickName.setText(benutzer.getVorname() + " " + benutzer.getName());
        TextView roomName = (TextView) convertView.findViewById(R.id.friend_room);
        roomName.setText("");
        ImageView profilePicture = (ImageView) convertView.findViewById(R.id.profileImg);
        new LoadProfilePicture(profilePicture).execute(benutzer.getFotoURL());

        /*ImageView deleteFriend = (ImageView) convertView.findViewById(R.id.friendDelete);
        deleteFriend.setImageResource(android.R.drawable.ic_menu_delete);*/

        return convertView;
    }

    private class LoadProfilePicture extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        LoadProfilePicture(ImageView bmImage) {
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
