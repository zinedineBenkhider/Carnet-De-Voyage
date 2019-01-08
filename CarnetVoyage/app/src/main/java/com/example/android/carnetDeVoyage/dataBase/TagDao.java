package com.example.android.carnetDeVoyage.dataBase;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import com.example.android.carnetDeVoyage.entities.TagTxtNote;
import com.example.android.carnetDeVoyage.entities.TagPictureNote;

import java.util.List;

@Dao
public interface TagDao {
    @Insert
    void insertTag(TagTxtNote tag);
    @Query("SELECT * FROM tag_txt_table ORDER BY idTag ASC")
    LiveData< List<TagTxtNote> > getAllTags();
    @Query("SELECT * FROM tag_txt_table where noteId=:idNote and tag Like '%' || :tag || '%'")
    List<TagTxtNote> getTagTxtNoteWithIdNoteAndTag(String idNote, String tag);
    @Query("SELECT * FROM tag_txt_table where noteId=:idNote ")
    List<TagTxtNote> getTagsWithIdNote (String idNote);


    @Insert
    void insertTagPictureNote(TagPictureNote tagPictureNote);

    @Query("SELECT * FROM tag_picture_table where noteId=:idNote")
    List<TagPictureNote> getTagsPictureNoteWithIdNote(String idNote);
    @Query("SELECT * FROM tag_picture_table where noteId=:idNote and tag Like '%' || :tag || '%'")
    List<TagPictureNote> getTagPictureNoteWithIdNoteAndTag(String idNote,String tag);


}
