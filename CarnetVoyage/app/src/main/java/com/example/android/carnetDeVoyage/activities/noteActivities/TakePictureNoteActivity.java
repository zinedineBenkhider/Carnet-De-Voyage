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

public class TakePictureNoteActivity extends AppCompatActivity {
    public static final String EXTRA_REPLY_NOTES_IMG = "com.example.android.activities.takeNoteActivity.NOTES_IMG";
    public static final int RECORD_PICTURE_ACTIVITY_REQUEST_CODE = 90;

    private String pathPicture = "";
    private String tags = "";
    private String place = "";
    private String notesPicture = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_picture_note);

        Button buttonTakePicture = findViewById(R.id.take_picture);

        buttonTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TakePictureNoteActivity.this, RecordPictureActivity.class);

                startActivityForResult(intent, RECORD_PICTURE_ACTIVITY_REQUEST_CODE);

            }
        });

        Button buttonAddTag = findViewById(R.id.add_tag);
        buttonAddTag.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                EditText etTag = findViewById(R.id.edit_tag_note_picture);
                if(!etTag.getText().toString().equals("")){
                tags += etTag.getText().toString() + getString(R.string.tag_separator);

                displayMsgTagSaved(etTag.getText().toString());
                etTag.setText("");
                }


            }
        });
        Button buttonAddNote = findViewById(R.id.add_note);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View view) {
                                                 EditText etPlacePicture = findViewById(R.id.edit_place_note_picture);
                                                 place = etPlacePicture.getText().toString();
                                                 Location location = getCurrentLocation();

                                                 String longitude= "3.138443736435079";//String.valueOf(location.getLongitude());
                                                 String latitude= "50.60955330933749";//String.valueOf(location.getLatitude());

                                                 if (place.equals("")) {
                                                     place = " ";
                                                 }
                                                 if (tags.equals("")) {
                                                     tags = " ";
                                                 }
                                                 if(pathPicture.equals("")){
                                                     displayMsgPathIsEmpty();
                                                 }
                                                 else {
                                                     notesPicture += pathPicture + getString(R.string.contenu_note_separator) + tags + getString(R.string.contenu_note_separator) + place + getString(R.string.contenu_note_separator) + longitude + getString(R.string.contenu_note_separator) + latitude + getString(R.string.note_separator);
                                                     tags = "";
                                                     etPlacePicture.setText("");
                                                     displayMsgNoteSaved();
                                                 }

                                             }
                                         }
        );
        Button buttonSaveNotes = findViewById(R.id.save_notes);
        buttonSaveNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                replyIntent.putExtra(EXTRA_REPLY_NOTES_IMG, notesPicture);
                setResult(RESULT_OK, replyIntent);
                finish();}

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RECORD_PICTURE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            if(data.getStringExtra(RecordPictureActivity.EXTRA_REPLY_FILE_PATH_PICTURE)!=null){
                pathPicture=data.getStringExtra(RecordPictureActivity.EXTRA_REPLY_FILE_PATH_PICTURE);
            }

        }
    }
    public void displayMsgPathIsEmpty(){
        Toast.makeText(this, "Vous devez prendre une photo pour que votre note soit enregetrée", Toast.LENGTH_SHORT).show();
    }
    public void displayMsgNoteSaved(){
        Toast.makeText(this, "La note est enregitrée", Toast.LENGTH_SHORT).show();
    }
    public void displayMsgTagSaved(String tag){
        Toast.makeText(this, "Le tag "+tag+" est enregitré", Toast.LENGTH_SHORT).show();
    }

    public Location getCurrentLocation() {
        LocationManager locationManager;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String provider;
        Criteria criteria = new Criteria();
        assert locationManager != null;
        provider = locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        99);

            }


        }

        return locationManager.getLastKnownLocation(provider);
    }


}
