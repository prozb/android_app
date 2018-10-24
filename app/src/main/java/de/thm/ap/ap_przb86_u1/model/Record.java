package de.thm.ap.ap_przb86_u1.model;

public class Record {
    private String modulNummer;
    private String modulName;
    private boolean ss15;
    private boolean fuenfzigProc;
    private int credits;
    private int note;

    public Record(){

    }

    public Record(String modulNummer, String modulName, boolean ss15, boolean fuenfzigProc, int credits, int note){
        this.modulNummer  = modulNummer;
        this.modulName    = modulName;
        this.ss15         = ss15;
        this.fuenfzigProc = fuenfzigProc;
        this.credits      = credits;
        this.note         = note;
    }

    public void setModulNummer(String modulNummer) {
        this.modulNummer = modulNummer;
    }

    public void setModulName(String modulName) {
        this.modulName = modulName;
    }

    public void setSs15(boolean ss15) {
        this.ss15 = ss15;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public void setNote(int note) {
        this.note = note;
    }

    public String getModulNummer() {
        return modulNummer;
    }

    public String getModulName() {
        return modulName;
    }

    public boolean isSs15() {
        return ss15;
    }

    public int getCredits() {
        return credits;
    }

    public int getNote() {
        return note;
    }

    public boolean isFuenfzigProc() {
        return fuenfzigProc;
    }

    public void setFuenfzigProc(boolean fuenfzigProc) {
        this.fuenfzigProc = fuenfzigProc;
    }
}
