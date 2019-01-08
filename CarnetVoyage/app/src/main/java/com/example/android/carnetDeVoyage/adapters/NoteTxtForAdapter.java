package com.example.android.carnetDeVoyage.adapters;

public class NoteTxtForAdapter {
    private String place;
    private String date;
    private String tag;
    private String text;
    public  NoteTxtForAdapter(String date,String tag, String place,String text ){
        this.date=date;
        this.text=text;
        this.tag=tag;
        this.place=place;
    }

    public String getPlace() {
        return place;
    }

    public boolean compartTo(NoteTxtForAdapter obj) {
        if(this.text.equals(obj.text)&&this.date.equals(obj.getDate()) &&this.place.equals(obj.place)&&this.tag.equals(obj.tag)){
            return true;
        }
        return false;
    }
    public void setPlace(String place) {
        this.place = place;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public String getDate() {
        return date;
    }

    public String getTag() {
        return tag;
    }


}


