package com.example.android.carnetDeVoyage.adapters;

import android.net.Uri;

public class NotePictureForAdapter {
    private Uri image;
    private String place;
    private String date;
    private String tag;
    public NotePictureForAdapter(Uri image, String date, String tag, String place ){
        this.date=date;
        this.image=image;
        this.tag=tag;
        this.place=place;
    }

    public boolean compartTo(NotePictureForAdapter obj) {
        if(this.date.equals(obj.getDate()) &&this.place.equals(obj.place)&&this.tag.equals(obj.tag)){
            return true;
        }
       return false;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDate() {
        return date;
    }

    public String getTag() {
        return tag;
    }

    public Uri getImage() {
        return image;
    }
}


