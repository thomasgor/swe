package com.swe.gruppe4.mockup2;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.widget.AbsListView;
import android.widget.ListView;

public class ActiveSessionActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_active_session, null, false);
        drawer.addView(contentView, 0);

        ListView roomView = (ListView) findViewById(R.id.current);
        RoomAdapter roomAdapter = new RoomAdapter(getApplicationContext(), R.layout.current_room_box);
        roomView.setAdapter(roomAdapter);
        roomView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        for (int i = 0; i < 1; i++) {
            roomAdapter.add(new Room("G10" + i, R.drawable.circle_green, "Aktiv bis 12:00 Uhr\n#präsentation", true));
        }
        roomAdapter.notifyDataSetChanged();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
}