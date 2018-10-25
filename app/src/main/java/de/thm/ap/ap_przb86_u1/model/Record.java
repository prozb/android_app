package de.thm.ap.ap_przb86_u1.model;

import java.io.Serializable;

public class Record implements Serializable {
    private int id;
    private String modulNummer;
    private String modulName;
    private boolean summerTerm;
    private boolean halfWeighted;
    private int credits;
    private int note;
    private int year;

    public Record(){

    }

    public Record(String modulNummer, String modulName, int year, boolean summerTerm, boolean halfWeighted, int credits, int note){
        this.modulNummer  = modulNummer;
        this.modulName    = modulName;
        this.summerTerm       = summerTerm;
        this.halfWeighted = halfWeighted;
        this.credits      = credits;
        this.note         = note;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setModuleName(String modulName) {
        this.modulName = modulName;
    }

    public void setSummerTerm(boolean summerTerm) {
        this.summerTerm = summerTerm;
    }

    public void setCrp(int credits) {
        this.credits = credits;
    }

    public void setMark(int note) {
        this.note = note;
    }

    public Integer getId() {
        return id;
    }

    public String getModuleName() {
        return modulName;
    }

    public boolean isSummerTerm() {
        return summerTerm;
    }

    public int getCrp() {
        return credits;
    }

    public int getMark() {
        return note;
    }

    public boolean isHalfWeighted() {
        return halfWeighted;
    }

    public void setHalfWeighted(boolean halfWeighted) {
        this.halfWeighted = halfWeighted;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getModuleNum() {
        return modulNummer;
    }
}
