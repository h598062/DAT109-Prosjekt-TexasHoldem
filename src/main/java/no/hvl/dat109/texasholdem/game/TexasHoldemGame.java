package no.hvl.dat109.texasholdem.game;

import java.util.ArrayList;
import java.util.List;

public class TexasHoldemGame {
	private Spiller spillerSinTur;
	private int pott;
	private int raiseTarget;
	private List<Spiller> ikkeGjortSineTrekk;
	private List<Spiller> ferdigMedRunde;

	private boolean erStartet;

	public TexasHoldemGame(List<Spiller> spillere) {
		this.ikkeGjortSineTrekk = new ArrayList<>();
		ikkeGjortSineTrekk.addAll(spillere);
		erStartet = false;
	}

	public void raise(Spiller spiller, int mengde) {

		// hvis det ikke er denne spilleren sin tur eller hvis spillet ikke er startet, avbryt
		if (!erStartet || !spillerSinTur.equals(spiller)) return;

		spiller.setChips(spiller.getChips() - mengde); // ta chips fra spiller
		pott += mengde; // legg til mengden i pott
		raiseTarget = mengde; // lagre hva som er den nye "målet" å calle til
		ikkeGjortSineTrekk.remove(spiller); // fjern denne spilleren fra ikkje gjort et trekk listen
		ikkeGjortSineTrekk.addAll(ferdigMedRunde); // alle de andre må nå calle den nye summen, legg de til i trekk listen på nytt
		ferdigMedRunde = List.of(spiller); // lag en ny ferdig med runde liste og legg til denne spilleren

		velgNesteSpiller();
	}

	private void velgNesteSpiller() {
		// velg neste spiller fra ikkeGjortSineTrekkListe
		spillerSinTur = ikkeGjortSineTrekk.getFirst();
	}

	public void call(Spiller spiller) {
		// TODO: Gjør denne
	}

	public void gjørRunde() {

	}

	public Spiller getSpillerSinTur() {
		return spillerSinTur;
	}

	public void setSpillerSinTur(Spiller spillerSinTur) {
		this.spillerSinTur = spillerSinTur;
	}

	public boolean sjekkOmRundeErFerdig() {
		return ikkeGjortSineTrekk.isEmpty();
	}

	public void startSpill() {
		if (erStartet) return;
		erStartet = true;
		spillerSinTur = ikkeGjortSineTrekk.getFirst(); // velg den første i listen til å begynne
	}
}
