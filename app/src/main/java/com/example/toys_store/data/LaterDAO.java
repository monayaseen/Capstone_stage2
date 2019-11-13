package com.example.toys_store.data;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface LaterDAO {

    @Query("SELECT * FROM Later_on")
    LiveData<List<Toy>> loadLater();

    @Insert
    void insertToy(Toy e);

    @Delete
    void deleteToy(Toy e);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateToy(Toy e);

    @Query("SELECT * FROM Later_on WHERE name = :id")
    LiveData<Toy> loadBookById(int id);
}