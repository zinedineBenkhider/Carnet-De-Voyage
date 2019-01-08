package com.example.android.carnetDeVoyage.dataBase;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.graphics.Picture;

import com.example.android.carnetDeVoyage.entities.Note;
import com.example.android.carnetDeVoyage.entities.PictureNote;
import com.example.android.carnetDeVoyage.entities.Trip;
import com.example.android.carnetDeVoyage.entities.TxtNote;

import java.util.List;

@Dao
public interface NoteDao {

    @Insert
    void insertImgNote(PictureNote note);

    @Insert
    void insertTxtNote(TxtNote note);

    @Query("SELECT * FROM note_table ORDER BY id ASC")
    LiveData< List<Note> >getAllNotes();
    @Query("SELECT * FROM txtNote_table WHERE tripId=:tripId")
    List<TxtNote> getTxtsNotesWithIdTrip( String tripId);
    @Query("SELECT * FROM txtNote_table WHERE tripId=:tripId")
    LiveData<List<TxtNote>> getTxtNotesWithIdTrip( String tripId);
    @Query("SELECT * FROM txtNote_table WHERE id=:idNote")
    TxtNote getTxtNoteWithId(String idNote);
    @Query("SELECT * FROM txtNote_table WHERE tripId=:tripId and date LIKE '%' || :date || '%'")
    List<TxtNote> getTxtNoteWithIDAndDate(String tripId,String date);
    @Query("SELECT * FROM txtNote_table WHERE tripId=:tripId and (date LIKE '%' || :value || '%' or place LIKE '%' || :value || '%' )")
    List<TxtNote> getTxtNoteWithIDAndDateOrCity(String tripId,String value);
    @Delete
    void deleteTxtNote(TxtNote txtNote);

    @Query("SELECT * FROM pictureNote_table WHERE tripId=:tripId")
   List<PictureNote> getPicturesNoteWithIdTrip( String tripId);
    @Query("SELECT * FROM pictureNote_table WHERE tripId=:tripId and (date LIKE '%' || :value || '%' or place LIKE '%' || :value || '%' )")
    List<PictureNote> getPictureNoteWithIDAndDateOrCity(String tripId,String value);
    @Query("SELECT * FROM pictureNote_table WHERE id=:idNote")
    PictureNote getPictureNoteWithId(String idNote);
    @Query("SELECT * FROM pictureNote_table WHERE tripId=:tripId")
    LiveData<List<PictureNote>> getAllPicturesNoteOfTrip(String tripId);
    @Delete
    void deletePictureNote(PictureNote pictureNote);


}
