package de.thm.ap.ap_przb86_u1;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import de.thm.ap.ap_przb86_u1.model.Record;

@Database(entities = {Record.class, Module.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;
    public abstract RecordDAO recordDAO();
    public abstract ModuleDAO moduleDAO();

    public static AppDatabase getDb(Context ctx){
        if(INSTANCE == null){
            Log.i("AppDatabase", "Creating app database");
            INSTANCE = Room.databaseBuilder(ctx.getApplicationContext(), AppDatabase.class, "records_menu-db")
                    .build();
        }
        return INSTANCE;
    }
}
