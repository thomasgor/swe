package com.swe.gruppe4.freespace;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
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

public class LectureEditActivity extends BaseActivity implements View.OnClickListener{
    private EditText veranstaltungsNameEtxt;

    private EditText fromDateEtxt;
    private EditText fromTimeEtxt;
    private EditText toTimeEtxt;

    private DatePickerDialog fromDatePickerDialog;

    private TimePickerDialog fromTimePickerDialog;
    private TimePickerDialog toTimePickerDialog;

    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lecture);

        //Spinner dropdown = (Spinner)findViewById(R.id.spinner);
        //String[] items = new String[]{"G101", "G102", "G103"};
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //dropdown.setAdapter(adapter);

       // VerbindungDUMMY verb = new VerbindungDUMMY();
        RestConnection verb = new RestConnection(this);

        final ArrayList<Raum> raumliste = verb.raumGet();


        List<String> spinnerArray =  new ArrayList<>();
        for(Raum raum: raumliste){
            spinnerArray.add(raum.getRaumname());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner sItems = (Spinner) findViewById(R.id.spinner);
        sItems.setAdapter(adapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setTitle("FreeSpace");

        Button saveLecture = (Button) findViewById(R.id.saveLectureButton);

        //final VerbindungDUMMY v = new VerbindungDUMMY();
        final RestConnection v = new RestConnection(this);

        saveLecture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //EditText edittext = (TextView) convertView.findViewById(R.id.editText2);
                //String name = (String) textview.getText();
                //textview = (TextView) convertView.findViewById(R.id.DateEditText);
                //Date date = textview.getText();
                int id = getIntent().getIntExtra("Id", 0);


                try {
                    SimpleDateFormat df = new SimpleDateFormat("EEE dd.MM.yyyy");
                    java.util.Date fromDate;
                    fromDate = df.parse(fromDateEtxt.getText().toString());
                    Long longFromDate = fromDate.getTime();

                    //df = new SimpleDateFormat("HH:mm");
                    //java.util.Date toTime;
                    //toTime = df.parse(toTimeEtxt.getText().toString());

                    String combiFromDate = fromDateEtxt.getText().toString() + " " + toTimeEtxt.getText().toString();
                    SimpleDateFormat dfCombi = new SimpleDateFormat("EEE dd.MM.yyyy HH:mm");



                    Date longToTime = dfCombi.parse(combiFromDate);


                    //java.util.Date fromTime;
                    //toTime = df.parse(fromTimeEtxt.getText().toString());
                    String combiToDate = fromDateEtxt.getText().toString() + " " + fromTimeEtxt.getText().toString();
                    Log.d("Matthias", "Kombiniertes String Datum: " + combiToDate);
                            Date longFromTime = dfCombi.parse(combiToDate);

                    String veranstaltungsName = veranstaltungsNameEtxt.getText().toString();

                    String selectedRoomName = sItems.getSelectedItem().toString();

                    //Nur damit selectedRoom initialisiert ist.
                    Raum selectedRoom = raumliste.get(0);

                    for(Raum raum: raumliste){
                        if(raum.getRaumname().toString().equals(selectedRoomName)){
                            selectedRoom = raum;
                            break;
                        }

                    }


                        if(selectedRoom == null) {
                            Log.d("edu", "raum ist nULL!!");
                        } else {
                            Log.d("edu", "raum ist nicht null!! " + selectedRoom.getRaumname());
                        }

                        if(longFromTime.getTime()>= longToTime.getTime() ){
                            Toast.makeText(getApplicationContext(),"Die Startzeit liegt vor der Endzeit.", Toast.LENGTH_LONG).show();
                        }else {
                            Log.d("Mat", "Id: " + Integer.toString(id) + " Name: " + veranstaltungsName + " from: " + Long.toString(longFromTime.getTime()) + " to: " + Long.toString(longToTime.getTime()));
                            if(v.lecturePut(id, veranstaltungsName, longFromTime.getTime(), longToTime.getTime(), selectedRoom)) {
                                Toast.makeText(getApplicationContext(), "Änderungen gespeichert", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(LectureEditActivity.this.getApplicationContext(), LectureListActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                LectureEditActivity.this.startActivity(intent);
                            }else{
                                Toast.makeText(getApplicationContext(),"Raum ist zu der ausgewählten Zeit schon blockiert", Toast.LENGTH_LONG).show();
                            }
                        }

                        //onBackPressed();




                }catch(java.text.ParseException e)
                {

                    // Auto-generated catch block
                    e.printStackTrace();
                }



                //Toast.makeText(getApplicationContext(),"Raum ist zu der ausgewählten Zeit schon blockiert", Toast.LENGTH_LONG).show();
            }
        });

        dateFormatter = new SimpleDateFormat("EEE dd.MM.yyyy", Locale.GERMAN);

        findViewsById();

        setDateTimeField();
        fromDateEtxt.setInputType(0);
        fromTimeEtxt.setInputType(0);
        toTimeEtxt.setInputType(0);

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
        final int id = getIntent().getIntExtra("Id", 0);

        //final VerbindungDUMMY verbindung = new VerbindungDUMMY();
        final RestConnection verbindung = new RestConnection(this);

        final Veranstaltung veranstaltung = verbindung.lectureGet(id);
        long longFrom = veranstaltung.getVon();
        long longTo = veranstaltung.getBis();
        SimpleDateFormat dfYear = new SimpleDateFormat("YYYY");
        int yearFrom = Integer.parseInt(dfYear.format(longFrom));
        SimpleDateFormat dfMonth = new SimpleDateFormat("MM");
        int monthFrom = Integer.parseInt(dfMonth.format(longFrom));
        SimpleDateFormat dfDay = new SimpleDateFormat("dd");
        int dayFrom = Integer.parseInt(dfDay.format(longFrom));
        SimpleDateFormat dfHour = new SimpleDateFormat("HH");
        String hourFrom = dfHour.format(longFrom);
        int hourFromInt = Integer.parseInt(hourFrom);
        String hourTo = dfHour.format(longTo);
        int hourToInt = Integer.parseInt(hourTo);
        SimpleDateFormat dfMin = new SimpleDateFormat("mm");
        String minuteFrom = dfMin.format(longFrom);
        int minuteFromInt = Integer.parseInt(minuteFrom);
        String minuteTo = dfMin.format(longTo);
        int minuteToInt =  Integer.parseInt(minuteTo);
        fromDateEtxt.setText(dateFormatter.format(longFrom));
        fromDateEtxt.setOnClickListener(this);


        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fromDateEtxt.setText(dateFormatter.format(newDate.getTime()));

            }
            //Hier werden die "Startwerte" für den DatePicker gesetzt.
        },yearFrom, monthFrom-1, dayFrom);

        fromTimeEtxt.setText(hourFrom +":"+minuteFrom);
        fromTimeEtxt.setOnClickListener(this);
        fromTimePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute){
                Calendar newDate = Calendar.getInstance();
                newDate.set(1970,0,0,hourOfDay,minute);
                SimpleDateFormat df1 = new SimpleDateFormat("HH:mm");
                String TimeFromSet = df1.format(newDate.getTime());
                fromTimeEtxt.setText(TimeFromSet);
            }
        },newCalendar.get(Calendar.HOUR_OF_DAY),newCalendar.get(Calendar.MINUTE),true);
        fromTimePickerDialog.updateTime(hourFromInt, minuteFromInt);
        toTimeEtxt.setText(hourTo+":"+minuteTo);
        toTimeEtxt.setOnClickListener(this);
        toTimePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute){
                Calendar newDate = Calendar.getInstance();
                newDate.set(1970,0,0,hourOfDay,minute);
                SimpleDateFormat df1 = new SimpleDateFormat("HH:mm");
                String timeFromSet = df1.format(newDate.getTime());
                toTimeEtxt.setText(timeFromSet);
            }
        },newCalendar.get(Calendar.HOUR_OF_DAY),newCalendar.get(Calendar.MINUTE),true);
        toTimePickerDialog.updateTime(hourToInt, minuteToInt);
        veranstaltungsNameEtxt.setText(veranstaltung.getName(), TextView.BufferType.EDITABLE);
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
    public void onBackPressed() {

        super.onBackPressed();

    }
}
