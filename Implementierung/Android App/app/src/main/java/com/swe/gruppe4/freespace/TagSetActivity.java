package com.swe.gruppe4.freespace;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.swe.gruppe4.freespace.Objektklassen.Tag;

import java.util.ArrayList;

public class TagSetActivity extends AppCompatActivity {
    private ListView tagView;
    private ArrayList<Tag> tagList;

    private RadioButton listRadioButton;

    private RadioGroup rg;
    int listIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        tagList = new Verbindung().tagGet();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tags_set);

        /*tagView = (ListView) findViewById(R.id.tagList);
        final TagSetAdapter tagAdapt = new TagSetAdapter(getApplicationContext(), R.layout.tag_box);
        tagView.setAdapter(tagAdapt);
        tagView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

        for(int i = 0; i < tagList.size(); i++) {
            tagAdapt.add(tagList.get(i));
        }

        tagAdapt.notifyDataSetChanged();*/

        rg = (RadioGroup) findViewById(R.id.tagList);
        for(Tag tag : tagList) {
            RadioButton rb = new RadioButton(this);
            rb.setText(tag.getName());
            rb.setId(tag.getId());
            rg.addView(rb);
        }



        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayShowTitleEnabled(true);
        }

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


        //tagView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        //Aktion, wenn Anwenden Button gedrückt wird
        Button saveButton = (Button) findViewById(R.id.saveTagsButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Verbindung().raumPut(rg.getCheckedRadioButtonId());
                finish();

            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    public void onClickRadioButton(View v) {
        View vMain = ((View) v.getParent());
        int newIndex = ((ViewGroup) vMain.getParent()).indexOfChild(vMain);
        if (listIndex == newIndex) return;

        if (listRadioButton != null) {
            listRadioButton.setChecked(false);
        }
        listRadioButton = (RadioButton) v;
        listIndex = newIndex;
    }

}
