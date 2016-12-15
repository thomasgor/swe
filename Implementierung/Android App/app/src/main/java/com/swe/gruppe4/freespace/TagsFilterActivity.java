package com.swe.gruppe4.freespace;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.swe.gruppe4.freespace.Objektklassen.Tag;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Stellt eine Liste mit Checkboxen bereit. Jedem verfügbaren Tag wird eine Checkbox bereitgestellt.
 * Die Tags die gesetzt werden, werden im Filter der Raumliste berücksichtigt.
 *
 * @author Marco Linnartz
 * @version 1.0
 */

public class TagsFilterActivity extends AppCompatActivity {
    private ListView tagView;

    /**
     * Anfangs werden die aktuell verfügbaren Tags vom Server über ein Verbindungsobjekt angefragt.
     * Die Tags werden danach in die Listview mit Hilfe des TagAdapter eingefügt.
     * Wenn auf den AnwendenButton gedrückt wird, werden die gesetzten Tags in eine Sharedpreferences Datei
     * gespeichtert. Die Datei steht der RoomActivity zur Anzeige und Filterun zur Verfügung. Danach wird die
     * RoomActivity über ein explizietes Intent gestartet.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tags_filter);

        tagView = (ListView) findViewById(R.id.tagList);
        final TagAdapter tagAdapt = new TagAdapter(getApplicationContext(), R.layout.tag_box);
        tagView.setAdapter(tagAdapt);
        tagView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

        Verbindung connection = new Verbindung();

        ArrayList<Tag> tagList = connection.tagGet();
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

                SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("com.swe.gruppe4.freespace.roomfilter", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putStringSet("filterTags", new HashSet<String>(tagAdapt.getCheckedTags()));
                editor.commit();

                Intent intent = new Intent(getApplicationContext(),RoomActivity.class);
                startActivity(intent);
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

}
