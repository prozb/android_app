package de.thm.ap.ap_przb86_u1.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Record implements Serializable {
    @PrimaryKey
    private Integer id;

    private String moduleNum;
    private String moduleName;
    private boolean summerTerm;
    private boolean isHalfWeighted;
    private Integer credits;
    private Integer mark;
    private Integer year;

    public Record(){

    }


    public Record(String moduleNumber, String moduleName, Integer year, boolean summerTerm, boolean isHalfWeighted, Integer credits, Integer mark){
        this.moduleNum    = moduleNumber;
        this.moduleName   = moduleName;
        this.summerTerm   = summerTerm;
        this.isHalfWeighted = isHalfWeighted;
        this.credits      = credits;
        this.mark         = mark;
        this.year         = year;
    }


    @Override
    public String toString() {
        String result = "";

        if(moduleName != null){
            result += (moduleName + " ");
        }
        if(moduleNum != null){
            result += (moduleNum.toUpperCase() + " ");
        }

        result += ("(" + mark + "% ");
        result += credits + "crp)";

        return result;
    }

    public void setId(Integer id){
        this.id = id;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public void setModuleNum(String moduleNum) {
        this.moduleNum = moduleNum;
    }

    public void setSummerTerm(boolean summerTerm) {
        this.summerTerm = summerTerm;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public void setMark(Integer note) {
        this.mark = note;
    }

    public Integer getId() {
        return id;
    }

    public String getModuleName() {
        return moduleName;
    }

    public boolean isSummerTerm() {
        return summerTerm;
    }

    public Integer getCredits() {
        return credits;
    }

    public Integer getMark() {
        return mark;
    }

    public boolean isHalfWeighted() {
        return isHalfWeighted;
    }

    public void setIsHalfWeighted(boolean halfWeighted) {
        this.isHalfWeighted = halfWeighted;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getModuleNum() {
        return moduleNum;
    }
}
