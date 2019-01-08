package com.example.android.carnetDeVoyage.activities.tripActivities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.example.android.carnetDeVoyage.R;
import com.example.android.carnetDeVoyage.fragments.TripDetailFragment;


public class UpdateTripActivity extends AppCompatActivity {

    private EditText mEditTitleView;
    private EditText mEditCityView;
    private EditText mEditCountryView;
    private EditText mEditContinentView;

    private String id;

    UpdateTripViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(UpdateTripViewModel.class);
        setContentView(R.layout.update_trip);
        mEditTitleView = findViewById(R.id.updateTitle);
        mEditCityView = findViewById(R.id.updateCity);
        mEditCountryView = findViewById(R.id.updateCountry);
        mEditContinentView = findViewById(R.id.updateContinent);

        getInformationOfTrip();
        Button buttonSave = findViewById(R.id.boutonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(mEditTitleView.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    String title = mEditTitleView.getText().toString();
                    replyIntent.putExtra(TripDetailFragment.EXTRA_REPLY_ID, id);
                    String city = mEditCityView.getText().toString();
                    String continents = mEditContinentView.getText().toString();
                    String country = mEditCountryView.getText().toString();
                    mViewModel.updateTrip( id, city, continents,title,country);
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });
    }
    public void getInformationOfTrip(){
        Intent data = getIntent();
        id=data.getStringExtra(TripDetailFragment.EXTRA_REPLY_ID);
        mEditTitleView.setText(data.getStringExtra(TripDetailFragment.EXTRA_REPLY_TITLE));
        mEditCityView.setText(data.getStringExtra(TripDetailFragment.EXTRA_REPLY_CITY));
        mEditContinentView.setText(data.getStringExtra(TripDetailFragment.EXTRA_REPLY_CONTINENTS));
        mEditCountryView.setText(data.getStringExtra(TripDetailFragment.EXTRA_REPLY_CONTRY));
    }
}
