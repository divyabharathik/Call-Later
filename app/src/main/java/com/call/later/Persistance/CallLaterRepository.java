package com.call.later.Persistance;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.call.later.Model.CallEventItem;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class CallLaterRepository {
    private CallLaterDao callLaterDao;
    private LiveData<List<CallEventItem>> allEventsLiveData;

    public CallLaterRepository(Application application) {
        CallLaterRoomDatabase db = CallLaterRoomDatabase.getDatabase(application);
        callLaterDao = db.callLaterDao();
        allEventsLiveData = callLaterDao.getAllEvents();
    }

    public LiveData<List<CallEventItem>> getAllEventsLiveData() {
        return allEventsLiveData;
    }


    public long insert(CallEventItem callEventItem) {
        try {
            return new insertAsyncTask(callLaterDao).execute(callEventItem).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return -1L;
        }

    }

    public void delete(CallEventItem callEventItem) {
        new deleteAsyncTask(callLaterDao).execute(callEventItem);
    }

    public void deleteSpecificItem(long id) {
        new deleteSpecificAsyncTask(callLaterDao).execute();
    }

    public void updateSpecificItem(long id) {
        new updateAsyncTask(callLaterDao).execute();
    }
    public CallEventItem getSpecificItem(long id){
        try {
            return new getSpecificAsyncTask(callLaterDao).execute(id).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class insertAsyncTask extends AsyncTask<CallEventItem, Void, Long> {

        private CallLaterDao mAsyncTaskDao;

        insertAsyncTask(CallLaterDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Long doInBackground(final CallEventItem... params) {
            return mAsyncTaskDao.insert(params[0]);
        }
    }

    private static class deleteAsyncTask extends AsyncTask<CallEventItem, Void, Void> {

        private CallLaterDao mAsyncTaskDao;

        deleteAsyncTask(CallLaterDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final CallEventItem... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }

    private static class updateAsyncTask extends AsyncTask<CallEventItem, Void, Void> {

        private CallLaterDao mAsyncTaskDao;

        updateAsyncTask(CallLaterDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final CallEventItem... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }

    private static class deleteSpecificAsyncTask extends AsyncTask<Integer, Void, Void> {

        private CallLaterDao mAsyncTaskDao;

        deleteSpecificAsyncTask(CallLaterDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            mAsyncTaskDao.deleteItemForGivenId(integers[0]);
            return null;
        }
    }

    private static class getSpecificAsyncTask extends AsyncTask<Long, Void, CallEventItem> {

        private CallLaterDao mAsyncTaskDao;

        getSpecificAsyncTask(CallLaterDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected CallEventItem doInBackground(Long... longs) {
            mAsyncTaskDao.getItemForGivenId(longs[0]);
            return null;
        }
    }


}
