package com.fourrooms.inshortsapp.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.fourrooms.inshortsapp.model.LocalTechModelRoom;


@Database(entities = {LocalTechModelRoom.class}, version = 2)
@TypeConverters({converters.class})
public abstract class SimpleDatabase extends RoomDatabase {
    public abstract Dao Dao();
}
