package com.example.android.carnetDeVoyage.reposistory;



import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.android.carnetDeVoyage.dataBase.NoteDao;
import com.example.android.carnetDeVoyage.dataBase.TripDao;
import com.example.android.carnetDeVoyage.dataBase.TripRoomDatabase;
import com.example.android.carnetDeVoyage.entities.Trip;

import java.util.List;
import java.util.concurrent.ExecutionException;


public class TripRepository {

    private TripDao mTripDao;
    private LiveData<List<Trip>> mAllTrips;
    private TripRoomDatabase db;
    private  static TripRepository INSTANCE =null;
    public TripRepository(Application application) {
        db = TripRoomDatabase.getDatabase(application);
        mTripDao = db.TripDao();
        mAllTrips = mTripDao.getAlphabetizedTrips();
    }

    public static TripRepository getInstance(Application application){
        if (INSTANCE==null){
            INSTANCE=new TripRepository(application);
        }
        return INSTANCE;
    }
    public LiveData<List<Trip>> getAllTrips() {
        return mAllTrips;
    }

    public void insert(Trip trip) { new insertAsyncTask(mTripDao).execute(trip); }

    public void delete(Trip trip){new deletetAsyncTask(mTripDao).execute(trip);}
    public void update(Trip trip){new UpdateAsyncTask(mTripDao).execute(trip);}

    public Trip getTripWithId(String tripID){
        try {
            return new getTripWithIdAsyncTask(mTripDao).execute(tripID).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void clearDataBase(){ new DeletAllAsyncTask(db).execute(); }
    public List<Trip> getTripWithDateOrTitle(String dateOrTitle){
        try {
            return new GetTripWithDateOrTitleAsyncTask(mTripDao).execute(dateOrTitle).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
    private static class GetTripWithDateOrTitleAsyncTask extends AsyncTask<String, Void, List<Trip>> {

        private TripDao mAsyncTaskDaoTrip;
        GetTripWithDateOrTitleAsyncTask(TripDao tripDao) {

            mAsyncTaskDaoTrip=tripDao;
        }

        @Override
        protected List<Trip> doInBackground(final String... params) {

            return mAsyncTaskDaoTrip.getTripWithDateOrTitle(params[0]);

        }

    }
    private static class insertAsyncTask extends AsyncTask<Trip, Void, Void> {

        private TripDao mAsyncTaskDao;

        insertAsyncTask(TripDao dao) {
            mAsyncTaskDao = dao;

        }

        @Override
        protected Void doInBackground(final Trip... params) {
            mAsyncTaskDao.insert(params[0]);

            return null;
        }

    }

    private static class getTripWithIdAsyncTask extends AsyncTask<String, Void, Trip> {

        private TripDao mAsyncTaskDaoTrip;
        getTripWithIdAsyncTask(TripDao tripDao) {

            mAsyncTaskDaoTrip=tripDao;
        }

        @Override
        protected Trip doInBackground(final String... params) {

            return mAsyncTaskDaoTrip.findTripWithId(params[0]);

        }

    }
    private static class deletetAsyncTask extends AsyncTask<Trip,Void,  Void> {

        private TripDao mAsyncTaskDao;

        deletetAsyncTask(TripDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Trip... params) {
            mAsyncTaskDao.delete(params[0]);

            return null;
        }
    }
    private static class UpdateAsyncTask extends AsyncTask<Trip, Void, Void> {

        private TripDao mAsyncTaskDao;

        UpdateAsyncTask(TripDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Trip... params) {
            mAsyncTaskDao.update(params[0]);

            return null;
        }
    }
    private static class DeletAllAsyncTask extends AsyncTask<Void, Void, Void> {

        private final TripDao mDao;

        DeletAllAsyncTask(TripRoomDatabase db) {
            mDao = db.TripDao();

        }

        @Override
        protected Void doInBackground(final Void... params) {
            mDao.deleteAll();


            return null;
        }
    }

}
