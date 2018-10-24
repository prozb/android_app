package de.thm.ap.ap_przb86_u1.model;

public class Record {
    private String modulNummer;
    private String modulName;
    private boolean isSoSe;
    private boolean fuenfzigProc;
    private int credits;
    private int note;

    public Record(){

    }

    public Record(String modulNummer, String modulName, boolean isSoSe, boolean fuenfzigProc, int credits, int note){
        this.modulNummer  = modulNummer;
        this.modulName    = modulName;
        this.isSoSe       = isSoSe;
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
        this.isSoSe = isSoSe;
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
        return isSoSe;
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
