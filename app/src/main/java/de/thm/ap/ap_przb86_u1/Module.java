package de.thm.ap.ap_przb86_u1;

import android.arch.persistence.room.Entity;

@Entity
public class Module {
    private String nr;
    private String name;
    private int crp;

    public Module(String nr, String name, int crp) {
        this.nr = nr;
        this.name = name;
        this.crp = crp;
    }

    public int getCrp() {
        return crp;
    }

    public void setCrp(int crp) {
        this.crp = crp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNr() {
        return nr;
    }

    public void setNr(String nr) {
        this.nr = nr;
    }
}
