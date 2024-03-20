package no.hvl.dat109.texasholdem.game;

import no.hvl.dat109.texasholdem.Enums.Korttype;

import java.util.ArrayList;
import java.util.Collections;

public class Kortstokk {
    private ArrayList<Kort> kortstokk;

    /**
     * Konstruktør for Kortstokk klassen.
     * Lager 13 forskjellige kort for hver type Kort og stokker deretter kortstokken tilfeldig.
     * Korttype.values() brukes til å iterere over alle mulige typer Kort.
     */
    public Kortstokk() {
        kortstokk = new ArrayList<>();
        for (Korttype type : Korttype.values()) {
            for (int i = 1; i < 13; i++) {
                kortstokk.add(new Kort(type, i));
            }
        }
        Collections.shuffle(kortstokk);
    }

    /**
     * Returnerer og fjerner det siste kortet i kortstokken.
     * @return Det siste kortet i kortstokken.
     */
    public Kort trekKort() {
        return kortstokk.remove(kortstokk.size() - 1);
    }
}
