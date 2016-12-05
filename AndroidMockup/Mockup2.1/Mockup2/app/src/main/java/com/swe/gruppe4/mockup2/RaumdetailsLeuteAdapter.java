package com.swe.gruppe4.mockup2;

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
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.swe.gruppe4.mockup2.Objektklassen.Benutzer;

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
        nickName.setText(benutzer.getVorname() + " " + benutzer.getName());
        TextView roomName = (TextView) convertView.findViewById(R.id.friend_room);
        roomName.setText("");
        final ImageView profilePicture = (ImageView) convertView.findViewById(R.id.profileImg);
        ViewTreeObserver viewTree = profilePicture.getViewTreeObserver();
        viewTree.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                int finalHeight = profilePicture.getMeasuredHeight();
                int finalWidth = profilePicture.getMeasuredWidth();
                //print or do some code
                new LoadProfilePicture(profilePicture, finalWidth,finalHeight).execute(benutzer.getFotoURL());
                return true;
            }
        });
        //new LoadProfilePicture(profilePicture).execute(benutzer.getFotoURL());

        /*ImageView deleteFriend = (ImageView) convertView.findViewById(R.id.friendDelete);
        deleteFriend.setImageResource(android.R.drawable.ic_menu_delete);*/

        return convertView;
    }

    private class LoadProfilePicture extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        int height, width;

        LoadProfilePicture(ImageView bmImage, int height, int width) {
            this.bmImage = bmImage;
            this.height=height;
            this.width=width;
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
            result = ImageHelper.scaleCenterCrop(result, height,width);
            bmImage.setImageBitmap(ImageHelper.getRoundedCornerBitmap(result,1000));
        }
    }

}
