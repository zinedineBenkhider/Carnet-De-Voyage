package com.example.android.carnetDeVoyage.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "note_table",foreignKeys = @ForeignKey(entity = Trip.class,
        parentColumns = "uniqueID",
        childColumns = "tripId",
        onDelete = CASCADE))


public  class Note {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    public  String id;
    @ColumnInfo(name = "tripId")
    public String tripId;
    public String date;
    public String tags;
    public String place;
    public String longItude;
    public String latitude;
    public Note(@NonNull String id, @NonNull String tripId, String date, String place) {
        this.id = id;
        this.tripId = tripId;
        this.date=date;
        this.place=place;
    }
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    public void setLongItude(String longItude) {
        this.longItude = longItude;
    }
    public String getLatitude() {
        return latitude;
    }
    public String getLongItude() {
        return longItude;
    }
    public String getPlace() {
        return place;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setId(String id){
        this.id=id;
    }
    public String getId(){
        return this.id;
    }

}
