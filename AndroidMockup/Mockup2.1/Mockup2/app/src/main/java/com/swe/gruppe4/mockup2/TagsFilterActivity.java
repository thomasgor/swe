package com.swe.gruppe4.mockup2;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.ListView;

public class TagsFilterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tags_filter);

        ListView tagView = (ListView) findViewById(R.id.tagList);
        TagAdapter tagAdapt = new TagAdapter(getApplicationContext(), R.layout.tag_box);
        tagView.setAdapter(tagAdapt);
        tagView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        tagAdapt.add(new Tag("Präsentation", false));
        tagAdapt.add(new Tag("Lernen", true));
        tagAdapt.add(new Tag("Ruhe", false));

        tagAdapt.notifyDataSetChanged();

        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayShowTitleEnabled(true);
        }

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
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
