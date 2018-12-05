package de.thm.ap.ap_przb86_u1;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import de.thm.ap.ap_przb86_u1.model.Record;

@Dao
public interface ModuleDAO {
    @Query("DELETE FROM module")
    void deleteAll();

    @Insert
    void persistAll(Module[] modules);

    @Query("SELECT * FROM module WHERE id = :id")
    Module findById(Integer id);

    @Query("SELECT * FROM module")
    LiveData<List<Module>> findAll();
}
