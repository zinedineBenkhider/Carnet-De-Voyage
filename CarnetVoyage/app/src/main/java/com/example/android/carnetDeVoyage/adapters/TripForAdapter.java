package com.example.android.carnetDeVoyage.adapters;

public class TripForAdapter {
    private String title;
    private String date;
    public TripForAdapter(String title, String date){
        this.date=date;
        this.title=title;
    }

    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }
}
