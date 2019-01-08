package com.example.android.carnetDeVoyage.activities.tripActivities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.android.carnetDeVoyage.R;
import com.example.android.carnetDeVoyage.activities.noteActivities.TakePictureNoteActivity;
import com.example.android.carnetDeVoyage.activities.noteActivities.TakeTxtNoteActivity;


public class NewTripActivity extends AppCompatActivity {
    public static final String EXTRA_REPLY_COUNTRY = "com.example.android.mainActivity.COUNTRY";
    public static final String EXTRA_REPLY_TITLE = "com.example.android.mainActivity.TITLE";
    public static final String EXTRA_REPLY_CONTINENTS = "com.example.android.mainActivity.CONTINENTS";
    public static final String EXTRA_REPLY_CITY = "com.example.android.mainActivity.CITY";
    public static final String EXTRA_REPLY_NOTES_IMG = "com.example.android.mainActivity.NOTES_IMG";
    public static final String EXTRA_REPLY_NOTES_TXT = "com.example.android.mainActivity.NOTE_TXT";
    private static final int NEW_NOTES_PICTURES_ACTIVITY_REQUEST_CODE = 339;
    private static final int NEW_NOTES_TXT_ACTIVITY_REQUEST_CODE = 330;
    private EditText mEditTitleView=null;
    private EditText mEditCityView;
    private EditText mEditCountryView;
    private EditText mEditContinentView;

    private String noteString="";
    private String  notesPicture="";
    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_trip);

        sendInformations();
    }
    public void sendInformations(){

        mEditTitleView = findViewById(R.id.edit_title);
        mEditCityView = findViewById(R.id.edit_city);
        mEditCountryView = findViewById(R.id.edit_country);
        mEditContinentView=findViewById(R.id.edit_continents);
        final Button save_trip_button = findViewById(R.id.button_save);
        final Button take_picture_note_button= findViewById(R.id.take_picture_note);
        final Button take_txt_note_button= findViewById(R.id.take_txt_note);
        save_trip_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(mEditTitleView.getText())) {//if field of title is empty
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    String title = mEditTitleView.getText().toString();
                    replyIntent.putExtra(EXTRA_REPLY_TITLE, title);
                    String city = mEditCityView.getText().toString();
                    replyIntent.putExtra(EXTRA_REPLY_CITY, city);
                    String continents = mEditContinentView.getText().toString();
                    replyIntent.putExtra(EXTRA_REPLY_CONTINENTS, continents);
                    String  country= mEditCountryView.getText().toString();
                    replyIntent.putExtra(EXTRA_REPLY_COUNTRY, country);
                    replyIntent.putExtra(EXTRA_REPLY_NOTES_TXT, noteString);
                    replyIntent.putExtra(EXTRA_REPLY_NOTES_IMG,  notesPicture);
                    setResult(RESULT_OK, replyIntent);
                }
                finish();

            }
        });

        take_picture_note_button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View button) {
                Intent intent=new Intent(NewTripActivity.this,TakePictureNoteActivity.class);
                startActivityForResult(intent, NEW_NOTES_PICTURES_ACTIVITY_REQUEST_CODE);
            }
        });
        take_txt_note_button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View button) {
                Intent intent=new Intent(NewTripActivity.this,TakeTxtNoteActivity.class);
                startActivityForResult(intent, NEW_NOTES_TXT_ACTIVITY_REQUEST_CODE);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_NOTES_PICTURES_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            notesPicture=data.getStringExtra(TakePictureNoteActivity.EXTRA_REPLY_NOTES_IMG);
            if(notesPicture.equals("")){
                Toast.makeText(
                        this,
                        R.string.notes_img_not_saved,
                        Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(
                        this,
                        R.string.notes_img_saved,
                        Toast.LENGTH_LONG).show();
            }


        }
        else if (requestCode == NEW_NOTES_TXT_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            noteString=data.getStringExtra(TakeTxtNoteActivity.EXTRA_REPLY_NOTES_TXT);
            if(noteString.equals("")){
                Toast.makeText(
                        this,
                        R.string.notes_str_not_saved,
                        Toast.LENGTH_LONG).show();
            }
            else
            {
            Toast.makeText(
                    this,
                    R.string.notes_str_saved,
                    Toast.LENGTH_LONG).show();}
        }


    }

}