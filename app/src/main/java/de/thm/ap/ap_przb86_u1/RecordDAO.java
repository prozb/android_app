package de.thm.ap.ap_przb86_u1;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import de.thm.ap.ap_przb86_u1.model.Record;

@Dao
public interface RecordDAO {
    @Query("SELECT * FROM record")
    LiveData<List<Record>> findAll();

    @Query("SELECT * FROM record WHERE id = :id")
    Record findById(Integer id);

    @Insert
    void persist(Record record);

    @Update
    void update(Record record);

    @Delete
    void delete(Record record);

    @Query("DELETE FROM record WHERE id = :id")
    void remove(Integer id);

    @Delete
    void deleteAll(Record ... records);
}
