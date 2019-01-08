package com.example.android.carnetDeVoyage.fragments;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Pair;
import com.example.android.carnetDeVoyage.entities.PictureNote;
import com.example.android.carnetDeVoyage.entities.TxtNote;
import com.example.android.carnetDeVoyage.reposistory.NoteRepository;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

public class MapsViewModel extends AndroidViewModel {

    private NoteRepository mNoteReposistory;

    public MapsViewModel(@NonNull Application application) {
        super(application);
        mNoteReposistory=NoteRepository.getInstance(application);
    }
    public Pair<List<PictureNote>,List<TxtNote>> getAllNotesWithId(String id){

        List<PictureNote> picturesNotes= mNoteReposistory.getPicturesNoteWithIdTrip(id);
        List<TxtNote> txtNoteList=mNoteReposistory.getListTxtsNotesWithIdTrip(id);


        return new Pair<>(picturesNotes,txtNoteList);
    }

    public void observeListNotes(GoogleMap mMap, String id, FragmentActivity fragment){
        LiveData<List<PictureNote>> picturesNotes= mNoteReposistory.getAllPicturesNoteOfTrip(id);
        LiveData<List<TxtNote>> txtNoteList=mNoteReposistory.getListTxtNoteWithIdTrip(id);
        final String idTrip=id;
        final GoogleMap mGoogleMap=mMap;
        picturesNotes.observe(fragment, new Observer<List<PictureNote>>() {
            @Override
            public void onChanged(@Nullable final List<PictureNote> pictureNotes) {
                mGoogleMap.clear();
                PolylineOptions lineOptions = new PolylineOptions();
                LatLng postion;
                for(int j=0;j<getAllNotesWithId(idTrip).second.size();j++){
                    postion=new LatLng(Double.valueOf(getAllNotesWithId(idTrip).second.get(j).getLatitude()),Double.valueOf(getAllNotesWithId(idTrip).second.get(j).getLongItude()));
                    mGoogleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(Double.valueOf(getAllNotesWithId(idTrip).second.get(j).getLatitude()),Double.valueOf(getAllNotesWithId(idTrip).second.get(j).getLongItude())))
                            .title(getAllNotesWithId(idTrip).second.get(j).getPlace())
                            .snippet(getAllNotesWithId(idTrip).second.get(j).getDate())
                    );
                    lineOptions.add(postion);
                }
                for(int i=0;i<getAllNotesWithId(idTrip).first.size();i++){

                    postion =new LatLng(Double.valueOf(getAllNotesWithId(idTrip).first.get(i).getLatitude()),Double.valueOf(getAllNotesWithId(idTrip).first.get(i).getLongItude()));
                    mGoogleMap.addMarker(new MarkerOptions()
                            .position(postion)
                            .title(getAllNotesWithId(idTrip).first.get(i).getPlace())
                            .snippet(getAllNotesWithId(idTrip).first.get(i).getDate())


                    );
                    lineOptions.add(postion);
                }
               }

        });
        txtNoteList.observe(fragment, new Observer<List<TxtNote>>() {
            @Override
            public void onChanged(@Nullable final List<TxtNote> pictureNotes) {
                mGoogleMap.clear();
                PolylineOptions lineOptions = new PolylineOptions();
                LatLng postion;
                for(int j=0;j<getAllNotesWithId(idTrip).second.size();j++){
                    postion=new LatLng(Double.valueOf(getAllNotesWithId(idTrip).second.get(j).getLatitude()),Double.valueOf(getAllNotesWithId(idTrip).second.get(j).getLongItude()));
                    mGoogleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(Double.valueOf(getAllNotesWithId(idTrip).second.get(j).getLatitude()),Double.valueOf(getAllNotesWithId(idTrip).second.get(j).getLongItude())))
                            .title(getAllNotesWithId(idTrip).second.get(j).getPlace())
                            .snippet(getAllNotesWithId(idTrip).second.get(j).getDate())
                    );
                    lineOptions.add(postion);
                }
                for(int i=0;i<getAllNotesWithId(idTrip).first.size();i++){

                    postion =new LatLng(Double.valueOf(getAllNotesWithId(idTrip).first.get(i).getLatitude()),Double.valueOf(getAllNotesWithId(idTrip).first.get(i).getLongItude()));
                    mGoogleMap.addMarker(new MarkerOptions()
                            .position(postion)
                            .title(getAllNotesWithId(idTrip).first.get(i).getPlace())
                            .snippet(getAllNotesWithId(idTrip).first.get(i).getDate())

                    );
                    lineOptions.add(postion);
                }
            }

        });

    }


}
