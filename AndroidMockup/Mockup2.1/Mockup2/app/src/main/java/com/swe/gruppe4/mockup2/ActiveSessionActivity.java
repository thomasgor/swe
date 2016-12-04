package com.swe.gruppe4.mockup2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.InputStream;

public class ActiveSessionActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_active_session, null, false);
        drawer.addView(contentView, 0);

        ImageView imgRoom = (ImageView) findViewById(R.id.img_room_photo);
        new ActiveSessionActivity.LoadRoomImage(imgRoom).execute("http://i.imgur.com/LyzIuVj.jpg");

        ListView listPeopleInRoomView = (ListView) findViewById(R.id.list_people_in_room);
        FriendListAdapter friendsAdapter = new FriendListAdapter(getApplicationContext(), R.layout.friends_box);
        listPeopleInRoomView.setAdapter(friendsAdapter);
        listPeopleInRoomView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        for(int i =1; i<10;i++){
            friendsAdapter.add(new PrivateContact("Student"+i,R.drawable.profilepicture,"G10"+i));
        }
        friendsAdapter.notifyDataSetChanged();



        /*ListView roomView = (ListView) findViewById(R.id.current);
        RoomAdapter roomAdapter = new RoomAdapter(getApplicationContext(), R.layout.current_room_box);
        roomView.setAdapter(roomAdapter);
        roomView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        for (int i = 0; i < 1; i++) {
            roomAdapter.add(new Room("G10" + i, R.drawable.circle_green, "Aktiv bis 12:00 Uhr\n#präsentation", true));
        }
        roomAdapter.notifyDataSetChanged();*/


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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

