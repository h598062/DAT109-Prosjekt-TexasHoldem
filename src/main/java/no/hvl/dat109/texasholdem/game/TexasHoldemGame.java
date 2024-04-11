package no.hvl.dat109.texasholdem.game;

import no.hvl.dat109.texasholdem.enums.Status;
import no.hvl.dat109.texasholdem.enums.Trekk;
import no.hvl.dat109.texasholdem.service.LobbyMeldingService;
import no.hvl.dat109.texasholdem.websocket.message.GameStatusMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class TexasHoldemGame {
	private static final Logger              logger = LoggerFactory.getLogger(TexasHoldemGame.class);
	private final        LobbyMeldingService lms;
	private final        String              lobbyId;

	private Spiller       spillerSinTur;
	private int           pott;
	private int           raiseTarget;
	private List<Spiller> spillere;

	private Kortstokk kortstokk;

	private List<Kort> bordKort;

	private Round runde;

	private boolean erStartet;

	public TexasHoldemGame(LobbyMeldingService lms, String lobbyId, List<Spiller> spillere) {
		this.lms      = lms;
		this.lobbyId  = lobbyId;
		this.spillere = new ArrayList<>();
		this.spillere.addAll(spillere);

		erStartet = false;

		raiseTarget = 0;

		kortstokk     = new Kortstokk();
		this.runde    = Round.PREFLOP;
		this.bordKort = new ArrayList<>();
	}

	/**
	 * Dealer kort til hver spiller som er i listen.
	 */
	public void dealCards() {
		spillere.forEach(s -> {
			s.drawCard(kortstokk);
			s.drawCard(kortstokk);
		});
	}

	public void addCardToTable() {
		bordKort.add(kortstokk.trekKort());
	}

	/**
	 * Raise i spillet.
	 * Når en spiller har raiset, og en annen prøver å raise igien vil den nye raise mengden være den forrige + den nye.
	 * Hvis spiller 1 raiser med 20, og spiller 2 rå raiser med 20, vil dette da føre til at spiller to har raiset med 40.
	 * Da må alle andre spillere calle 40 for å være med videre, mens spiller 1 må calle 20.
	 *
	 * @param spiller
	 * @param nyRaiseMengde
	 *
	 * @return
	 *
	 * @throws VinnerException
	 */
	public Spiller raise(Spiller spiller, int nyRaiseMengde) throws VinnerException {

		// hvis det ikke er denne spilleren sin tur eller hvis spillet ikke er startet, avbryt
		if (!erStartet || !spillerSinTur.equals(spiller)) {
			return null;
		}

		if (nyRaiseMengde <= 0) {
			return null;
		}

		if (spiller.getChips() <= 0) {
			return null;
		}

		if (raiseTarget == 0) {
			if (spiller.getChips() < nyRaiseMengde) {
				return null;
			} else {
				raiseTarget = nyRaiseMengde;
				spiller.setChips(spiller.getChips() - nyRaiseMengde);
				pott += nyRaiseMengde;
				spiller.setCurrentBet(nyRaiseMengde);
			}
		} else {
			if (spiller.getCurrentBet() == 0) {
				if (spiller.getChips() < nyRaiseMengde + raiseTarget) {
					return null;
				} else {
					spiller.setChips(spiller.getChips() - nyRaiseMengde - raiseTarget);
					pott += nyRaiseMengde + raiseTarget;
					raiseTarget += nyRaiseMengde;
					spiller.setCurrentBet(nyRaiseMengde + raiseTarget);
				}
			} else {
				if (spiller.getChips() < nyRaiseMengde + raiseTarget - spiller.getCurrentBet()) {
					return null;
				} else {
					spiller.setChips(spiller.getChips() - nyRaiseMengde - raiseTarget + spiller.getCurrentBet());
					pott += nyRaiseMengde + raiseTarget - spiller.getCurrentBet();
					raiseTarget += nyRaiseMengde;
					spiller.setCurrentBet(nyRaiseMengde + raiseTarget);
				}
			}
		}

		spillere.stream().filter(s -> !s.equals(spiller) && s.getStatus().equals(Status.DONE))
		        .forEach(s -> s.setStatus(Status.WAITING));
		spiller.setStatus(Status.DONE);

		lms.sendTrekk(lobbyId, spiller.getNavn(), Trekk.RAISE, nyRaiseMengde);

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
		if (!erStartet || !spillerSinTur.equals(spiller)) {
			return null;
		}

		// Trenger kanskje enda en if sjekk for å sjekke all in dersom call er all in
		if (spiller.getChips() <= raiseTarget - spiller.getCurrentBet()) {
			return allIn(spiller);
		}

		if (spiller.getCurrentBet() == 0) {
			spiller.setChips(spiller.getChips() - raiseTarget);
			pott += raiseTarget;
			spiller.setCurrentBet(raiseTarget);
		} else {
			spiller.setChips(spiller.getChips() - raiseTarget + spiller.getCurrentBet());
			pott += raiseTarget - spiller.getCurrentBet();
			spiller.setCurrentBet(raiseTarget);
		}

		spiller.setStatus(Status.DONE);
		logger.info("Spillere: {}", spillere);

		lms.sendTrekk(lobbyId, spiller.getNavn(), Trekk.CALL, 0);

		return velgNesteSpiller();
	}

	public Spiller check(Spiller spiller) throws VinnerException {
		if (!erStartet || !spillerSinTur.equals(spiller)) {
			return null;
		}

		if (spiller.getCurrentBet() != raiseTarget) {
			return null;
		}

		spiller.setStatus(Status.DONE);
		logger.info("Spillere: {}", spillere);

		lms.sendTrekk(lobbyId, spiller.getNavn(), Trekk.CHECK, 0);
		return velgNesteSpiller();
	}

	public Spiller fold(Spiller spiller) throws VinnerException {
		if (!erStartet || !spillerSinTur.equals(spiller)) {
			return null;
		}

		spiller.emptyHand();
		spiller.setStatus(Status.FOLD);
		logger.info("Spillere: {}", spillere);

		lms.sendTrekk(lobbyId, spiller.getNavn(), Trekk.FOLD, 0);

		return velgNesteSpiller();
	}

	public Spiller allIn(Spiller spiller) throws VinnerException {
		if (!erStartet || !spillerSinTur.equals(spiller)) {
			return null;
		}
		if (spiller.getChips() <= 0) {
			return null;
		}

		int allInnMengde = spiller.getChips();
		spiller.setChips(0);
		if (allInnMengde >= raiseTarget) {
			raiseTarget = allInnMengde;
			pott += allInnMengde;
			spiller.setCurrentBet(allInnMengde);
		} else {
			pott += allInnMengde;
			spiller.setCurrentBet(allInnMengde);
		}

		spillere.stream().filter(s -> !s.equals(spiller) && s.getStatus().equals(Status.DONE))
		        .forEach(s -> s.setStatus(Status.WAITING));

		spiller.setStatus(Status.ALLIN);

		lms.sendTrekk(lobbyId, spiller.getNavn(), Trekk.ALL_IN, 0);

		return velgNesteSpiller();
	}

	public Spiller velgNesteSpiller() throws VinnerException {
		// velg neste spiller fra ikkeGjortSineTrekkListe
		Spiller vinner = sjekkEnesteIgjen();
		if (vinner != null) {
			throw new VinnerException(vinner);
		}
		if (sjekkOmRundeErFerdig()) {
			nesteRunde();
		}
		spillerSinTur = spillere.stream().filter(s -> s.getStatus().equals(Status.WAITING))
		                        .findFirst().orElse(null);
		lms.sendSpillStatus(lobbyId,
				new GameStatusMessage(lobbyId, spillere,
						spillerSinTur, runde, pott, raiseTarget));
		return spillerSinTur;
	}


	public void nesteRunde() throws VinnerException {
		raiseTarget = 0;
		spillere.stream().filter(s -> s.getStatus().equals(Status.DONE))
		        .forEach(s -> {
			        s.setStatus(Status.WAITING);
					s.setCurrentBet(0);
		        });
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
		lms.sendBordKort(lobbyId, bordKort);
	}

	private Spiller sjekkVinner() {
		Spiller vinner      = null;
		Hand    hoyesteHand = null;
		for (Spiller spiller : spillere) {
			if (spiller.getStatus().equals(Status.FOLD)) {
				continue;
			}
			Hand completeHand = new Hand();
			completeHand.getHand().addAll(spiller.getHand().getHand());
			completeHand.getHand().addAll(bordKort);


			if (hoyesteHand == null || EvaluateCards.compareHand(hoyesteHand, completeHand) < 0) {
				hoyesteHand = completeHand;
				vinner      = spiller;
			}
		}
		return vinner;
	}

	private Spiller sjekkEnesteIgjen() {
		Spiller vinner = null;
		for (Spiller spiller : spillere) {
			if (spiller.getStatus().equals(Status.FOLD)) {
				continue;
			}
			if (vinner != null) {
				return null;
			}
			vinner = spiller;
		}
		return vinner;
	}

	public Spiller getSpillerSinTur() {
		return spillerSinTur;
	}

	public void setSpillerSinTur(Spiller spillerSinTur) {
		this.spillerSinTur = spillerSinTur;
	}

	public boolean sjekkOmRundeErFerdig() {
		int antallDone = 0;
		for (Spiller spiller : spillere) {
			if (!spiller.getStatus().equals(Status.WAITING)) {
				antallDone++;
			}
		}
		return antallDone == spillere.size();
	}

	public Spiller startSpill() {
		if (erStartet) {
			return null;
		}
		erStartet     = true;
		spillerSinTur = spillere.get(0); // velg den første i listen til å begynne
		dealCards();
		lms.sendSpillStatus(lobbyId,
				new GameStatusMessage(lobbyId, spillere,
						spillerSinTur, runde, pott, raiseTarget));
		lms.sendKort(spillere, lobbyId);
		return spillerSinTur;
	}

	public enum Round {
		PREFLOP,
		FLOP,
		TURN,
		RIVER
	}
}
