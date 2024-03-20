package no.hvl.dat109.texasholdem.game;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "highscores" )
public class Highscore {


    @Id
    @Column(name = "spiller_navn", nullable = false)
    private String spillerNavn;

    @Column (name = "total_vunnet")
    private int totalVunnet;

    public Highscore() {

    }

    public Highscore(String spillerNavn, int totalVunnet) {
        this.spillerNavn = spillerNavn;
        this.totalVunnet = totalVunnet;
    }

    public String getSpillerNavn() {
        return spillerNavn;
    }

    public void setSpillerNavn(String spillerNavn) {
        this.spillerNavn = spillerNavn;
    }

    public int getTotalVunnet() {
        return totalVunnet;
    }

    public void setTotalVunnet(int totalVunnet) {
        this.totalVunnet = totalVunnet;
    }


}
