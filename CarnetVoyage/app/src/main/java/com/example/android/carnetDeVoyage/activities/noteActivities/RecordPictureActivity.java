package com.example.android.carnetDeVoyage.activities.noteActivities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class RecordPictureActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY_FILE_PATH_PICTURE = "com.example.android.activities.RecordPictureActivity.NOTE_PICTURE";
    private String imageFilePath;
    private String pathOfPictureCreated="";
    public static final int REQUEST_IMAGE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openCameraIntent();
    }

    public String getPathOfPictureCreated() {
        return pathOfPictureCreated;
    }

    public void openCameraIntent() {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {


            File photoFile;
            try {
                photoFile = createImageFile();
            }
            catch (IOException e) {
                e.printStackTrace();
                return;
            }
            Uri photoUri = FileProvider.getUriForFile(this, this.getPackageName() +".provider", photoFile);
            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            setResult(RESULT_OK, pictureIntent);
            startActivityForResult(pictureIntent, REQUEST_IMAGE);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Intent i=  new Intent();
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {

                pathOfPictureCreated=imageFilePath;
                Log.i("pahth enregestre",pathOfPictureCreated);

                   i.putExtra(EXTRA_REPLY_FILE_PATH_PICTURE, pathOfPictureCreated);
                   setResult(RESULT_OK, i);
            }
            else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "La photo n'est pas souvergardée", Toast.LENGTH_SHORT).show();
            }
        }
        finish();

    }

    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmm", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp+"_" ;
        File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        imageFilePath = image.getAbsolutePath();

        return image;
    }

}
