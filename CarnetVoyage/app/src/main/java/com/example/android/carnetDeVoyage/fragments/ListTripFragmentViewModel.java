package com.example.android.carnetDeVoyage.fragments;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Pair;
import android.widget.Toast;

import com.example.android.carnetDeVoyage.R;
import com.example.android.carnetDeVoyage.activities.tripActivities.NewTripActivity;
import com.example.android.carnetDeVoyage.adapters.TripForAdapter;
import com.example.android.carnetDeVoyage.entities.PictureNote;
import com.example.android.carnetDeVoyage.entities.TagTxtNote;
import com.example.android.carnetDeVoyage.entities.TagPictureNote;
import com.example.android.carnetDeVoyage.entities.Trip;
import com.example.android.carnetDeVoyage.entities.TxtNote;
import com.example.android.carnetDeVoyage.reposistory.NoteRepository;
import com.example.android.carnetDeVoyage.reposistory.TagReposistory;
import com.example.android.carnetDeVoyage.reposistory.TripRepository;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static com.example.android.carnetDeVoyage.fragments.ListTripFragment.NEW_TRIP_ACTIVITY_REQUEST_CODE;

public class ListTripFragmentViewModel extends AndroidViewModel {

    private NoteRepository mNoteReposistory;
    private TagReposistory mTagReposistory;
    private Application application;
    private TripRepository mTripRepository;

    public ListTripFragmentViewModel(@NonNull Application application) {

        super(application);
        this.application=application;
        mTripRepository=TripRepository.getInstance(application);
        mTagReposistory=TagReposistory.getInstance(application);
        mNoteReposistory=NoteRepository.getInstance(application);

    }
    public List<String> getListIdOfTrips(){

       List<String> listID=new ArrayList<>();
       List<Trip> listTrip=mTripRepository.getAllTrips().getValue();
       for(int i=0;i<listTrip.size();i++){
           listID.add(listTrip.get(i).getUniqueID());
       }
       return listID;

    }
    public List<TripForAdapter> getListAllItems(){

        List<TripForAdapter> listItems=new ArrayList<>();
        List<Trip> listTrip=mTripRepository.getAllTrips().getValue();
        for(int i=0;i<listTrip.size();i++){
            listItems.add(new TripForAdapter(listTrip.get(i).getTitle(),listTrip.get(i).getDate()));
        }
        return  listItems;
    }
    public void observeListTrip(FragmentActivity activity){
        mTripRepository.getAllTrips().observe(activity, new Observer<List<Trip>>() {
            @Override
            public void onChanged(@Nullable final List<Trip> trips) {//when trip deleted or added
                // Update the cached copy of the words in the adapter.
                if(ListTripFragment.itemsAdapter!=null && ListTripFragment.idsAdapter!=null){
                    ListTripFragment.idsAdapter.clear();
                    ListTripFragment.itemsAdapter.clear();
                    for(int i=0;i<trips.size();i++) {
                        ListTripFragment.idsAdapter.add(trips.get(i).uniqueID);
                        ListTripFragment.itemsAdapter.add(new TripForAdapter(trips.get(i).getTitle(),trips.get(i).getDate()));
                    }
                }}
        });
    }
    public Trip getTrip(String id){
       return mTripRepository.getTripWithId(id);
    }
    public List<TxtNote> getTxtNotes(String idTrip){
        return mNoteReposistory.getListTxtNoteWithIdTrip(idTrip).getValue();
    }
    public List<PictureNote> getPicturesNotes(String idTrip){
        return mNoteReposistory.getPicturesNoteWithIdTrip(idTrip);
    }

    public List<TagPictureNote> getPicturesTagsOfTrip(String idTrip){
        List<TagPictureNote> listTags=new ArrayList<>();
        List<PictureNote>  listNote=mNoteReposistory.getPicturesNoteWithIdTrip(idTrip);
        if(listNote!=null){
        for(int i=0;i<listNote.size();i++){
            listTags.addAll(mTagReposistory.getTagsPictureNoteWithIdNote(listNote.get(i).getId()));
        }}
        return listTags;
        }
    public List<TagTxtNote> getTxtTagsOfTrip(String idTrip){
        List<TagTxtNote> listTags=new ArrayList<>();
        List<TxtNote>  listNote=mNoteReposistory.getListTxtsNotesWithIdTrip(idTrip);
        if(listNote!=null){
            for(int i=0;i<listNote.size();i++){
                listTags.addAll(mTagReposistory.getTagsWithIdNote(idTrip));
            }}
        return listTags;
    }
    public int sizeOfListTrips(){
       return mTripRepository.getAllTrips().getValue().size();
    }
    public void deleteTrip(Trip trip){
    mTripRepository.delete(trip);}

    public Pair<List<TripForAdapter>, List<String>> getListTripAndTheirIdWithDateOrTitle(String dateOrTitle){
        List<String> listId=new ArrayList<>();
        List<Trip> listTripFound=mTripRepository.getTripWithDateOrTitle(dateOrTitle);
        List<TripForAdapter> listItems=new ArrayList<>();
        for(int i=0;i<listTripFound.size();i++){
            listItems.add(new TripForAdapter(listTripFound.get(i).getTitle(),listTripFound.get(i).getDate()));
            listId.add(listTripFound.get(i).getUniqueID());
        }
        return Pair.create(listItems,listId);
    }

    public void activityResult(int requestCode, int resultCode,  Intent data){

        if (requestCode == NEW_TRIP_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK &&  data.getStringExtra(NewTripActivity.EXTRA_REPLY_TITLE)!="") {
            final Trip trip = new Trip(UUID.randomUUID().toString());
            trip.title= data.getStringExtra(NewTripActivity.EXTRA_REPLY_TITLE);
            trip.setCity(data.getStringExtra(NewTripActivity.EXTRA_REPLY_CITY));
            trip.setContinents(data.getStringExtra(NewTripActivity.EXTRA_REPLY_CONTINENTS));
            trip.setCountry(data.getStringExtra(NewTripActivity.EXTRA_REPLY_COUNTRY));
            String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
            trip.setDate(date);
            mTripRepository.insert(trip);
            String pictureNotes=data.getStringExtra(NewTripActivity.EXTRA_REPLY_NOTES_IMG);
            String txtNotes=data.getStringExtra(NewTripActivity.EXTRA_REPLY_NOTES_TXT);
            if(!pictureNotes.equals("")){
                insertAllPicturesNote(pictureNotes, trip.getUniqueID());
            }
            if(!txtNotes.equals("")){
                insertAllTxtsNote(txtNotes, trip.getUniqueID());
            }

        }
        else {
            Toast.makeText(
                    getApplication(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }
    public void insertAllPicturesNote(String pictureNotes,String tripID){
        String date= new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        String [] allNotes=pictureNotes.split(application.getString(R.string.note_separator));
        PictureNote note;
        String [] picturesNote;
        String placeStr;
        String tagsStr;
        String uriStr;
        String [] tagsSplited;
        TagPictureNote tag;
        for(int i=0;i<allNotes.length;i++){
            picturesNote=allNotes[i].split(application.getString(R.string.contenu_note_separator));
            uriStr=picturesNote[0];
            tagsStr=picturesNote[1];
            placeStr=picturesNote[2];
            note =new PictureNote(UUID.randomUUID().toString(),tripID,date,placeStr);
            note.setUri(uriStr);
            mNoteReposistory.insertPictureNote(note);
            tagsSplited=tagsStr.split(application.getString(R.string.tag_separator));
            for(int j=0;j<tagsSplited.length;j++){
                tag=new TagPictureNote(UUID.randomUUID().toString(),note.getId(),tagsSplited[j] );
                mTagReposistory.insertTagPictureNote(tag);
            }
        }
    }

    public void insertAllTxtsNote( String txtNotes,String tripID){
        TxtNote note;
        String date= new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        String [] allNotes=txtNotes.split(application.getString(R.string.note_separator));
        String [] txtNote;
        String textStr;
        String tagsStr;
        String placeStr;
        String [] tagsSplited;
        TagTxtNote tag;
        for(int i=0;i<allNotes.length;i++){
            txtNote=allNotes[i].split(application.getString(R.string.contenu_note_separator));
            textStr=txtNote[0];
            tagsStr=txtNote[1];
            placeStr=txtNote[2];
            note =new TxtNote(UUID.randomUUID().toString(),tripID,date,placeStr);
            note.setText(textStr);
            mNoteReposistory.insertTxtNote(note);
            tagsSplited=tagsStr.split(application.getString(R.string.tag_separator));
            for(int j=0;j<tagsSplited.length;j++){
                tag=new TagTxtNote(UUID.randomUUID().toString(),note.getId(),tagsSplited[j] );
                mTagReposistory.insertTag(tag);

            }

        }}


}
