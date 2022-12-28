package com.example.dbtestactual;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MusicDao {

    @Insert
    void insert(Track track);

    @Delete
    void delete(Track track);

    @Query("SELECT * FROM music_table WHERE title=:value")
    LiveData<List<Track>> getAllTrackByTitle(String value);

    @Query("SELECT * FROM music_table WHERE artist=:value")
    LiveData<List<Track>> getAllTrackByArtist(String value);

    @Query("SELECT * FROM music_table ORDER BY title ASC")
    LiveData<List<Track>> getAllTracksSorted();

    @Query("DELETE FROM music_table")
    void deleteAllTracks();


}
