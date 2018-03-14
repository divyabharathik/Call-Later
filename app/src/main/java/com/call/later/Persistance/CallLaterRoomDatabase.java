package com.call.later.Persistance;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.call.later.Model.CallEventItem;

@Database(entities = {CallEventItem.class}, version = 2, exportSchema = true)
public abstract class CallLaterRoomDatabase extends RoomDatabase {
    private static CallLaterRoomDatabase CALL_LATER_DB_INSTANCE;
    private static RoomDatabase.Callback dbCallback =
            new RoomDatabase.Callback() {
                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                    new PopulateDbAsync(CALL_LATER_DB_INSTANCE).execute();
                }
            };

    public static CallLaterRoomDatabase getDatabase(final Context context) {
        if (CALL_LATER_DB_INSTANCE == null) {
            synchronized (CallLaterRoomDatabase.class) {
                if (CALL_LATER_DB_INSTANCE == null) {
                    // Create database here
                    CALL_LATER_DB_INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            CallLaterRoomDatabase.class, "call_later_database")
                            .addCallback(dbCallback).fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return CALL_LATER_DB_INSTANCE;
    }

    public abstract CallLaterDao callLaterDao();

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final CallLaterDao mDao;

        PopulateDbAsync(CallLaterRoomDatabase db) {
            mDao = db.callLaterDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
//            mDao.deleteAll();
//            CallEventItem word = new CallEventItem("Hello");
//            mDao.insert(word);
//            word = new Word("World");
//            mDao.insert(word);
//            mDao.deleteAll();
//            CallEventItem callEventItem = new CallEventItem("dummy no", "dummy name", "dummy time");
//            mDao.insert(callEventItem);
            return null;
        }
    }
}
