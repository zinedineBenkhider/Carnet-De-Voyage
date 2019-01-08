package com.example.android.carnetDeVoyage.reposistory;
import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import com.example.android.carnetDeVoyage.dataBase.TagDao;
import com.example.android.carnetDeVoyage.dataBase.TripRoomDatabase;
import com.example.android.carnetDeVoyage.entities.TagTxtNote;
import com.example.android.carnetDeVoyage.entities.TagPictureNote;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class TagReposistory {
    private TagDao mTagDao;
    private LiveData<List<TagTxtNote>> mAllTags;
    private TripRoomDatabase db;
    private  static TagReposistory INSTANCE =null;

    public TagReposistory(Application application) {
        db = TripRoomDatabase.getDatabase(application);
        mTagDao=db.TagDao();
        mAllTags= mTagDao.getAllTags();
    }

    public static TagReposistory  getInstance(Application application){
        if (INSTANCE==null){
            INSTANCE=new TagReposistory(application);
        }
        return INSTANCE;
    }

    public void insertTag(TagTxtNote tag) {
        new TagReposistory.InsertTagAsyncTask(mTagDao).execute(tag);
    }
    public List<TagTxtNote>  getTagsWithIdNoteAndTag(String idNote, String tag){
        try {
            return new GetTagsWithIdNoteAndTagAsyncTask(mTagDao).execute(idNote,tag).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class GetTagsWithIdNoteAndTagAsyncTask extends AsyncTask<String, Void, List<TagTxtNote> > {

        private TagDao mAsyncTaskDaoTag;
        GetTagsWithIdNoteAndTagAsyncTask (TagDao noteDao) {

            mAsyncTaskDaoTag=noteDao;
        }
        @Override
        protected List<TagTxtNote> doInBackground(final String... params) {

            return mAsyncTaskDaoTag.getTagTxtNoteWithIdNoteAndTag(params[0],params[1]);

        }

    }

    public List<TagTxtNote>  getTagsWithIdNote(String idNote){
        try {
            return new GetTagsWithIdNoteAsyncTask(mTagDao).execute(idNote).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class GetTagsWithIdNoteAsyncTask extends AsyncTask<String, Void, List<TagTxtNote> > {

        private TagDao mAsyncTaskDaoTag;
        GetTagsWithIdNoteAsyncTask (TagDao noteDao) {

            mAsyncTaskDaoTag=noteDao;
        }
        @Override
        protected List<TagTxtNote> doInBackground(final String... params) {

            return mAsyncTaskDaoTag.getTagsWithIdNote(params[0]);

        }

    }
    private static class InsertTagAsyncTask extends AsyncTask<TagTxtNote, Void, Void> {
        private TagDao mAsyncTaskDaoTag;
        InsertTagAsyncTask (TagDao noteDao) {
            mAsyncTaskDaoTag=noteDao;
        }
        @Override
        protected Void doInBackground(final TagTxtNote... params) {
            mAsyncTaskDaoTag.insertTag(params[0]);
            return null;
        }

    }
    // TagsPictureNote methodes
    ///
    ///
    ///
    ///

    public void insertTagPictureNote(TagPictureNote tag) {
        new TagReposistory.InsertTagPictureNoteAsyncTask(mTagDao).execute(tag);

    }
    private static class InsertTagPictureNoteAsyncTask extends AsyncTask<TagPictureNote, Void, Void> {
        private TagDao mAsyncTaskDaoTag;
        InsertTagPictureNoteAsyncTask  (TagDao noteDao) {
            mAsyncTaskDaoTag=noteDao;
        }
        @Override
        protected Void doInBackground(final TagPictureNote... params) {
            mAsyncTaskDaoTag.insertTagPictureNote(params[0]);
            return null;
        }
    }
    private static class GetTagPictureNoteWithIdNoteAsyncTask extends AsyncTask<String, Void, List<TagPictureNote> > {

        private TagDao mAsyncTaskDaoTag;
        GetTagPictureNoteWithIdNoteAsyncTask (TagDao noteDao) {

            mAsyncTaskDaoTag=noteDao;
        }
        @Override
        protected List<TagPictureNote> doInBackground(final String... params) {

            return mAsyncTaskDaoTag.getTagsPictureNoteWithIdNote(params[0]);

        }

    }
    public List<TagPictureNote>  getTagsPictureNoteWithIdNote(String idNote){
        try {
            return new GetTagPictureNoteWithIdNoteAsyncTask(mTagDao).execute(idNote).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
    public List<TagPictureNote>  getTagsPictureWithIdNoteAndTag(String idNote,String tag){
        try {
            return new GetTagsPictureNoteWithIdNoteAndTagAsyncTask(mTagDao).execute(idNote,tag).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class GetTagsPictureNoteWithIdNoteAndTagAsyncTask extends AsyncTask<String, Void, List<TagPictureNote> > {

        private TagDao mAsyncTaskDaoTag;
        GetTagsPictureNoteWithIdNoteAndTagAsyncTask (TagDao noteDao) {

            mAsyncTaskDaoTag=noteDao;
        }
        @Override
        protected List<TagPictureNote> doInBackground(final String... params) {

            return mAsyncTaskDaoTag.getTagPictureNoteWithIdNoteAndTag(params[0],params[1]);

        }

    }
}
