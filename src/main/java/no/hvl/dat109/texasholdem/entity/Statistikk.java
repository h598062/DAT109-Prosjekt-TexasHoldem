package no.hvl.dat109.texasholdem.entity;

import jakarta.persistence.*;
import no.hvl.dat109.texasholdem.Enums.HaandKombinasjon;


@Entity
@Table(schema ="TexasHoldem", name = "statistikk")
public class Statistikk {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int runde;
    @Column(name="antallSpilteHender", length = 40)
    private int antallSpilteHender;
    @Column(name="antallVunnetHender", length = 40)
    private int antallVunnetHender;
    @Column(name="antallTapteHender", length = 40)
    private int antallTapteHender;
    @Enumerated(EnumType.STRING)
    @Column(name="hoyesteHaand", length = 40)
    private HaandKombinasjon hoyesteHaand;
    @Column(name="totaltVunnetBelop", length = 40)
    private double totaltVunnetBelop;
    @Column(name="totaltTaptBelop", length = 40)
    private double totaltTaptBelop;
    @Column(name="storstePottVunnet", length = 40)

    private double storstePottVunnet;
    @Column(name="storstPottVunnet", length = 40)
    private double storstPottVunnet;



    public Statistikk() {

    }

    public Statistikk(int runde, int antallSpilteHender, int antallVunnetHender, int antallTapteHender,
                      HaandKombinasjon hoyesteHaand, double totaltVunnetBelop, double totaltTaptBelop,
                      double storstePottVunnet, double storstPottVunnet) {
        this.runde = runde;
        this.antallSpilteHender = antallSpilteHender;
        this.antallVunnetHender = antallVunnetHender;
        this.antallTapteHender = antallTapteHender;
        this.hoyesteHaand = hoyesteHaand;
        this.totaltVunnetBelop = totaltVunnetBelop;
        this.totaltTaptBelop = totaltTaptBelop;
        this.storstePottVunnet = storstePottVunnet;
        this.storstPottVunnet = storstPottVunnet;
    }

    public int getRunde() {
        return runde;
    }

    public void setRunde(int runde) {
        this.runde = runde;
    }

    public int getAntallSpilteHender() {
        return antallSpilteHender;
    }

    public void setAntallSpilteHender(int antallSpilteHender) {
        this.antallSpilteHender = antallSpilteHender;
    }

    public int getAntallVunnetHender() {
        return antallVunnetHender;
    }

    public void setAntallVunnetHender(int antallVunnetHender) {
        this.antallVunnetHender = antallVunnetHender;
    }

    public int getAntallTapteHender() {
        return antallTapteHender;
    }

    public void setAntallTapteHender(int antallTapteHender) {
        this.antallTapteHender = antallTapteHender;
    }

    public HaandKombinasjon getHoyesteHaand() {
        return hoyesteHaand;
    }

    public void setHoyesteHaand(HaandKombinasjon hoyesteHaand) {
        this.hoyesteHaand = hoyesteHaand;
    }

    public double getTotaltVunnetBelop() {
        return totaltVunnetBelop;
    }

    public void setTotaltVunnetBelop(double totaltVunnetBelop) {
        this.totaltVunnetBelop = totaltVunnetBelop;
    }

    public double getTotaltTaptBelop() {
        return totaltTaptBelop;
    }

    public void setTotaltTaptBelop(double totaltTaptBelop) {
        this.totaltTaptBelop = totaltTaptBelop;
    }

    public double getStorstePottVunnet() {
        return storstePottVunnet;
    }

    public void setStorstePottVunnet(double storstePottVunnet) {
        this.storstePottVunnet = storstePottVunnet;
    }

    public double getStorstPottVunnet() {
        return storstPottVunnet;
    }

    public void setStorstPottVunnet(double storstPottVunnet) {
        this.storstPottVunnet = storstPottVunnet;
    }
}
