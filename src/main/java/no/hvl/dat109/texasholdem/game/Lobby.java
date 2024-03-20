package no.hvl.dat109.texasholdem.game;

import java.util.ArrayList;

public class Lobby {
    private ArrayList<Spiller> spillere;
    private Kortstokk kortstokk;

    public Lobby() {
        spillere = new ArrayList<>();
        kortstokk = new Kortstokk();
    }

    /**
     * Dealer kort til hver spiller som er i listen.
     */
    public void dealCards() {
        for (Spiller spiller : spillere) {
            spiller.drawCard(kortstokk);
            spiller.drawCard(kortstokk);
        }
    }

    /**
     * Legger til en spiller til spillere listen
     * @param spiller
     */
    public void leggTilSpillere(Spiller spiller) {
        spillere.add(spiller);
    }

    /**
     * Fjerner en spiller fra spillere listen
     * @param spiller
     */
    public void fjernSpiller(Spiller spiller) {
        spillere.remove(spiller);
    }


    public ArrayList<Spiller> getSpillere() {
        return this.spillere;
    }
    
    public void setSpillere(ArrayList<Spiller> spillere) {
        this.spillere = spillere;
    }
}
