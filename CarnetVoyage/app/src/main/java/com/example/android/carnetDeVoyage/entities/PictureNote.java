package com.example.android.carnetDeVoyage.entities;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.net.Uri;
import android.support.annotation.NonNull;
import java.io.File;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "pictureNote_table",foreignKeys = @ForeignKey(entity = Trip.class,
        parentColumns = "uniqueID",
        childColumns = "tripId",
        onDelete = CASCADE))
public class PictureNote extends  Note{
    public String uri;
    public PictureNote(@NonNull String id, @NonNull String tripId,String date,String place) {
        super(id, tripId,date,place);
    }
    public void setUri(String uri) {
        this.uri = uri;
    }

    public Uri getUri() {
        return Uri.fromFile(new File(this.uri));
    }
}
