package no.hvl.dat109.texasholdem.game;

import java.util.ArrayList;
import java.util.List;

public class TexasHoldemGame {
	private Spiller spillerSinTur;
	private int pott;
	private int raiseTarget;
	private List<Spiller> ikkeGjortSineTrekk;
	private List<Spiller> ferdigMedRunde;

	private Kortstokk kortstokk;

	private List<Kort> bordKort;

	private Round runde;

	private boolean erStartet;

	public TexasHoldemGame(List<Spiller> spillere) {
		this.ikkeGjortSineTrekk = new ArrayList<>();
		ikkeGjortSineTrekk.addAll(spillere);
		erStartet = false;

		kortstokk = new Kortstokk();
		this.runde = Round.PREFLOP;
	}

	/**
	 * Dealer kort til hver spiller som er i listen.
	 */
	public void dealCards() {
		ikkeGjortSineTrekk.forEach(s -> {
			s.drawCard(kortstokk);
			s.drawCard(kortstokk);
		});
	}

	public void addCardToTable() {
		bordKort.add(kortstokk.trekKort());
	}

	public Spiller raise(Spiller spiller, int mengde) throws VinnerException {

		// hvis det ikke er denne spilleren sin tur eller hvis spillet ikke er startet, avbryt
		if (!erStartet || !spillerSinTur.equals(spiller)) return null;

		spiller.setChips(spiller.getChips() - mengde); // ta chips fra spiller
		pott += mengde; // legg til mengden i pott
		raiseTarget = mengde; // lagre hva som er den nye "målet" å calle til
		ikkeGjortSineTrekk.remove(spiller); // fjern denne spilleren fra ikkje gjort et trekk listen
		ikkeGjortSineTrekk.addAll(ferdigMedRunde); // alle de andre må nå calle den nye summen, legg de til i trekk listen på nytt
		ferdigMedRunde = List.of(spiller); // lag en ny ferdig med runde liste og legg til denne spilleren

		return velgNesteSpiller();
	}

	public Spiller call(Spiller spiller) throws VinnerException {
		// TODO: Gjør denne
		return velgNesteSpiller();
	}

	public Spiller check(Spiller spiller) throws VinnerException {
		// TODO: Gjør denne
		return velgNesteSpiller();
	}

	public Spiller fold(Spiller spiller) throws VinnerException {
		// TODO: Gjør denne
		return velgNesteSpiller();
	}

	public Spiller allIn(Spiller spiller) throws VinnerException {
		// TODO: Gjør denne
		return velgNesteSpiller();
	}

	private Spiller velgNesteSpiller() throws VinnerException {
		// velg neste spiller fra ikkeGjortSineTrekkListe
		if (sjekkOmRundeErFerdig()) {
			nesteRunde();
		}
		spillerSinTur = ikkeGjortSineTrekk.get(0);
		return spillerSinTur;
	}

	public void nesteRunde() throws VinnerException {
		switch (runde) {
			case PREFLOP:
				runde = Round.FLOP;
				addCardToTable();
				addCardToTable();
				addCardToTable();
				break;
			case FLOP:
				runde = Round.TURN;
				addCardToTable();
				break;
			case TURN:
				runde = Round.RIVER;
				addCardToTable();
				break;
			case RIVER:
				Spiller vinner = sjekkVinner();
				throw new VinnerException(vinner);
		}
	}

	private Spiller sjekkVinner() {
		// TODO: Gjør denne
		return null;
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

	public Spiller startSpill() {
		if (erStartet) return null;
		erStartet = true;
		spillerSinTur = ikkeGjortSineTrekk.get(0); // velg den første i listen til å begynne
		dealCards();
		return spillerSinTur;
	}

	private enum Round {
		PREFLOP,
		FLOP,
		TURN,
		RIVER
	}
}
