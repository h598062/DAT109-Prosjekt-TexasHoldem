package no.hvl.dat109.texasholdem.service;

import no.hvl.dat109.texasholdem.enums.Action;
import no.hvl.dat109.texasholdem.enums.Trekk;
import no.hvl.dat109.texasholdem.game.Lobby;
import no.hvl.dat109.texasholdem.game.Spiller;
import no.hvl.dat109.texasholdem.websocket.LobbyAlreadyExistsException;
import no.hvl.dat109.texasholdem.websocket.message.LobbyActionMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Lobby servicen håndterer alt med oppretting, sletting og henting av lobbyer.<br>
 * Denne klassen er en singleton og blir opprettet av Spring.<br>
 * Hvis du skal legge den til flere steder, bruk @Autowired for å få samme instans.
 */
@Service
public class LobbyService {
	/**
	 * ingen direkte tilgang til lobbies hashmap utenfor denne klassen
	 */
	private final ConcurrentHashMap<String, Lobby> lobbies;
	private final SpillerMeldingService bms;
	Logger logger = LoggerFactory.getLogger(LobbyService.class);

	/**
	 * Oppretter en ny lobby service.<br>
	 * Bruk @Autowired for å få en instans av denne klassen.
	 */
	@Autowired
	public LobbyService(SpillerMeldingService bms) {
		this.lobbies = new ConcurrentHashMap<>();
		this.bms = bms;
	}

	/**
	 * Sjekker om en lobby med gitt id eksisterer
	 *
	 * @param lobbyId id til lobbyen
	 *
	 * @return true hvis lobbyen eksisterer, false ellers
	 */
	public boolean lobbyExists(String lobbyId) {
		return lobbies.containsKey(lobbyId);
	}

	/**
	 * Oppretter en ny lobby med gitt id og lobbyleder.
	 *
	 * @param lobbyId    id (navn) til lobbyen
	 * @param lobbyLeder navn (id) til lobbyleder. en spiller blir opprettet for lederen automatisk.
	 *
	 * @return lobbyen som ble opprettet
	 *
	 * @throws LobbyAlreadyExistsException hvis lobbyen allerede eksisterer
	 */
	public Lobby createLobby(String lobbyId, String lobbyLeder) throws LobbyAlreadyExistsException {
		if (lobbyExists(lobbyId)) {
			logger.error("Lobby with id {} already exists", lobbyId);
			throw new LobbyAlreadyExistsException(String.format("Lobby with id %s already exists", lobbyId));
		}
		logger.info("Creating lobby with id {}", lobbyId);
		Lobby lobby = new Lobby(lobbyId, lobbyLeder);
		lobbies.put(lobbyId, lobby);
		return lobby;
	}

	/**
	 * Tar imot et trekk fra en spiller, sjekker om det er mulig / lov etc. og deretter utfører trekket<br>
	 * Hvis det ikke går, sendes det en feilmelding / beskjed til spilleren via BrukerMeldingService
	 *
	 * @param spillerNavn Spilleren som utfører trekket
	 * @param trekk       Trekket spilleren utfører
	 * @param mengde      Mengden chips for å utføre trekket (hvis det trengs)
	 *
	 * @return Boolean True om det gikk, false om det ikke gikk
	 */
	public boolean doTrekk(String lobbyId, String spillerNavn, Trekk trekk, int mengde) {
		// TODO: lag og/eller finn metoder etc for å hente de under
		// finn lobby

		// hent ut game

		// finn spiller

		// TODO end

		switch (trekk) {
			case CALL:
				logger.info("Spiller {} har callet i lobbyen {}", spillerNavn, lobbyId);
				logger.error("CALL er ikke implementert");
				// TODO: Implementer call
				// send feilmelding med bms.sendMelding() hvis det ikke gikk (ikke din tur etc.)
				break;
			case CHECK:
				// muligens slå sammen CALL og CHECK? begge "godtar" nåværende sum på bordet
				logger.info("Spiller {} har checket i lobbyen {}", spillerNavn, lobbyId);
				logger.error("CHECK er ikke implementert");
				// TODO: Implementer check
				// send feilmelding med bms.sendMelding() hvis det ikke gikk (ikke din tur etc.)
				break;
			case FOLD:
				logger.info("Spiller {} har foldet i lobbyen {}", spillerNavn, lobbyId);
				logger.error("FOLD er ikke implementert");
				// TODO: Implementer fold
				// send feilmelding med bms.sendMelding() hvis det ikke gikk (ikke din tur etc.)
				break;
			case RAISE:
				logger.info("Spiller {} har raiset med {} i lobbyen {}", spillerNavn, mengde, lobbyId);
				logger.error("RAISE er ikke implementert");
				// TODO: Implementer raise
				// send feilmelding med bms.sendMelding() hvis det ikke gikk (ikke din tur etc.)
				break;
			case ALL_IN:
				logger.info("Spiller {} har gått ALL INN i lobbyen {}", spillerNavn, lobbyId);
				logger.error("ALL INN er ikke implementert");
				// TODO Implementer all in
				// send feilmelding med bms.sendMelding() hvis det ikke gikk (ikke din tur etc.)
				break;
		}
		return false;
	}

	/**
	 * Sjekker om spilleren eksisterer i lobbyen<br>
	 *
	 * @param spillerNavn Spilleren som skal finnes
	 * @param lobby       Lobbyen spilleren er i
	 *
	 * @return spilleren som meldingen refererer til
	 */
	private Spiller finnSpiller(String spillerNavn, Lobby lobby) {
		Spiller spiller = lobby.getSpiller(spillerNavn);
		if (spiller == null) {
			logger.warn("Spiller {} does not exist in lobby {}", spillerNavn, lobby.getLobbyId());
			bms.sendMelding(spillerNavn,
			                String.format("Spiller %s finnes ikke i lobby %s", spillerNavn, lobby.getLobbyId()));
			throw new IllegalArgumentException("Spiller does not exist in lobby");
		}
		return spiller;
	}

	/**
	 * Sjekker om lobbyId er gyldig og om lobbyen eksisterer<br>
	 *
	 * @param spillerNavn Navnet på spiller (for feilmelding)
	 * @param lobbyId     lobbyId til lobby som skal finnes
	 *
	 * @return lobbyen som meldingen refererer til
	 */
	private Lobby finnLobby(String spillerNavn, String lobbyId) {
		if (lobbyId == null || lobbyId.isBlank()) {
			logger.warn("LobbyId is missing or blank in message from: {}", spillerNavn);
			bms.sendMelding(spillerNavn, "Melding mangler lobbyId");
			throw new IllegalArgumentException("LobbyId is missing or blank");
		}
		logger.info("lobbyer: {}", getLobbies());
		Lobby lobby = getLobby(lobbyId);
		if (lobby == null) {
			logger.warn("Lobby {} does not exist", lobbyId);
			bms.sendMelding(spillerNavn, String.format("Lobby %s finnes ikke", lobbyId));
			throw new IllegalArgumentException("Lobby does not exist");
		}
		return lobby;
	}


	/**
	 * Henter en lobby med gitt id
	 *
	 * @param lobbyId id til lobbyen
	 *
	 * @return lobbyen med gitt id, eller null hvis lobbyen ikke eksisterer
	 */
	public Lobby getLobby(String lobbyId) {
		return lobbies.get(lobbyId);
	}

	/**
	 * Fjerner en lobby med gitt id
	 *
	 * @param lobbyId id til lobbyen
	 *
	 * @return lobbyen som ble fjernet, eller null hvis lobbyen ikke eksisterer
	 */
	public Lobby removeLobby(String lobbyId) {
		logger.info("Removing lobby with id {}", lobbyId);
		return lobbies.remove(lobbyId);
	}

	/**
	 * Henter en liste over alle lobby id-er
	 *
	 * @return en liste over alle lobby id-er
	 */
	public List<String> getLobbies() {
		return Collections.list(lobbies.keys());
	}

	public boolean doAction(String lobbyId, String spillerNavn, Action action) {
		Lobby lobby = finnLobby(spillerNavn, lobbyId);
		Spiller spiller = finnSpiller(spillerNavn, lobby);
		boolean suksess = false;
		switch (action) {
			case JOIN:
				logger.info("Spiller {} har joinet lobbyen {}", spillerNavn, lobbyId);
				logger.error("JOIN er ikke ferdig implementert");
				// TODO: Ferdigstill join implementasjon
				lobby.leggTilSpiller(spiller);
				suksess = true;
				break;
			case LEAVE:
				logger.info("Spiller {} har forlatt lobbyen {} ", spillerNavn, lobbyId);
				logger.error("LEAVE er ikke implementert");
				// TODO: Implementer leave
				break;
			case AFK:
				logger.info("Spiller {} er AFK i lobbyen {} ", spillerNavn, lobbyId);
				logger.error("AFK er ikke implementert");
				// TODO: Implementer AFK
				break;
			case READY:
				logger.info("Spiller {} er klar i lobbyen {} ", spillerNavn, lobbyId);
				logger.error("READY er ikke implementert");
				// TODO: Implementer ready
				break;
			case UNREADY:
				logger.info("Spiller {} er ikke klar i lobbyen {} ", spillerNavn, lobbyId);
				logger.error("UNREADY er ikke implementert");
				// TODO: Implementer unready
				break;
			case DISCONNECT:
				logger.info("Spiller {} har blitt disconnected i lobbyen {} ", spillerNavn, lobbyId);
				logger.error("DISCONNECT er ikke implementert");
				// TODO: Implementer disconnect
				break;
			case START:
				logger.info("Spiller {} prøver å starte spillet i lobbyen {} ", spillerNavn, lobbyId);
				logger.error("START er ikke implementert");
				// TODO: Gjør ferdig start implementasjon
				break;
			case END:
				logger.info("Spiller {} prøver å stoppe spillet i lobbyen {} ", spillerNavn, lobbyId);
				logger.error("END er ikke implementert");
				// TODO: Implementer end
				break;
		}
		return suksess;
	}

	public List<String> getNavneListe(String lobbyId) {
		Lobby lobby = getLobby(lobbyId);
		return lobby.getSpillernesNavn();
	}
}
