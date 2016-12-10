package com.swe.gruppe4.freespace;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddLectureActivity extends AppCompatActivity implements View.OnClickListener{
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

        Spinner dropdown = (Spinner)findViewById(R.id.spinner);
        String[] items = new String[]{"G101", "G102", "G103"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Button saveLecture = (Button) findViewById(R.id.saveLectureButton);
        saveLecture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Raum ist zu der ausgewählten Zeit schon blockiert", Toast.LENGTH_LONG).show();
            }
        });

        dateFormatter = new SimpleDateFormat("EEE dd.MM.yyyy", Locale.GERMAN);

        findViewsById();

        setDateTimeField();

    }

    private void findViewsById() {
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
                newDate.set(2016,8,11,hourOfDay,minute);
                fromTimeEtxt.setText(hourOfDay+":"+minute);
            }
        },newCalendar.get(Calendar.HOUR_OF_DAY),newCalendar.get(Calendar.MINUTE),true);

        toTimeEtxt.setOnClickListener(this);
        toTimePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute){
                Calendar newDate = Calendar.getInstance();
                newDate.set(2016,8,11,hourOfDay,minute);
                toTimeEtxt.setText(hourOfDay+":"+minute);
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
