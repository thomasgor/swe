package com.swe.gruppe4.mockup2;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.swe.gruppe4.mockup2.Objektklassen.*;
import com.swe.gruppe4.mockup2.Objektklassen.Tag;

import java.util.ArrayList;

public class TagsFilterActivity extends AppCompatActivity {
    private ListView tagView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tags_filter);

        tagView = (ListView) findViewById(R.id.tagList);
        final TagAdapter tagAdapt = new TagAdapter(getApplicationContext(), R.layout.tag_box);
        tagView.setAdapter(tagAdapt);
        tagView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

        Verbindung connection = new Verbindung();

        ArrayList<com.swe.gruppe4.mockup2.Objektklassen.Tag> tagList = connection.tagGet();
        for(int i = 0; i < tagList.size(); i++) {
            tagAdapt.add(tagList.get(i));
        }

        tagAdapt.notifyDataSetChanged();

        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayShowTitleEnabled(true);
        }

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


        //Aktion, wenn Anwenden Button gedrückt wird
        Button saveButton = (Button) findViewById(R.id.saveTagsButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = getApplicationContext();
                String text = "Tag Filter setzen!";
                for (int i = 0; i < tagAdapt.getCheckedTags().size(); i++) {
                    text += "\n";
                    text += tagAdapt.getCheckedTags().get(i);
                }
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
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

}
