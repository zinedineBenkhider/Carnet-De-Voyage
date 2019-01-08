package com.example.android.carnetDeVoyage.activities.tripActivities;
import android.app.Application;

import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;
import com.example.android.carnetDeVoyage.entities.Trip;
import com.example.android.carnetDeVoyage.reposistory.TripRepository;

public class UpdateTripViewModel extends AndroidViewModel {

    private TripRepository mTripReposistory;
    public UpdateTripViewModel(@NonNull Application application) {
        super(application);
        mTripReposistory= TripRepository.getInstance(application);
    }

    public void updateTrip( String id, String city, String continent, String title, String country) {

        Trip trip=mTripReposistory.getTripWithId(id);
        trip.setCity(city);
        trip.setContinents(continent);
        trip.setCountry(country);
        trip.title=title;
        mTripReposistory.update(trip);
    }
}