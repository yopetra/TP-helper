package com.example.android.tp_helper.data;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ArticleEntry.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private final static String LOG_TAG = AppDatabase.class.getSimpleName();
    private final static Object LOCK = new Object();
    private final static String DATABASE_NAME = "articleslist";
    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context){
        if(sInstance == null){
            synchronized (LOCK){
                Log.d(LOG_TAG, "Create a new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .build();
            }
        }

        Log.d(LOG_TAG, "Getting database instance");
        return sInstance;
    }

    public abstract ArticleDao articleDao();
}
