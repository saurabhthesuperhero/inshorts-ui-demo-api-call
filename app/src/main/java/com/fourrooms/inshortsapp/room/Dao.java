package com.fourrooms.inshortsapp.room;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.fourrooms.inshortsapp.model.LocalTechModelRoom;

import java.util.ArrayList;
import java.util.List;

@androidx.room.Dao
public interface Dao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ArrayList<LocalTechModelRoom> model);

    @Update
    void update(ArrayList<LocalTechModelRoom> model);

    @Delete
    void delete(ArrayList<LocalTechModelRoom> model);

    @Query("DELETE FROM sample_table")
    void deleteAllData();

    @Query("SELECT * FROM sample_table")
    List<LocalTechModelRoom> getAllData();
}
