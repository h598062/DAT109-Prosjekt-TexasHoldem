package no.hvl.dat109.texasholdem.game;

import no.hvl.dat109.texasholdem.enums.Trekk;
import no.hvl.dat109.texasholdem.service.LobbyMeldingService;
import no.hvl.dat109.texasholdem.websocket.message.GameStatusMessage;

import java.util.ArrayList;
import java.util.List;

public class TexasHoldemGame {
	private final LobbyMeldingService lms;
	private final String              lobbyId;

	private Spiller       spillerSinTur;
	private int           pott;
	private int           raiseTarget;
	private List<Spiller> ikkeGjortSineTrekk;
	private List<Spiller> ferdigMedRunde;
	private List<Spiller> allInSpillere;

	private Kortstokk kortstokk;

	private List<Kort> bordKort;

	private Round runde;

	private boolean erStartet;

	public TexasHoldemGame(LobbyMeldingService lms, String lobbyId, List<Spiller> spillere) {
		this.lms                = lms;
		this.lobbyId            = lobbyId;
		this.ikkeGjortSineTrekk = new ArrayList<>();
		ikkeGjortSineTrekk.addAll(spillere);
		erStartet = false;

		kortstokk  = new Kortstokk();
		this.runde = Round.PREFLOP;

		this.ferdigMedRunde = new ArrayList<>();
		this.allInSpillere  = new ArrayList<>();
		this.bordKort       = new ArrayList<>();
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
		if (!erStartet || !spillerSinTur.equals(spiller)) {
			return null;
		}
		if (mengde < raiseTarget) {
			return null; // Hvis spilleren raiser med mindre enn det allerede er raiset med
		}

		spiller.setChips(spiller.getChips() - mengde); // ta chips fra spiller
		pott += mengde; // legg til mengden i pott
		raiseTarget = mengde; // lagre hva som er den nye "målet" å calle til
		ikkeGjortSineTrekk.remove(spiller); // fjern denne spilleren fra ikkje gjort et trekk listen
		ikkeGjortSineTrekk.addAll(
				ferdigMedRunde); // alle de andre må nå calle den nye summen, legg de til i trekk listen på nytt
		ferdigMedRunde = List.of(spiller); // lag en ny ferdig med runde liste og legg til denne spilleren

		lms.sendTrekk(lobbyId, spiller.getNavn(), Trekk.RAISE, mengde);

		return velgNesteSpiller();
	}

	/**
	 * Metode for å calle
	 *
	 * @param spiller
	 *
	 * @return spiller
	 *
	 * @throws VinnerException
	 */
	public Spiller call(Spiller spiller) throws VinnerException {
		// Trenger kanskje enda en if sjekk for å sjekke all in dersom call er all in
		if (spiller.getChips() < raiseTarget) {
			return allIn(spiller);
		}

		spiller.setChips(spiller.getChips() - raiseTarget);
		pott += raiseTarget;

		ikkeGjortSineTrekk.remove(spiller);
		ferdigMedRunde.add(spiller);

		lms.sendTrekk(lobbyId, spiller.getNavn(), Trekk.CALL, 0);

		return velgNesteSpiller();
	}

	public Spiller check(Spiller spiller) throws VinnerException {
		ikkeGjortSineTrekk.remove(spiller);
		ferdigMedRunde.add(spiller);
		lms.sendTrekk(lobbyId, spiller.getNavn(), Trekk.CHECK, 0);
		return velgNesteSpiller();
	}

	public Spiller fold(Spiller spiller) throws VinnerException {
		spiller.emptyHand();
		ikkeGjortSineTrekk.remove(spiller);

		lms.sendTrekk(lobbyId, spiller.getNavn(), Trekk.FOLD, 0);

		return velgNesteSpiller();
	}

	public Spiller allIn(Spiller spiller) throws VinnerException {
		pott += spiller.getChips();
		spiller.setChips(0);

		ikkeGjortSineTrekk.remove(spiller);
		allInSpillere.add(spiller);

		lms.sendTrekk(lobbyId, spiller.getNavn(), Trekk.ALL_IN, 0);

		return velgNesteSpiller();
	}

	private Spiller velgNesteSpiller() throws VinnerException {
		// velg neste spiller fra ikkeGjortSineTrekkListe
		Spiller vinner = sjekkEnesteIgjen();
		if (vinner != null) {
			throw new VinnerException(vinner);
		}
		if (sjekkOmRundeErFerdig()) {
			nesteRunde();
		}
		spillerSinTur = ikkeGjortSineTrekk.get(0);
		lms.sendSpillStatus(lobbyId,
				new GameStatusMessage(lobbyId, ferdigMedRunde, ikkeGjortSineTrekk, allInSpillere, new ArrayList<>(),
						spillerSinTur, runde));
		return spillerSinTur;
	}


	public void nesteRunde() throws VinnerException {
		switch (runde) {
			case PREFLOP:
				runde = Round.FLOP;
				addCardToTable();
				addCardToTable();
				addCardToTable();
				ikkeGjortSineTrekk = ferdigMedRunde;
				ferdigMedRunde = new ArrayList<>();
				break;
			case FLOP:
				runde = Round.TURN;
				addCardToTable();
				ikkeGjortSineTrekk = ferdigMedRunde;
				ferdigMedRunde = new ArrayList<>();
				break;
			case TURN:
				runde = Round.RIVER;
				addCardToTable();
				ikkeGjortSineTrekk = ferdigMedRunde;
				ferdigMedRunde = new ArrayList<>();
				break;
			case RIVER:
				Spiller vinner = sjekkVinner();
				throw new VinnerException(vinner);
		}
		lms.sendBordKort(lobbyId, bordKort);
	}

	private Spiller sjekkVinner() {
		Spiller vinner      = null;
		Hand    hoyesteHand = null;
		for (Spiller spiller : ferdigMedRunde) {
			Hand hand = spiller.getHand();
			if (hoyesteHand == null || EvaluateCards.compareHand(hoyesteHand, hand) < 0) {
				hoyesteHand = hand;
				vinner      = spiller;
			}
		}
		return vinner;
	}

	private Spiller sjekkEnesteIgjen() {
		if (ikkeGjortSineTrekk.isEmpty() && (ferdigMedRunde.size() == 1 && allInSpillere.isEmpty()
		                                     || ferdigMedRunde.isEmpty() && allInSpillere.size() == 1)) {
			return ferdigMedRunde.get(0);
		}
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
		if (erStartet) {
			return null;
		}
		erStartet     = true;
		spillerSinTur = ikkeGjortSineTrekk.get(0); // velg den første i listen til å begynne
		dealCards();
		lms.sendSpillStatus(lobbyId,
				new GameStatusMessage(lobbyId, ferdigMedRunde, ikkeGjortSineTrekk, allInSpillere, new ArrayList<>(),
						spillerSinTur, runde));
		lms.sendKort(ikkeGjortSineTrekk, lobbyId);
		return spillerSinTur;
	}

	public enum Round {
		PREFLOP,
		FLOP,
		TURN,
		RIVER
	}
}
