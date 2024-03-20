package no.hvl.dat109.texasholdem.game;

import java.util.ArrayList;

public class Lobby {
    private ArrayList<Spiller> spillere;

    public Lobby() {
        spillere = new ArrayList<>();
    }

    public void leggTilSpillere(Spiller spiller) {
        spillere.add(spiller);
    }

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
