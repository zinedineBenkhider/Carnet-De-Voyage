package com.example.android.carnetDeVoyage.fragments;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.example.android.carnetDeVoyage.R;
import com.example.android.carnetDeVoyage.activities.noteActivities.ListPicturesActivity;
import com.example.android.carnetDeVoyage.activities.noteActivities.ListTxtNotesActivity;
import com.example.android.carnetDeVoyage.activities.tripActivities.UpdateTripActivity;
import com.example.android.carnetDeVoyage.entities.Trip;

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class TripDetailFragment extends Fragment {
    public static int position = 0;

    public static final String EXTRA_REPLY_CONTRY = "com.example.android.UpdateTripActivity.COUNTRY";
    public static final String EXTRA_REPLY_TITLE = "com.example.android.UpdateTripActivity.TITLE";
    public static final String EXTRA_REPLY_CONTINENTS = "com.example.android.UpdateTripActivity.CONTINENTS";
    public static final String EXTRA_REPLY_CITY = "com.example.android.UpdateTripActivity.CITY";
    public static final String EXTRA_REPLY_ID = "com.example.android.UpdateTripActivity.ID";
    public static final int UPDATE_TRIP_ACTIVITY_REQUEST_CODE = 2;

    private  TripDetailFragmentViewModel mViewModel;
    TextView tripTitleTv;
    TextView tripCountryTv;
    TextView tripCityTv;
    TextView tripContinentTv;
    String id;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = ViewModelProviders.of(this).get(TripDetailFragmentViewModel.class);
        if(savedInstanceState == null){
            if(getArguments() != null) {
                position = getArguments().getInt("position", 0);
            }
        }
        if(!ListTripFragment.idsAdapter.isEmpty()){ //if list trip is empty we can't find any id
            id=ListTripFragment.idsAdapter.getItem(position);
        }

        mViewModel.getmTripRepository().getAllTrips().observe(this, new Observer<List<Trip>>() {
            @Override
            public void onChanged(@Nullable final List<Trip> trips) {

                if (mViewModel.getmTripRepository().getAllTrips().getValue().size()>0)
                    onViewCreated(getView(),savedInstanceState);//show detail of another trip
                else
                {//when list of trips is empty
                    tripTitleTv =  getView().findViewById(R.id.tripTitle);
                    tripCountryTv =  getView().findViewById(R.id.tripCountry);
                    tripCityTv= getView().findViewById(R.id.tripCity);
                    tripContinentTv= getView().findViewById(R.id.tripContinent);
                    tripTitleTv.setText("");
                    tripCountryTv.setText("");
                    tripCityTv.setText("");
                    tripContinentTv.setText("");

                    Button buttonUpdate =getActivity().findViewById(R.id.boutonUpdate);
                    if (buttonUpdate!=null){
                    buttonUpdate.setVisibility(View.INVISIBLE);}//make button invisible
                }
            }});
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        // Inflate the xml file for the fragment
        return inflater.inflate(R.layout.fragment_detail_trip, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {


        Button buttonUpdate = view.findViewById(R.id.boutonUpdate);
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onClickSendDataToUpdateTrip();
            }
        });
        Button buttonListPictures = view.findViewById(R.id.bouton_notes_picture);
        Button buttonListTxtNote=view.findViewById(R.id.bouton_notes_txt);
        buttonListTxtNote.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),ListTxtNotesActivity.class);
                intent.putExtra(EXTRA_REPLY_ID, id);
                getActivity().setResult(RESULT_OK, intent);
                startActivity(intent);
            }
        });
        buttonListPictures.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(),ListPicturesActivity.class);

                intent.putExtra(EXTRA_REPLY_ID, id);

                startActivity(intent);

            }
        });


        if (!ListTripFragment.itemsAdapter.isEmpty()){

            String id=ListTripFragment.idsAdapter.getItem(position);
            tripTitleTv =  view.findViewById(R.id.tripTitle);
            tripCountryTv = view.findViewById(R.id.tripCountry);
            tripCityTv= view.findViewById(R.id.tripCity);
            tripContinentTv= view.findViewById(R.id.tripContinent);

            //update view
            Trip trip=mViewModel.getTripWithID(id);
            tripTitleTv.setText(trip.title);
            tripCountryTv.setText(trip.getCountry());
            tripCityTv.setText(trip.getCity());
            tripContinentTv.setText(trip.getContinents());
        }
    }

    public void onClickSendDataToUpdateTrip(){
        Intent intent = new Intent(getActivity(),UpdateTripActivity.class);

            String title = tripTitleTv.getText().toString();
            intent.putExtra(EXTRA_REPLY_TITLE, title);
            intent.putExtra(EXTRA_REPLY_ID, id);
            String city = tripCityTv.getText().toString();
            intent.putExtra(EXTRA_REPLY_CITY, city);
            String continents = tripContinentTv.getText().toString();
            intent.putExtra(EXTRA_REPLY_CONTINENTS, continents);
            String  country= tripCountryTv.getText().toString();
            intent.putExtra(EXTRA_REPLY_CONTRY, country);
            getActivity().setResult(RESULT_OK, intent);

        startActivityForResult(intent, UPDATE_TRIP_ACTIVITY_REQUEST_CODE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UPDATE_TRIP_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {

            Trip trip=mViewModel.getTripWithID(id);
            tripTitleTv.setText(trip.title);
            tripCountryTv.setText(trip.getCountry());
            tripCityTv.setText(trip.getCity());
            tripContinentTv.setText(trip.getContinents());
        }


    }
}