package com.swe.gruppe4.freespace;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import android.widget.TextView;
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

public class AddLectureActivity extends AppCompatActivity implements View.OnClickListener{
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
        final RestConnection verb = new RestConnection(this);
        final ArrayList<Raum> raumliste = verb.raumGet();





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

        Button saveLecture = (Button) findViewById(R.id.saveLectureButton);
        saveLecture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //EditText edittext = (TextView) convertView.findViewById(R.id.editText2);
                //String name = (String) textview.getText();
                //textview = (TextView) convertView.findViewById(R.id.DateEditText);
                //Date date = textview.getText();
                try {
                    SimpleDateFormat df = new SimpleDateFormat("dd.mm.yyyy");
                    java.util.Date fromDate;

                    fromDate = df.parse(fromDateEtxt.getText().toString());
                    Long longFromDate = fromDate.getTime();

                    df = new SimpleDateFormat("hh:mm");
                    java.util.Date toTime;
                    toTime = df.parse(toTimeEtxt.getText().toString());

                    Long longToTime = toTime.getTime() + longFromDate;

                    java.util.Date fromTime;
                    toTime = df.parse(fromTimeEtxt.getText().toString());
                    Long longFromTime = toTime.getTime() + longFromDate;

                    String veranstaltungsName = veranstaltungsNameEtxt.getText().toString();



                    String selectedRoomName = sItems.getSelectedItem().toString();

                    //Nur damit selectedRoom initialisiert ist.
                    Raum selectedRoom = raumliste.get(0);

                    for(Raum raumInListe: raumliste){
                        if(raumInListe.getRaumname().toString() == selectedRoomName){
                            selectedRoom = raumInListe;
                            break;
                        }

                    }


                    verb.lecturePost(veranstaltungsName, longFromTime,longToTime,selectedRoom);
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
}
