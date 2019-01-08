package com.example.android.carnetDeVoyage.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "txtNote_table",foreignKeys = @ForeignKey(entity = Trip.class,
        parentColumns = "uniqueID",
        childColumns = "tripId",
        onDelete = CASCADE))
public class TxtNote extends Note {
    public String text;
    public TxtNote(@NonNull String id, @NonNull String tripId,String date,String place) {
        super(id, tripId,date,place);
    }

    @Override
    public String getId() {
        return super.getId();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

