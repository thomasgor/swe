package com.swe.gruppe4.mockup2;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;

import static com.swe.gruppe4.mockup2.R.id.toolbar;

public class RoomActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_room, null, false);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer.addView(contentView, 0);
    }
}
