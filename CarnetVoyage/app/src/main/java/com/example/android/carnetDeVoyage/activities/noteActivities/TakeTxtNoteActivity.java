package com.example.android.carnetDeVoyage.activities.noteActivities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.carnetDeVoyage.R;

public class TakeTxtNoteActivity extends AppCompatActivity

    {
        public static final String EXTRA_REPLY_NOTES_TXT = "com.example.android.activities.takeNoteActivity.NOTE_TXT";
        private String tags=" ";
        private String place=" ";
        private String text="";
        private String notesTxt="";
        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_txt_note);
        final Button buttonAddTag = findViewById(R.id.add_tag_note_txt);
        buttonAddTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText etTag=findViewById(R.id.edit_tag_note_txt);
                if(!etTag.getText().toString().equals("")){
                tags+=etTag.getText().toString()+getString(R.string.tag_separator);
                displayMsgTagSaved(etTag.getText().toString());
                etTag.setText("");
                }

            }
        });

        final Button buttonAddNote = findViewById(R.id.add_note_txt);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText etPlace=findViewById(R.id.edit_place_note_txt);
                EditText etText=findViewById(R.id.edit_text_note_txt);
                Location location=getCurrentLocation();
                String longitude= "3.138443736435079";//String.valueOf(location.getLongitude());
                String latitude= "50.60955330933749";//String.valueOf(location.getLatitude());
                text=etText.getText().toString();

                place=etPlace.getText().toString();
                if(place.equals("")){
                    place= " ";
                }
                if (text.equals("")){
                    displayMsgTextIsEmpty();
                }
                else{
                notesTxt+=text+getString(R.string.contenu_note_separator)+tags+getString(R.string.contenu_note_separator)+place+getString(R.string.contenu_note_separator)+longitude+getString(R.string.contenu_note_separator)+latitude+getString(R.string.note_separator);
                etPlace.setText("");
                etText.setText("");
                tags="";
                displayMsgNoteSaved();

                }
            }}
        );

        final Button buttonSaveNotes = findViewById(R.id.save_notes);
        buttonSaveNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                replyIntent.putExtra(EXTRA_REPLY_NOTES_TXT,notesTxt);
                setResult(RESULT_OK, replyIntent);
                finish();
            }
        });
    }
        public void displayMsgTextIsEmpty(){
            Toast.makeText(this, "Vous devez écrire un text pour que votre note soit enregetrée", Toast.LENGTH_SHORT).show();
        }
        public void displayMsgNoteSaved(){
            Toast.makeText(this, "Votre note est enregetrée", Toast.LENGTH_SHORT).show();
        }
        public void displayMsgTagSaved(String tag){
            Toast.makeText(this, "Le tag "+tag+" est enregitré", Toast.LENGTH_SHORT).show();
        }
        public Location getCurrentLocation() {
            LocationManager locationManager;
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            String provider;
            Criteria criteria = new Criteria();
            provider = locationManager.getBestProvider(criteria, false);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            99);
                }
                getCurrentLocation();
            }


            return locationManager.getLastKnownLocation(provider);
        }

}
