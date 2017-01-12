package com.swe.gruppe4.freespace;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.swe.gruppe4.freespace.Objektklassen.*;
import com.swe.gruppe4.freespace.Objektklassen.Tag;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.List;
import java.util.ArrayList;

import static com.swe.gruppe4.freespace.R.id.time;

public class AddLectureActivity extends BaseActivity implements View.OnClickListener{
    private EditText veranstaltungsNameEtxt;

    private EditText fromDateEtxt;
    private EditText fromTimeEtxt;
    private EditText toTimeEtxt;

    private DatePickerDialog fromDatePickerDialog;

    private TimePickerDialog fromTimePickerDialog;
    private TimePickerDialog toTimePickerDialog;

    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat timeFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lecture);

        //Spinner dropdown = (Spinner)findViewById(R.id.spinner);
        //String[] items = new String[]{"G101", "G102", "G103"};
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //dropdown.setAdapter(adapter);
        findViewsById();
        final RestConnection verb = new RestConnection(this);
        //final RestConnection verb = new RestConnection(this);


        final ArrayList<Raum> raumliste = verb.raumGet();
        veranstaltungsNameEtxt.setText("Veranstaltungsname");
        dateFormatter = new SimpleDateFormat("EEE dd.MM.yyyy", Locale.GERMAN);
        timeFormatter = new SimpleDateFormat("HH:mm", Locale.GERMAN);
        Date heute = new Date();
        fromDateEtxt.setText(dateFormatter.format(heute));
        fromTimeEtxt.setText(timeFormatter.format(heute));

        fromDateEtxt.setInputType(0);
        fromTimeEtxt.setInputType(0);
        toTimeEtxt.setInputType(0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Calendar cal = Calendar.getInstance(); // creates calendar
        cal.setTime(heute); // sets calendar time/date
        cal.add(Calendar.HOUR_OF_DAY, 1); // adds one hour
        Date heutePlusEins = cal.getTime();

        toTimeEtxt.setText(timeFormatter.format(heutePlusEins));

        List<String> spinnerArray =  new ArrayList<>();
        for(Raum raum: raumliste){
            spinnerArray.add(raum.getRaumname());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner sItems = (Spinner) findViewById(R.id.spinner);
        sItems.setAdapter(adapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setTitle("FreeSpace");


        Button saveLecture = (Button) findViewById(R.id.saveLectureButton);
        saveLecture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //EditText edittext = (TextView) convertView.findViewById(R.id.editText2);
                //String name = (String) textview.getText();
                //textview = (TextView) convertView.findViewById(R.id.DateEditText);
                //Date date = textview.getText();
                try {
                    SimpleDateFormat df = dateFormatter;
                    java.util.Date fromDate;

                    fromDate = df.parse(fromDateEtxt.getText().toString());
                    Long longFromDate = fromDate.getTime();

                    df = new SimpleDateFormat("hh:mm");
                    //java.util.Date toTime;
                    //toTime = df.parse(toTimeEtxt.getText().toString());

                    String combiFromDate = fromDateEtxt.getText().toString() + " " + fromTimeEtxt.getText().toString();
                    SimpleDateFormat dfCombi = new SimpleDateFormat("EEE dd.MM.yyyy HH:mm");
                    Log.d(AddLectureActivity.class.getSimpleName(), "CombiFromDate: " + combiFromDate);

                    Date longFromTime = dfCombi.parse(combiFromDate);

                    //Long longToTime = toTime.getTime() + longFromDate;

                    //java.util.Date fromTime;
                    //toTime = df.parse(fromTimeEtxt.getText().toString());
                    //Long longFromTime = toTime.getTime() + longFromDate;
                    String combiToDate = fromDateEtxt.getText().toString() + " " + toTimeEtxt.getText().toString();

                    Log.d(AddLectureActivity.class.getSimpleName(), "CombiToDate: " + combiToDate);
                    Date longToTime = dfCombi.parse(combiToDate);

                    String veranstaltungsName = veranstaltungsNameEtxt.getText().toString();



                    String selectedRoomName = (String) sItems.getSelectedItem();
                    Log.d(AddLectureActivity.class.getSimpleName(), "selectedRoomName: " + selectedRoomName);
                    //Nur damit selectedRoom initialisiert ist.
                    Raum selectedRoom = raumliste.get(0);

                    for(Raum raumInListe: raumliste){
                        if(raumInListe.getRaumname().toString().equals(selectedRoomName)){
                            selectedRoom = raumInListe;
                            break;
                        }

                    }







                    if(longFromTime.getTime()>= longToTime.getTime() ){
                        Toast.makeText(getApplicationContext(),"Die Startzeit liegt vor der Endzeit.", Toast.LENGTH_LONG).show();
                    }else{


                        if(verb.lecturePost(veranstaltungsName, longFromTime.getTime(),longToTime.getTime(),selectedRoom)){


                            Toast.makeText(getApplicationContext(),"Gespeichert", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(AddLectureActivity.this.getApplicationContext(), LectureListActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            AddLectureActivity.this.startActivity(intent);
                        }else{
                            Toast.makeText(getApplicationContext(),"Raum ist zu der ausgewählten Zeit schon blockiert", Toast.LENGTH_LONG).show();
                        }
                    }



                    //Toast.makeText(getApplicationContext(),"Raum ist zu der ausgewählten Zeit schon blockiert", Toast.LENGTH_LONG).show();
                    //verb.lecturePost(veranstaltungsName, longFromTime,longToTime,selectedRoom);
                    //onBackPressed();
                }catch(java.text.ParseException e)
                {
                    // Auto-generated catch block
                    e.printStackTrace();
                }



                //Toast.makeText(getApplicationContext(),"Raum ist zu der ausgewählten Zeit schon blockiert", Toast.LENGTH_LONG).show();
            }
        });



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);

        TextView profileName = (TextView)header.findViewById(R.id.profileName);
        TextView profileEmail = (TextView)header.findViewById(R.id.profileEmail);
        ImageView profileImage = (ImageView) header.findViewById(R.id.profileImage);

        Bundle profileInfo = getIntent().getExtras();
        if(BaseActivity.profileName.equals("")){
            BaseActivity.profileName=(profileInfo.get("profileName").toString());
        }
        if(BaseActivity.profileEmail.equals("")){
            BaseActivity.profileEmail=(profileInfo.get("profileEmail").toString());
        }
        if(BaseActivity.profileImageUrl.equals("")){
            BaseActivity.profileImageUrl=(profileInfo.get("profilePicture").toString());
        }
        profileName.setText(BaseActivity.profileName);
        profileEmail.setText(BaseActivity.profileEmail);

        Ion.with(getApplicationContext())
                .load(BaseActivity.profileImageUrl)
                .withBitmap()
                .placeholder(R.drawable.ic_hourglass_empty_black_24dp)
                .error(R.drawable.nopp)
                .animateIn(android.R.anim.fade_in)
                .intoImageView(profileImage);


        //drawer.setVisibility(View.GONE);
        //Log.d("Matthias", "LockDrawer");
        //super.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        //drawer.removeDrawerListener(toggle);
        //toggle.setDrawerIndicatorEnabled(false);
        //toggle.syncState();
        setDateTimeField();

    }



    private void findViewsById() {
        veranstaltungsNameEtxt = (EditText) findViewById(R.id.editText2);
        fromDateEtxt = (EditText) findViewById(R.id.DateEditText);
        fromDateEtxt.setInputType(InputType.TYPE_NULL);
        fromDateEtxt.requestFocus();

        toTimeEtxt = (EditText) findViewById(R.id.toTime);
        fromTimeEtxt = (EditText) findViewById(R.id.fromTime);

    }

    private void setDateTimeField() {
        fromDateEtxt.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fromDateEtxt.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        fromTimeEtxt.setOnClickListener(this);
        fromTimePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute){
                Calendar newDate = Calendar.getInstance();
                newDate.set(1970,1,1,hourOfDay,minute);
                SimpleDateFormat df1 = new SimpleDateFormat("HH:mm");
                String TimeFromSet = df1.format(newDate.getTime());
                fromTimeEtxt.setText(TimeFromSet);
            }
        },newCalendar.get(Calendar.HOUR_OF_DAY),newCalendar.get(Calendar.MINUTE),true);

        toTimeEtxt.setOnClickListener(this);
        toTimePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute){
                Calendar newDate = Calendar.getInstance();
                newDate.set(1970,1,1,hourOfDay,minute);
                SimpleDateFormat df1 = new SimpleDateFormat("HH:mm");
                String TimeToSet = df1.format(newDate.getTime());
                toTimeEtxt.setText(TimeToSet);
            }
        },newCalendar.get(Calendar.HOUR_OF_DAY),newCalendar.get(Calendar.MINUTE),true);


    }



    @Override
    public void onClick(View view) {
        if(view == fromDateEtxt) {
            fromDatePickerDialog.show();
        } else if(view == fromTimeEtxt) {
            fromTimePickerDialog.show();
        } else if(view == toTimeEtxt) {
            toTimePickerDialog.show();
        }
    }

    @Override
    public final void onBackPressed() {
        super.onBackPressed();

    }
}
