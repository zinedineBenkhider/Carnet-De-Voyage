package com.example.android.carnetDeVoyage.reposistory;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.carnetDeVoyage.dataBase.NoteDao;
import com.example.android.carnetDeVoyage.dataBase.TripRoomDatabase;
import com.example.android.carnetDeVoyage.entities.Note;
import com.example.android.carnetDeVoyage.entities.PictureNote;
import com.example.android.carnetDeVoyage.entities.TxtNote;


import java.util.List;
import java.util.concurrent.ExecutionException;

public class NoteRepository {

    private NoteDao mNoteDao;
    private LiveData<List<Note>> mAllNote;
    private TripRoomDatabase db;
    private  static   NoteRepository INSTANCE =null;

    public NoteRepository(Application application) {
        db = TripRoomDatabase.getDatabase(application);
        mNoteDao=db.NoteDao();
        mAllNote = mNoteDao.getAllNotes();
    }



    public static NoteRepository  getInstance(Application application){
        if (INSTANCE==null){
            INSTANCE=new NoteRepository(application);
        }
        return INSTANCE;
    }


    public void insertTxtNote(TxtNote note) {
        new NoteRepository.insertTxtNoteAsyncTask(mNoteDao).execute(note);

    }

    public List<TxtNote> getTxtNoteWithIDAndDateOrCity(String tripId,String value){
        try {
            return  new GetTxtNoteWithDateOrCityAndIdTripAsyncTask(mNoteDao).execute(tripId,value).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
    public List<PictureNote> getPicturesNoteWithIdTrip(String idTrip){
        try {
            return  new getPicturesNotesWithIdTripAsyncTask(mNoteDao).execute(idTrip).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
    public LiveData<List<TxtNote>> getListTxtNoteWithIdTrip(String idTrip){
        try {
            return  new getAsyncTask(mNoteDao).execute(idTrip).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
    public List<TxtNote> getListTxtsNotesWithIdTrip(String idTrip){
        try {
            return  new GetTxtsNotesWithIdTripAsyncTask(mNoteDao).execute(idTrip).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
    private static class GetTxtsNotesWithIdTripAsyncTask extends AsyncTask<String, Void, List<TxtNote>> {

        private NoteDao mAsyncTaskDaoNote;
        GetTxtsNotesWithIdTripAsyncTask(NoteDao noteDao) {

            mAsyncTaskDaoNote=noteDao;
        }

        @Override
        protected List<TxtNote> doInBackground(final String... params) {

            return mAsyncTaskDaoNote.getTxtsNotesWithIdTrip(params[0]);
        }

    }
    private static class GetTxtNoteWithDateOrCityAndIdTripAsyncTask extends AsyncTask<String, Void, List<TxtNote>> {

        private NoteDao mAsyncTaskDaoNote;
        GetTxtNoteWithDateOrCityAndIdTripAsyncTask (NoteDao noteDao) {

            mAsyncTaskDaoNote=noteDao;
        }

        @Override
        protected List<TxtNote> doInBackground(final String... params) {

            return mAsyncTaskDaoNote.getTxtNoteWithIDAndDateOrCity(params[0],params[1]);
        }

    }

    private static class getAsyncTask extends AsyncTask<String, Void, LiveData<List<TxtNote>>> {

        private NoteDao mAsyncTaskDaoNote;
        getAsyncTask(NoteDao noteDao) {

            mAsyncTaskDaoNote=noteDao;
        }

        @Override
        protected LiveData<List<TxtNote>> doInBackground(final String... params) {

            return mAsyncTaskDaoNote.getTxtNotesWithIdTrip(params[0]);
        }

    }
    private static class getPicturesNotesWithIdTripAsyncTask extends AsyncTask<String, Void, List<PictureNote>> {

        private NoteDao mAsyncTaskDaoNote;
        getPicturesNotesWithIdTripAsyncTask(NoteDao noteDao) {

            mAsyncTaskDaoNote=noteDao;
        }

        @Override
        protected List<PictureNote> doInBackground(final String... params) {
            return mAsyncTaskDaoNote.getPicturesNoteWithIdTrip(params[0]);

        }

    }







    private static class insertTxtNoteAsyncTask extends AsyncTask<TxtNote, Void, Void> {

        private NoteDao mAsyncTaskDaoNote;
        insertTxtNoteAsyncTask (NoteDao noteDao) {

            mAsyncTaskDaoNote=noteDao;
        }
        @Override
        protected Void doInBackground(final TxtNote... params) {

            mAsyncTaskDaoNote.insertTxtNote(params[0]);
            return null;
        }

    }

    private static class GetTxtNoteWithIDAsyncTask extends AsyncTask<String, Void, TxtNote> {

        private NoteDao mAsyncTaskDaoNote;
        GetTxtNoteWithIDAsyncTask (NoteDao noteDao) {

            mAsyncTaskDaoNote=noteDao;
        }

        @Override
        protected TxtNote doInBackground(final String... params) {

            return mAsyncTaskDaoNote.getTxtNoteWithId(params[0]);
        }

    }
    public  TxtNote getTxtNoteWithId(String idNote){
        try {
            return new GetTxtNoteWithIDAsyncTask(mNoteDao).execute(idNote).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return  null;
    }
    private static class DeletetTxtNoteAsyncTask extends AsyncTask<TxtNote,Void,Void> {
        private NoteDao mAsyncTaskDaoNote;
        DeletetTxtNoteAsyncTask (NoteDao  dao) {
            mAsyncTaskDaoNote = dao;
        }

        @Override
        protected Void doInBackground(final TxtNote... params) {
            mAsyncTaskDaoNote.deleteTxtNote(params[0]);
            return null;
        }
    }
    public void deleteTxtNote(TxtNote txtNote){
        new NoteRepository.DeletetTxtNoteAsyncTask(mNoteDao).execute(txtNote);
    }
    //PictureNote methodes

    public void insertPictureNote(PictureNote note) {
        new NoteRepository.insertPictureNoteAsyncTask(mNoteDao).execute(note);
    }
    public void deletePictureNote(PictureNote pictureNote){
        new NoteRepository.DeletetPictureNoteAsyncTask(mNoteDao).execute(pictureNote);
    }

    public LiveData<List<PictureNote>> getAllPicturesNoteOfTrip(String idTrip) {
        try {
            return new GetAllPicturesNoteOfTripAsyncTask(mNoteDao).execute(idTrip).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<PictureNote> getPicturesNoteWithIDAndDateOrCity(String tripId, String value){
        try {
            return  new  GetPicturesNoteWithIDAndDateOrCityAsyncTask(mNoteDao).execute(tripId,value).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }



    public  PictureNote getPictureNoteWithId(String idNote){
        try {
            return new GetPictureNoteWithIDAsyncTask(mNoteDao).execute(idNote).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return  null;
    }
    private static class GetPictureNoteWithIDAsyncTask extends AsyncTask<String, Void, PictureNote> {

        private NoteDao mAsyncTaskDaoNote;
        GetPictureNoteWithIDAsyncTask (NoteDao noteDao) {

            mAsyncTaskDaoNote=noteDao;
        }

        @Override
        protected PictureNote doInBackground(final String... params) {

            return mAsyncTaskDaoNote.getPictureNoteWithId(params[0]);
        }
    }
    private static class DeletetPictureNoteAsyncTask extends AsyncTask<PictureNote,Void,Void> {
        private NoteDao mAsyncTaskDaoNote;
        DeletetPictureNoteAsyncTask (NoteDao  dao) {
            mAsyncTaskDaoNote = dao;
        }

        @Override
        protected Void doInBackground(final PictureNote... params) {
            mAsyncTaskDaoNote.deletePictureNote(params[0]);
            return null;
        }
    }

    private static class GetPicturesNoteWithIDAndDateOrCityAsyncTask extends AsyncTask<String, Void, List<PictureNote>> {

        private NoteDao mAsyncTaskDaoNote;
        GetPicturesNoteWithIDAndDateOrCityAsyncTask(NoteDao noteDao) {

            mAsyncTaskDaoNote=noteDao;
        }

        @Override
        protected List<PictureNote> doInBackground(final String... params) {

            return mAsyncTaskDaoNote.getPictureNoteWithIDAndDateOrCity(params[0],params[1]);
        }

    }
    private static class insertPictureNoteAsyncTask extends AsyncTask<PictureNote, Void, Void> {

        private NoteDao mAsyncTaskDaoNote;
        insertPictureNoteAsyncTask (NoteDao noteDao) {

            mAsyncTaskDaoNote=noteDao;
        }
        @Override
        protected Void doInBackground(final PictureNote... params) {

            mAsyncTaskDaoNote.insertImgNote(params[0]);
            return null;
        }

    }
    private static class GetAllPicturesNoteOfTripAsyncTask extends AsyncTask<String, Void, LiveData<List<PictureNote>>> {

        private NoteDao mAsyncTaskDaoNote;
        GetAllPicturesNoteOfTripAsyncTask(NoteDao noteDao) {

            mAsyncTaskDaoNote=noteDao;
        }

        @Override
        protected LiveData<List<PictureNote>> doInBackground(final String... params) {

            return mAsyncTaskDaoNote.getAllPicturesNoteOfTrip(params[0]);
        }

    }

}

