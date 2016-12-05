package com.swe.gruppe4.mockup2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.swe.gruppe4.mockup2.Objektklassen.Benutzer;
import com.swe.gruppe4.mockup2.Objektklassen.Raum;
import com.swe.gruppe4.mockup2.Objektklassen.Sitzung;

import java.io.InputStream;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ActiveSessionActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ImageView imgRoom;
    Sitzung data;
    Raum raum;
    TextView raumName;
    TextView tag;
    Button setTag;
    ListView listPeopleInRoomView;
    RaumdetailsLeuteAdapter raumLeuteAdapter;
    TextView activeUntil;
    Date untilTime;
    TextView leute;
    Button erneuern;
    Button beenden;

    int finalHeight;
    int finalWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_active_session, null, false);
        drawer.addView(contentView, 0);

        imgRoom = (ImageView) findViewById(R.id.img_room_photo);

        //Daten holen
        data = (Sitzung) getIntent().getSerializableExtra("sitzung");
        raum = data.getRaum();

        setData();

        erneuern = (Button) findViewById(R.id.btn_session_renew);
        erneuern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data = new Verbindung().sitzungPut(data.getId());
                setData();
            }
        });

        beenden = (Button) findViewById(R.id.btn_session_quit);
        beenden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Verbindung().sitzungDelete(data.getId());
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void setData(){
        raumName = (TextView) findViewById(R.id.txt_room_number);
        raumName.setText(raum.getRaumname());

        tag = (TextView) findViewById(R.id.txt_tag);
        tag.setText(raum.getTag().getName());

        ViewTreeObserver viewTree = imgRoom.getViewTreeObserver();
        viewTree.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                finalHeight = imgRoom.getMeasuredHeight();
                finalWidth = imgRoom.getMeasuredWidth();
                //print or do some code
                new ActiveSessionActivity.LoadRoomImage(imgRoom, finalWidth,finalHeight).execute(data.getRaum().getFotoURL());
                return true;
            }
        });



        setTag = (Button) findViewById(R.id.btn_set_tag);
        setTag.setEnabled(data.isMyTag());

        listPeopleInRoomView = (ListView) findViewById(R.id.list_people_in_room);
        raumLeuteAdapter = new RaumdetailsLeuteAdapter(getApplicationContext(), R.layout.friends_box);
        listPeopleInRoomView.setAdapter(raumLeuteAdapter);
        listPeopleInRoomView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        int nichtAnonym = 0;
        for(Benutzer ben : raum.getBenutzer()){
            nichtAnonym++;
            raumLeuteAdapter.add(ben);
        }
        raumLeuteAdapter.notifyDataSetChanged();

        activeUntil = (TextView) findViewById(R.id.txt_session_active_until);
        untilTime = new Date(data.getEndzeit()*1000);
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(untilTime);
        activeUntil.setText(getString(R.string.session_active_until_clock, DateFormat.getTimeInstance(DateFormat.SHORT).format(untilTime)));

        leute = (TextView) findViewById(R.id.txt_people_in_room_cnt);
        int max = raum.getTeilnehmer_max();
        int crnt = raum.getTeilnehmer_aktuell();
        leute.setText(getString(R.string.people_in_room_cnt,crnt,max,crnt-nichtAnonym));
    }

    private class LoadRoomImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        int width;
        int height;

        LoadRoomImage(ImageView bmImage, int width, int height) {
            this.bmImage = bmImage;
            this.width = width;
            this.height=height;
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
            result = ImageHelper.scaleCenterCrop(result,height,width);
            bmImage.setImageBitmap(ImageHelper.getRoundedCornerBitmap(result,10));
            /*Bitmap bmp = ((BitmapDrawable)bmImage.getDrawable()).getBitmap();
            bmImage.setImageBitmap(ImageHelper.getRoundedCornerBitmap(bmp,100));*/
        }

    }
}

