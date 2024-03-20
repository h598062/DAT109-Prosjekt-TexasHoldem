package no.hvl.dat109.texasholdem.game;

import no.hvl.dat109.texasholdem.Enums.Status;
import no.hvl.dat109.texasholdem.Enums.Trekk;

public class Spiller {
    private Kort kort;
    private int chips;
    private String navn;
    private Status status;
    private Trekk trekk;

    public Spiller() {

    }

    private void dinTur() {

    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Trekk getTrekk() {
        return trekk;
    }

    public void setTrekk(Trekk trekk) {
        this.trekk = trekk;
    }
}
