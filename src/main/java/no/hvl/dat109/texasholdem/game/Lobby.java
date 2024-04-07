package no.hvl.dat109.texasholdem.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Lobby {
	private final String lobbyId;
	private final ConcurrentHashMap<String, Spiller> spillere;
	private final Spiller lobbyLeder;
	Logger logger = LoggerFactory.getLogger(Lobby.class);
	private TexasHoldemGame game;

	public Lobby(String lobbyId, String lobbyLederNavn) {
		this.lobbyId = lobbyId;
		this.spillere = new ConcurrentHashMap<>();
		this.lobbyLeder = new Spiller(lobbyLederNavn);
		spillere.put(lobbyLederNavn, this.lobbyLeder);
	}


	/**
	 * Metode skal kalles når spillet i lobbyen skal startes.
	 */
	public Spiller start() {
		// TODO: Ferdigstill start-sekvens
		game = new TexasHoldemGame((List<Spiller>) spillere.values());
		return game.startSpill();
	}

	public String getLobbyId() {
		return lobbyId;
	}

	public synchronized void leggTilSpiller(Spiller spiller) {
		spillere.put(spiller.getNavn(), spiller);
	}

	public synchronized void fjernSpiller(Spiller spiller) {
		if (spiller.equals(lobbyLeder)) {
			logger.warn("Lobbyleder kan ikke fjernes fra lobbyen");
			return;
		}
		spillere.remove(spiller.getNavn());
	}

	public synchronized boolean erSpillerMed(String navn) {
		return spillere.containsKey(navn);
	}

	/**
	 * Henter en spiller fra lobbyen
	 *
	 * @param navn navnet på spilleren
	 *
	 * @return spilleren hvis den finnes, null ellers
	 */
	public synchronized Spiller getSpiller(String navn) {
		return spillere.get(navn);
	}

	public Spiller getLobbyLeder() {
		return lobbyLeder;
	}

	/**
	 * Returnerer en liste med navn på alle spillere i denne lobbyen
	 *
	 * @return liste med navn på alle spillere i denne lobbyen
	 */
	public synchronized List<String> getSpillernesNavn() {
		ArrayList<String> list = Collections.list(spillere.keys());
		logger.info("spillere {}", list);
		return list;
	}

	public TexasHoldemGame getGame() {
		return game;
	}

	public void setGame(TexasHoldemGame game) {
		this.game = game;
	}
}
