package com.example.android.carnetDeVoyage.activities.noteActivities;
import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.Observer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.widget.ArrayAdapter;

import com.example.android.carnetDeVoyage.R;
import com.example.android.carnetDeVoyage.adapters.ListNotePictureAdapter;
import com.example.android.carnetDeVoyage.adapters.NotePictureForAdapter;
import com.example.android.carnetDeVoyage.adapters.TripForAdapter;
import com.example.android.carnetDeVoyage.entities.PictureNote;
import com.example.android.carnetDeVoyage.entities.TagPictureNote;
import com.example.android.carnetDeVoyage.reposistory.NoteRepository;
import com.example.android.carnetDeVoyage.reposistory.TagReposistory;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class ListPicturesViewModel extends AndroidViewModel {

    private NoteRepository mNoteRepository;
    private TagReposistory mTagReposistory;
    public ListPicturesViewModel(@NonNull Application application) {
        super(application);
        mNoteRepository=NoteRepository.getInstance(application);
        mTagReposistory=TagReposistory.getInstance(application);
    }
    public Pair<List<NotePictureForAdapter>, List<String>>  pictureNoteToPictureNoteForAdapter(List<PictureNote> listPicturesNote){

        List<String> listId=new ArrayList<>();
        List<NotePictureForAdapter> listItems=new ArrayList<>();
        List<TagPictureNote> listTags;
        String date,place;
        StringBuilder tags= new StringBuilder(getApplication().getString(R.string.item_tags));
        Uri uri;
        for(int i=0;i<listPicturesNote.size();i++){
            listId.add(listPicturesNote.get(i).getId());
            uri=listPicturesNote.get(i).getUri();
            date=listPicturesNote.get(i).getDate();
            listTags=mTagReposistory.getTagsPictureNoteWithIdNote(listPicturesNote.get(i).getId());
            for(int j=0;j<listTags.size();j++){
                if(!listTags.get(j).getTag().equals(" ")) {
                    tags.append(getApplication().getString(R.string.befor_each_tag)).append(listTags.get(j).getTag()).append(getApplication().getString(R.string.tag_separator_display));
                }}
            place=getApplication().getString(R.string.item_place)+listPicturesNote.get(i).getPlace();
            listItems.add(new NotePictureForAdapter(uri,date, tags.toString(),place));
            tags = new StringBuilder(getApplication().getString(R.string.item_tags));
        }

        return Pair.create(listItems,listId);
    }
    public Pair<List<NotePictureForAdapter>, List<String>>getListAllItems(String tripId){
        List<PictureNote> listPicturesNoteResult=mNoteRepository.getPicturesNoteWithIdTrip(tripId);
        return pictureNoteToPictureNoteForAdapter(listPicturesNoteResult);
    }

    public Pair<List<NotePictureForAdapter>, List<String>>  getListItemsWithDateOrCity(String value,String idTrip){
        List<PictureNote> listPicturesNoteResult=mNoteRepository.getPicturesNoteWithIDAndDateOrCity(idTrip,value);
        return pictureNoteToPictureNoteForAdapter(listPicturesNoteResult);
    }


    public Pair<List<NotePictureForAdapter>, List<String>> getListItemsWithTags(String tag,String idTrip){
        List<PictureNote> listPicturesNote=mNoteRepository.getPicturesNoteWithIdTrip(idTrip);
        List<PictureNote> listPicturesNoteResult=new ArrayList<>();
        List<TagPictureNote> listTag;
        for(int i=0;i<listPicturesNote.size();i++){
            listTag =mTagReposistory.getTagsPictureWithIdNoteAndTag(listPicturesNote.get(i).getId(),tag);//trouver tous les tags qui contient tag et qui correpondent à la note
            if(!listTag.isEmpty()){// si il existe au moin un tag, la note sera affichée
                listPicturesNoteResult.add(listPicturesNote.get(i));
            }
        }
        return pictureNoteToPictureNoteForAdapter(listPicturesNoteResult);
    }

    public void insertAllPicturesNote( String picturesNotes,String tripID){
        PictureNote note;
        String date= new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        String [] allNotes=picturesNotes.split(getApplication().getString(R.string.note_separator));
        String [] picturesNoteNote;
        String uriStr;
        String tagsStr;
        String placeStr;
        String [] tagsSplited;
        TagPictureNote tag;
        String longitude;
        String latitude;
        for(int i=0;i<allNotes.length;i++){
            picturesNoteNote=allNotes[i].split(getApplication().getString(R.string.contenu_note_separator));
            uriStr=picturesNoteNote[0];
            tagsStr=picturesNoteNote[1];
            placeStr=picturesNoteNote[2];
            longitude=picturesNoteNote[3];
            latitude=picturesNoteNote[4];
            note =new PictureNote(UUID.randomUUID().toString(),tripID,date,placeStr);
            note.setUri(uriStr);
            note.setLatitude(latitude);
            note.setLongItude(longitude);
            mNoteRepository.insertPictureNote(note);
            tagsSplited=tagsStr.split(getApplication().getString(R.string.tag_separator));
            for(int j=0;j<tagsSplited.length;j++){
                tag=new TagPictureNote(UUID.randomUUID().toString(),note.getId(),tagsSplited[j] );
                mTagReposistory.insertTagPictureNote(tag);
            }
        }}



        public String getUrlOfPictureWithIdNote(String idNote){
          return String.valueOf(mNoteRepository.getPictureNoteWithId(idNote).getUri());
        }
        public void deleteNote(String idNote){
          PictureNote pictureNote=  mNoteRepository.getPictureNoteWithId(idNote);
          mNoteRepository.deletePictureNote(pictureNote);
        }
    public void observeListNote(final AppCompatActivity activity, final String idTrip){
        mNoteRepository.getAllPicturesNoteOfTrip(idTrip).observe(activity, new Observer<List<PictureNote>>() {

            @Override
            public void onChanged(@Nullable List<PictureNote> listPicturesNote) {
                ListPicturesActivity.allItemsAdapter.clear();
                List<TagPictureNote> listTags;
                String date,place;
                StringBuilder tags= new StringBuilder(getApplication().getString(R.string.item_tags));
                Uri uri;
                for(int i=0;i<listPicturesNote.size();i++){
                    uri=listPicturesNote.get(i).getUri();
                    date=listPicturesNote.get(i).getDate();
                    listTags=mTagReposistory.getTagsPictureNoteWithIdNote(listPicturesNote.get(i).getId());
                    for(int j=0;j<listTags.size();j++){
                        if(!listTags.get(j).getTag().equals(" ")) {
                            tags.append(getApplication().getString(R.string.befor_each_tag)).append(listTags.get(j).getTag()).append(getApplication().getString(R.string.tag_separator_display));
                        }}
                    place=getApplication().getString(R.string.item_place)+listPicturesNote.get(i).getPlace();
                    ListPicturesActivity.allItemsAdapter.add(new NotePictureForAdapter(uri,date, tags.toString(),place));
                    tags = new StringBuilder(getApplication().getString(R.string.item_tags));
                }
                ListPicturesActivity.idsAdapter=new ArrayAdapter<>(getApplication(), android.R.layout.simple_list_item_1,getListAllItems(idTrip).second);
                ListPicturesActivity.itemsAdapter = new ListNotePictureAdapter(getApplication(), ListPicturesActivity.allItemsAdapter);
                ListPicturesActivity.lvItems.setAdapter(ListPicturesActivity.itemsAdapter);
                }
            });

    }
}
