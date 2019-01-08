package com.example.android.carnetDeVoyage.entities;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


import static android.arch.persistence.room.ForeignKey.CASCADE;
@Entity(tableName = "tag_txt_table",foreignKeys ={ @ForeignKey(entity = TxtNote.class,
        parentColumns = "id",
        childColumns = "noteId",
        onDelete = CASCADE)})

public class TagTxtNote {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "idTag")
    public  String idTag;
    @ColumnInfo(name = "noteId")
    public  String noteId;
    @ColumnInfo(name = "tag")
    public  String tag;
    public TagTxtNote(@NonNull String idTag, String noteId, String tag){
        this.idTag=idTag;
        this.noteId=noteId;
        this.tag=tag;
    }
    public String getTag() {
        return tag;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }
    public String getNoteId() {
        return noteId;
    }
    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

}
