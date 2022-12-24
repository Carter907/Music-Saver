package com.example.dbtestactual;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities ={Track.class}, version=1, exportSchema = false)
public abstract class MusicDatabase extends RoomDatabase {

    public abstract MusicDao getDao();

    public enum Instance {
        DB_INSTANCE;

        private MusicDatabase database;

        public MusicDatabase getDatabase(Context context) {
            if (database == null) {
                database = Room.databaseBuilder(context, MusicDatabase.class, "music_table")
                        .addCallback(operations)
                        .build();
            }
            return database;
        }

    }

    static ExecutorService service = Executors.newFixedThreadPool(4);

    private static RoomDatabase.Callback operations = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

        }
    };





}
