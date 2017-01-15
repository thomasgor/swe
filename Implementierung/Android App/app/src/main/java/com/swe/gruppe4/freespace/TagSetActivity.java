package com.swe.gruppe4.freespace;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.swe.gruppe4.freespace.Objektklassen.AktuellerBenutzer;
import com.swe.gruppe4.freespace.Objektklassen.Benutzer;
import com.swe.gruppe4.freespace.Objektklassen.Tag;

import java.util.ArrayList;


/**
 * <p>Überschrift: Struktur von TagSetActivity</p>
 * <p>Beschreibung: Diese Activity dient dazu, Tags auszuwählen, um der momentan aktiven Sitzung einen Tag zuzuweisen.
 * </p>
 * <p>Organisation: FH Aachen, FB05, SWE Gruppe 4 </p>
 *
 * @author Merlin
 * @version 1.0
 *
 */
public class TagSetActivity extends AppCompatActivity {
    private ListView tagView;
    private ArrayList<Tag> tagList;

    private RadioButton listRadioButton;

    private RadioGroup rg;
    int listIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Benutzer ben = AktuellerBenutzer.getAktuellerBenutzer();
        if(ben.istProfessor()) {
            super.setTheme(R.style.AppThemeProf);
        }

        tagList = new RestConnection(this).tagGet();

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
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }



        //tagView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        //Aktion, wenn Anwenden Button gedrückt wird
        Button saveButton = (Button) findViewById(R.id.saveTagsButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent result = new Intent();
                result.putExtra("id",rg.getCheckedRadioButtonId());
                setResult(Activity.RESULT_OK,result);
                //new VerbindungDUMMY().raumPut(rg.getCheckedRadioButtonId(),raumID);
                finish();

            }
        });


    }

    /**
     * Funktion für den Zurückbutton in der oberen Ecke.
     *
     * @param menuItem gedrückter Knopf
     * @return super.onOptionsItemSelected(menuItem)*/
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

    /**
     * Funktion für das Setzen eines Tags. Simuliert RadioGroup
     *
     * @param v gedrückter Knopf
     * @return super.onOptionsItemSelected(menuItem)*/
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

    /**
     * Setzt 0 als gedrückten Tag, um Abbruch anzuzeigen
     *
     */
    @Override
    public void onBackPressed(){
        Intent result = new Intent();
        result.putExtra("id",0);
        setResult(Activity.RESULT_OK,result);
        //new VerbindungDUMMY().raumPut(rg.getCheckedRadioButtonId(),raumID);
        finish();
    }

}
