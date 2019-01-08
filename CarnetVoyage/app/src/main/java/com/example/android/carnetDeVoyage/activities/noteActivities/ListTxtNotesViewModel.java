package com.example.android.carnetDeVoyage.activities.noteActivities;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.widget.ArrayAdapter;

import com.example.android.carnetDeVoyage.R;
import com.example.android.carnetDeVoyage.adapters.ListNoteTxtAdapter;
import com.example.android.carnetDeVoyage.adapters.NoteTxtForAdapter;
import com.example.android.carnetDeVoyage.entities.TagTxtNote;
import com.example.android.carnetDeVoyage.entities.TxtNote;
import com.example.android.carnetDeVoyage.reposistory.NoteRepository;
import com.example.android.carnetDeVoyage.reposistory.TagReposistory;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class ListTxtNotesViewModel extends AndroidViewModel {
    private NoteRepository mNoteRepository;
    private TagReposistory mTagReposistory;
    public ListTxtNotesViewModel(@NonNull Application application) {
        super(application);
        mNoteRepository=NoteRepository.getInstance(application);
        mTagReposistory=TagReposistory.getInstance(application);
    }
    public Pair<List<NoteTxtForAdapter>, List<String>>  txtNoteToTxtNoteForAdapter(List<TxtNote> listTxtNotes){
        List<NoteTxtForAdapter> notesTxtItems=new ArrayList<>();
        NoteTxtForAdapter item;
        List<String> listId=new ArrayList<>();
        String date,place,text;
        StringBuilder tags= new StringBuilder(getApplication().getString(R.string.item_tags));
        for (int i=0;i<listTxtNotes.size();i++){
            listId.add(listTxtNotes.get(i).getId());
            date=listTxtNotes.get(i).getDate();
            List<TagTxtNote> listTags=mTagReposistory.getTagsWithIdNote(listTxtNotes.get(i).getId());
            for(int j=0;j< listTags.size();j++){
                tags.append(getApplication().getString(R.string.befor_each_tag)).append(listTags.get(j).getTag()).append(getApplication().getString(R.string.tag_separator_display));
            }
            place=getApplication().getString(R.string.item_place)+listTxtNotes.get(i).getPlace();
            text=getApplication().getString(R.string.item_text)+listTxtNotes.get(i).getText();
            item=new NoteTxtForAdapter(date, tags.toString(),place,text);
            notesTxtItems.add(item);
            tags = new StringBuilder(getApplication().getString(R.string.item_tags));
        }
        return Pair.create(notesTxtItems,listId);
    }
    public Pair<List<NoteTxtForAdapter>, List<String>> getListAllItems(String idTrip){
        List<TxtNote> listTxtNotesResult=mNoteRepository.getListTxtsNotesWithIdTrip(idTrip);

        return  txtNoteToTxtNoteForAdapter(listTxtNotesResult);
    }
    public Pair<List<NoteTxtForAdapter>, List<String>> getListItemsWithDateOrCity(String dateParam,String idTrip){
        List<TxtNote> listTxtNotesResult=mNoteRepository.getTxtNoteWithIDAndDateOrCity(idTrip,dateParam);
        return txtNoteToTxtNoteForAdapter(listTxtNotesResult);

    }

    public Pair<List<NoteTxtForAdapter>, List<String>> getListItemsWithTag(String tag,String idTrip){
        List<TxtNote> listTxtNotes=mNoteRepository.getListTxtNoteWithIdTrip(idTrip).getValue();
        List<TxtNote> listTxtNotesResult=new ArrayList<>();
        List<TagTxtNote> listTag;
        if(listTxtNotes!=null){
        for(int i=0;i<listTxtNotes.size();i++){
            listTag =mTagReposistory.getTagsWithIdNoteAndTag(listTxtNotes.get(i).getId(),tag);
            if(!listTag.isEmpty()){
                listTxtNotesResult.add(listTxtNotes.get(i));
            }
        }}
        return  txtNoteToTxtNoteForAdapter(listTxtNotesResult);

    }
    public void deleteNote(String idNote){
        TxtNote txtNote=  mNoteRepository.getTxtNoteWithId(idNote);
        mNoteRepository.deleteTxtNote(txtNote);
    }
    public void insertAllTxtsNote( String txtNotes,String tripID){
        TxtNote note;
        String date= new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        String [] allNotes=txtNotes.split(getApplication().getString(R.string.note_separator));
        String [] txtNote;
        String textStr;
        String tagsStr;
        String placeStr;
        String [] tagsSplited;
        TagTxtNote tag;
        String longitude;
        String latitude;
        for(int i=0;i<allNotes.length;i++){
            txtNote=allNotes[i].split(getApplication().getString(R.string.contenu_note_separator));
            textStr=txtNote[0];
            tagsStr=txtNote[1];
            placeStr=txtNote[2];
            longitude=txtNote[3];
            latitude=txtNote[4];
            note =new TxtNote(UUID.randomUUID().toString(),tripID,date,placeStr);
            note.setText(textStr);
            note.setLatitude(latitude);
            note.setLongItude(longitude);
            mNoteRepository.insertTxtNote(note);
            tagsSplited=tagsStr.split(getApplication().getString(R.string.tag_separator));
            for(int j=0;j<tagsSplited.length;j++){
                tag=new TagTxtNote(UUID.randomUUID().toString(),note.getId(),tagsSplited[j] );
                mTagReposistory.insertTag(tag);
            }
        }
    }
    public void observeListNote(final AppCompatActivity activity, final String idTrip){
        mNoteRepository.getListTxtNoteWithIdTrip(idTrip).observe(activity, new Observer<List<TxtNote>>() {

            @Override
            public void onChanged(@Nullable List<TxtNote> listTxtNote) {

                Pair<List<NoteTxtForAdapter>, List<String>> tuple=txtNoteToTxtNoteForAdapter(listTxtNote);
                ListTxtNotesActivity.idsAdapter=new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1,getListAllItems(idTrip).second);
                ListTxtNotesActivity.itemsAdapter = new ListNoteTxtAdapter(getApplication(),tuple.first);
                ListTxtNotesActivity.lvItems.setAdapter(ListTxtNotesActivity.itemsAdapter);
            }
        });

    }
}
