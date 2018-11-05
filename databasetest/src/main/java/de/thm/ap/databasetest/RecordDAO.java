package de.thm.ap.databasetest;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface RecordDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addRecord(Record record);

    @Query("select * from record")
    List<Record> getAllRecords();

    @Query("delete from record")
    void removeAllRecords();
}
