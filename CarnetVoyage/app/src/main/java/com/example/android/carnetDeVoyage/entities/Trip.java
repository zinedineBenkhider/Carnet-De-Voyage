package com.example.android.carnetDeVoyage.entities;

/*
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


/**

 */

@Entity(tableName = "trip_table")
public class Trip {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "uniqueID")
    public String uniqueID;
    public String continents;
    public String country;
    @ColumnInfo(name = "city")
    public String city;
    @ColumnInfo(name = "title")
    public String title ;
    public String date;


    public Trip(@NonNull String uniqueID) {
        this.uniqueID= uniqueID;
    }
    public String getUniqueID(){
        return this.uniqueID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle(){
        return this.title;
    }
    public String getCity() {
        return this.city;
    }
    public void setCity( String city) {
        this.city=city;
    }
    public String getCountry() {
        return this.country;
    }
    public void setCountry( String country) {
        this.country=country;
    }
    public String getContinents() {
        return this.continents;
    }
    public void setContinents( String continents) {
        this.continents=continents;
    }
}