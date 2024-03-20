package no.hvl.dat109.texasholdem.game;

import no.hvl.dat109.texasholdem.Enums.Status;
import no.hvl.dat109.texasholdem.Enums.Trekk;

import java.util.Set;

public class Spiller {
    private Hand hand;
    private int chips;
    private String navn;
    private Status status;
    private Trekk trekk;

    public Spiller() {

    }

    /**
     * Trekker ett kort fra kortstokken og legger det til i hånden
     * @param ks (kortstokk)
     */
    public void drawCard(Kortstokk ks) {
        Kort kort = ks.trekKort();
        hand.addCard(kort);
    }

    private void dinTur() {

    }

    private void emptyHand() {
        hand.clear();
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
