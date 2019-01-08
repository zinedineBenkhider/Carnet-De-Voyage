package com.example.android.carnetDeVoyage.activities;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;
import com.example.android.carnetDeVoyage.R;
import com.example.android.carnetDeVoyage.activities.tripActivities.NewTripActivity;
import com.example.android.carnetDeVoyage.adapters.TripForAdapter;
import com.example.android.carnetDeVoyage.entities.PictureNote;
import com.example.android.carnetDeVoyage.entities.TagTxtNote;
import com.example.android.carnetDeVoyage.entities.TagPictureNote;
import com.example.android.carnetDeVoyage.entities.Trip;
import com.example.android.carnetDeVoyage.entities.TxtNote;
import com.example.android.carnetDeVoyage.fragments.ListTripFragment;
import com.example.android.carnetDeVoyage.reposistory.NoteRepository;
import com.example.android.carnetDeVoyage.reposistory.TagReposistory;
import com.example.android.carnetDeVoyage.reposistory.TripRepository;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import static android.app.Activity.RESULT_OK;
public class MainActivityViewModel extends AndroidViewModel {

    public static final int NEW_TRIP_ACTIVITY_REQUEST_CODE = 1;
    private TripRepository mTripReposistory;
    private NoteRepository mNoteReposistory;
    private TagReposistory mTagReposistory;
    private Application application;
    public static final int REQUEST_PERMISSION = 200;
    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        this.application=application;
        mTripReposistory=TripRepository.getInstance(application);
        mTagReposistory=TagReposistory.getInstance(application);
        mNoteReposistory=NoteRepository.getInstance(application);
    }
    public void activityResult(int requestCode, int resultCode,  Intent data){

        if (requestCode == NEW_TRIP_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK ) {
            if(data.getStringExtra(NewTripActivity.EXTRA_REPLY_TITLE)!=""){
            final Trip trip = new Trip(UUID.randomUUID().toString());
            trip.title= data.getStringExtra(NewTripActivity.EXTRA_REPLY_TITLE);
            trip.setCity(data.getStringExtra(NewTripActivity.EXTRA_REPLY_CITY));
            trip.setContinents(data.getStringExtra(NewTripActivity.EXTRA_REPLY_CONTINENTS));
            trip.setCountry(data.getStringExtra(NewTripActivity.EXTRA_REPLY_COUNTRY));
            String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
            trip.setDate(date);
            mTripReposistory.insert(trip);
            String pictureNotes=data.getStringExtra(NewTripActivity.EXTRA_REPLY_NOTES_IMG);
            String txtNotes=data.getStringExtra(NewTripActivity.EXTRA_REPLY_NOTES_TXT);
            if(!pictureNotes.equals("")){
            insertAllPicturesNote(pictureNotes, trip.getUniqueID());
            }
            if(!txtNotes.equals("")){
            insertAllTxtsNote(txtNotes, trip.getUniqueID());
            }
                Toast.makeText(getApplication(),
                        R.string.trip_saved,
                        Toast.LENGTH_LONG).show();}

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
        String longitude;
        String latitude;
        for(int i=0;i<allNotes.length;i++){
            picturesNote=allNotes[i].split(application.getString(R.string.contenu_note_separator));

            uriStr=picturesNote[0];
            tagsStr=picturesNote[1];
            placeStr=picturesNote[2];
            longitude=picturesNote[3];
            latitude=picturesNote[4];
            note =new PictureNote(UUID.randomUUID().toString(),tripID,date,placeStr);
            note.setLatitude(latitude);
            note.setLongItude(longitude);
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
        String longitude;
        String latitude;
        TagTxtNote tag;
        for(int i=0;i<allNotes.length;i++){
            txtNote=allNotes[i].split(application.getString(R.string.contenu_note_separator));
            textStr=txtNote[0];
            tagsStr=txtNote[1];
            placeStr=txtNote[2];
            longitude=txtNote[3];
            latitude=txtNote[4];
            note =new TxtNote(UUID.randomUUID().toString(),tripID,date,placeStr);
            note.setText(textStr);
            note.setLatitude(latitude);
            note.setLongItude(longitude);
            mNoteReposistory.insertTxtNote(note);
            tagsSplited=tagsStr.split(application.getString(R.string.tag_separator));
            for(int j=0;j<tagsSplited.length;j++){
                tag=new TagTxtNote(UUID.randomUUID().toString(),note.getId(),tagsSplited[j] );
                mTagReposistory.insertTag(tag);
            }
        }}

    public void freeDB(){
        mTripReposistory.clearDataBase();
    }
    public void observeDB(FragmentActivity activity){
        mTripReposistory.getAllTrips().observe(activity, new Observer<List<Trip>>() {
            @Override
            public void onChanged(@Nullable final List<Trip> trips) {
                if(ListTripFragment.itemsAdapter!=null){
                    ListTripFragment.itemsAdapter.clear();
                    for(int i=0;i<trips.size();i++) {
                        ListTripFragment.itemsAdapter.add(new TripForAdapter(trips.get(i).getTitle(),trips.get(i).getCountry()));
                    }
                }}
        });
    }
}
