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

import com.koushikdutta.ion.Ion;
import com.makeramen.roundedimageview.RoundedImageView;
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
        final Benutzer benutzer = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.friends_box, parent, false);
        }
        TextView nickName = (TextView) convertView.findViewById(R.id.friendsName);
        String name = benutzer.getVorname();
        if(benutzer.getName()!=null) {
            name = name + " " + benutzer.getName();
        }
        nickName.setText(name);
        TextView roomName = (TextView) convertView.findViewById(R.id.friend_room);
        roomName.setText("");
        final RoundedImageView profilePicture = (RoundedImageView) convertView.findViewById(R.id.profileImg);

        Ion.with(this.getContext())
                .load(benutzer.getFotoURL())
                .withBitmap()
                .placeholder(R.drawable.ic_hourglass_empty_black_24dp)
                .error(R.drawable.nopp)
                .animateIn(android.R.anim.fade_in)
                .intoImageView(profilePicture);

        return convertView;
    }

}
