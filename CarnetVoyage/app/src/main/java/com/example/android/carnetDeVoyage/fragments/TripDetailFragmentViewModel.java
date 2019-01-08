package com.example.android.carnetDeVoyage.fragments;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;
import com.example.android.carnetDeVoyage.entities.Trip;
import com.example.android.carnetDeVoyage.reposistory.NoteRepository;
import com.example.android.carnetDeVoyage.reposistory.TripRepository;



public class TripDetailFragmentViewModel extends AndroidViewModel {
    private TripRepository mTripRepository;
    private NoteRepository mNoteRepository;
    public TripDetailFragmentViewModel(@NonNull Application application) {
        super(application);
        mTripRepository=TripRepository.getInstance(application);
        mNoteRepository=NoteRepository.getInstance(application);
    }
    public Trip getTripWithID(String id){
        return mTripRepository.getTripWithId(id);
    }
    public TripRepository getmTripRepository(){
        return mTripRepository;
    }



}
