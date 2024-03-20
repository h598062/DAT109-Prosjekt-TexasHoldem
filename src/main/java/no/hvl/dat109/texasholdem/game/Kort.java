package no.hvl.dat109.texasholdem.game;

import no.hvl.dat109.texasholdem.Enums.Korttype;

public class Kort {
    private final static int ANTALL_KORT = 52;
    private Korttype korttype;
    private int verdi;

    public Kort() {

    }
    public Kort(Korttype korttype, int verdi) {
        this.korttype = korttype;
        this.verdi = verdi;
    }

    public int getVerdi() {
        return verdi;
    }

    public void setVerdi(int verdi) {
        this.verdi = verdi;
    }

    public Korttype getKorttype() {
        return korttype;
    }

    public void setKorttype(Korttype korttype) {
        this.korttype = korttype;
    }
}
