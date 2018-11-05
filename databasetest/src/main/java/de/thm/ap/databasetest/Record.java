package de.thm.ap.databasetest;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Record {
    @PrimaryKey
    public int id;

    public int score;
    public int level;
    public String name;

    public Record(int id, int score, int level, String name){
        this.id    = id;
        this.score = score;
        this.level = level;
        this.name  = name;
    }

    @Override
    public String toString() {
        return "record: " + score + " score " + level + " level " + name;
     }
}
