package com.example.android.carnetDeVoyage.activities.noteActivities;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import com.example.android.carnetDeVoyage.R;

public class FullScreenPictureActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_picture);
        Intent i=getIntent();
        String url=i.getStringExtra(ListPicturesActivity.EXTRA_REPLY_FULL_SCREEN);
        ImageView image=findViewById(R.id.full_screen_picture);
        image.setImageURI(Uri.parse(url));
    }
}
