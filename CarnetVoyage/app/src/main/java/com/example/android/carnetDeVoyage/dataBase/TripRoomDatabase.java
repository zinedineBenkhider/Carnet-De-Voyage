package com.example.android.carnetDeVoyage.dataBase;

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

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.android.carnetDeVoyage.entities.Note;
import com.example.android.carnetDeVoyage.entities.PictureNote;
import com.example.android.carnetDeVoyage.entities.TagTxtNote;
import com.example.android.carnetDeVoyage.entities.TagPictureNote;
import com.example.android.carnetDeVoyage.entities.Trip;
import com.example.android.carnetDeVoyage.entities.TxtNote;


@Database(entities = { Trip.class, Note.class,TxtNote.class,PictureNote.class,TagTxtNote.class,TagPictureNote.class}, version = 1,exportSchema = false)
public abstract class TripRoomDatabase extends RoomDatabase {

    public abstract TripDao TripDao();
    public abstract NoteDao NoteDao();
    public abstract TagDao TagDao();
    // marking the instance as volatile to ensure atomic access to the variable
    private static volatile TripRoomDatabase INSTANCE;
    public static TripRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TripRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TripRoomDatabase.class, "trip_database")
                            // Wipes and rebuilds instead of migrating if no Migration object.
                            // Migration is not part of this codelab.
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
