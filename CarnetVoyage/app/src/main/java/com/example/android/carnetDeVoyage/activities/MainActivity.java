package com.example.android.carnetDeVoyage.activities;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.widget.Toast;

import com.example.android.carnetDeVoyage.R;
import com.example.android.carnetDeVoyage.activities.tripActivities.ListTripActivity;
import com.example.android.carnetDeVoyage.activities.tripActivities.NewTripActivity;

import static com.example.android.carnetDeVoyage.activities.MainActivityViewModel.REQUEST_PERMISSION;

public class MainActivity extends AppCompatActivity {
    public static final int NEW_TRIP_ACTIVITY_REQUEST_CODE = 1;


    private MainActivityViewModel mViewModel;
    @Override

    protected void onCreate(Bundle savedInstanceState) {

        Button  buttonAdd, buttonList,buttonClearDB,boutounExport;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        mViewModel.observeDB(this);
        buttonAdd =  findViewById(R.id.buttonAdd);
        buttonList =  findViewById(R.id.buttonList);
        buttonClearDB=findViewById(R.id.buttonClearDB) ;
        boutounExport=findViewById(R.id.buttonExport);
        boutounExport.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,ListTripActivity.class);
                startActivity(intent);
            }
        }
    );
        buttonAdd.setOnClickListener(new View.OnClickListener() {

            public void onClick(View button) {

                Intent intent=new Intent(MainActivity.this,NewTripActivity.class);

                startActivityForResult(intent, NEW_TRIP_ACTIVITY_REQUEST_CODE);

            }

        });
        buttonList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View button) {
                Intent intent=new Intent(MainActivity.this,ListTripActivity.class);
                startActivity(intent);
            }});
        buttonClearDB.setOnClickListener(new View.OnClickListener() {

            public void onClick(View button) {
                mViewModel.freeDB();
            }
        });

        askPermissionToWriteExternalStorage();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mViewModel.activityResult(requestCode,resultCode,data);
    }
    public void askPermissionToWriteExternalStorage(){
        if (Environment.MEDIA_MOUNTED!=Environment.getExternalStorageState()){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION);
        }


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Pour utiliser Carnet de voyage, vous devez accorder l'accée a vos fichiers", Toast.LENGTH_SHORT).show();
                askPermissionToWriteExternalStorage();
            }
            else{
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                99);

                    }
                }
            }
        }
        if (requestCode == 99&& grantResults.length > 0) {

                  if( grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                      Toast.makeText(this, "Pour utiliser Carnet de voyage, vous devez accorder l'accée a votre position", Toast.LENGTH_SHORT).show();
                      askPermissionToWriteExternalStorage();
                  }

            }

    }

}