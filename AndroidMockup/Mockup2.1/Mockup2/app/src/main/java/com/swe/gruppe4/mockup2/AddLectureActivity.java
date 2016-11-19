package com.swe.gruppe4.mockup2;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static com.swe.gruppe4.mockup2.R.id.time;

public class AddLectureActivity extends AppCompatActivity {
    public long setTime;

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
        EditText dateTimeStart = (EditText) findViewById(R.id.editText3);
        dateTimeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View dialogView = View.inflate(getApplicationContext(), R.layout.date_time_picker, null);
                final AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();

                dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
                        TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);

                        Calendar calendar = new GregorianCalendar(datePicker.getYear(),
                                datePicker.getMonth(),
                                datePicker.getDayOfMonth(),
                                timePicker.getCurrentHour(),
                                timePicker.getCurrentMinute());

                        setTime = calendar.getTimeInMillis();
                        alertDialog.dismiss();
                    }});
                alertDialog.setView(dialogView);
                alertDialog.show();
            }
        });
    }
}
