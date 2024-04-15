package no.hvl.dat109.texasholdem.service;

import no.hvl.dat109.texasholdem.enums.Action;
import no.hvl.dat109.texasholdem.enums.Status;
import no.hvl.dat109.texasholdem.enums.Trekk;
import no.hvl.dat109.texasholdem.game.Lobby;
import no.hvl.dat109.texasholdem.game.Spiller;
import no.hvl.dat109.texasholdem.game.TexasHoldemGame;
import no.hvl.dat109.texasholdem.websocket.LobbyAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
	@Autowired
	private final LobbyMeldingService              lms;
	@Autowired
	private final SpillerMeldingService            sms;
	/**
	 * ingen direkte tilgang til lobbies hashmap utenfor denne klassen
	 */
	private final ConcurrentHashMap<String, Lobby> lobbies;
	Logger logger = LoggerFactory.getLogger(LobbyService.class);

	/**
	 * Oppretter en ny lobby service.<br>
	 * Bruk @Autowired for å få en instans av denne klassen.
	 */
	public LobbyService(SpillerMeldingService sms, LobbyMeldingService lms) {
		this.lobbies = new ConcurrentHashMap<>();
		this.sms     = sms;
		this.lms     = lms;
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
		Lobby lobby = new Lobby(lms, lobbyId, lobbyLeder);
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
	public void doTrekk(String lobbyId, String spillerNavn, Trekk trekk, int mengde) {
		Lobby           lobby   = finnLobby(spillerNavn, lobbyId);
		TexasHoldemGame game    = lobby.getGame();
		Spiller         spiller = finnSpiller(spillerNavn, lobby);

		// Returnererer null for å ikke få feilmelding når noen caller før game er startet
		if (game == null) {
			return;
		}

		if (game.getSpillerSinTur() == null) {
			logger.info("Ingen spillere skal gjøre et trekk i lobby {}, fortsetter med runden", lobbyId);
			game.velgNesteSpiller();
			return;
		}
		switch (trekk) {
			case CALL:
				logger.info("Spiller {} har callet i lobbyen {}", spillerNavn, lobbyId);
				game.call(spiller);
				// send feilmelding med sms.sendMelding() hvis det ikke gikk (ikke din tur etc.)
				break;
			case CHECK:
				// muligens slå sammen CALL og CHECK? begge "godtar" nåværende sum på bordet
				logger.info("Spiller {} har checket i lobbyen {}", spillerNavn, lobbyId);
				game.check(spiller);
				// send feilmelding med sms.sendMelding() hvis det ikke gikk (ikke din tur etc.)
				break;
			case FOLD:
				logger.info("Spiller {} har foldet i lobbyen {}", spillerNavn, lobbyId);
				game.fold(spiller);
				// send feilmelding med sms.sendMelding() hvis det ikke gikk (ikke din tur etc.)
				break;
			case RAISE:
				logger.info("Spiller {} har raiset med {} i lobbyen {}", spillerNavn, mengde, lobbyId);
				// send feilmelding med sms.sendMelding() hvis det ikke gikk (ikke din tur etc.)
				game.raise(spiller, mengde);
				break;
			case ALL_IN:
				logger.info("Spiller {} har gått ALL INN i lobbyen {}", spillerNavn, lobbyId);
				game.allIn(spiller);
				// send feilmelding med sms.sendMelding() hvis det ikke gikk (ikke din tur etc.)
				break;
		}
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
			sms.sendMelding(spillerNavn,
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
			sms.sendMelding(spillerNavn, "Melding mangler lobbyId");
			throw new IllegalArgumentException("LobbyId is missing or blank");
		}
		logger.info("lobbyer: {}", getLobbies());
		Lobby lobby = getLobby(lobbyId);
		if (lobby == null) {
			logger.warn("Lobby {} does not exist", lobbyId);
			sms.sendMelding(spillerNavn, String.format("Lobby %s finnes ikke", lobbyId));
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
		Lobby           lobby   = finnLobby(spillerNavn, lobbyId);
		TexasHoldemGame game    = lobby.getGame();
		Spiller         spiller = finnSpiller(spillerNavn, lobby);
		boolean         suksess = false;
		switch (action) {
			case JOIN:
				logger.info("Spiller {} har joinet lobbyen {}", spillerNavn, lobbyId);

				lobby.leggTilSpiller(spiller);
				lms.sendAction(lobbyId, lobby.getSpillere(), spillerNavn, Action.JOIN);
				suksess = true;
				break;
			case LEAVE:
				logger.info("Spiller {} har forlatt lobbyen {} ", spillerNavn, lobbyId);

				// Fjerner spilleren fra lobbyen
				game.fjernSpiller(spiller);
				lobby.fjernSpiller(spiller);
				lms.sendAction(lobbyId, lobby.getSpillere(), spillerNavn, Action.LEAVE);
				suksess = true;
				break;
			case AFK:
				logger.info("Spiller {} er AFK i lobbyen {} ", spillerNavn, lobbyId);
				logger.error("AFK er ikke implementert");
				// TODO: Implementer AFK
				// ikke viktig for første demo
				break;
			case READY:
				logger.info("Spiller {} er klar i lobbyen {} ", spillerNavn, lobbyId);
				logger.error("READY er ikke implementert");
				// TODO: Implementer ready
				// ikke viktig for første demo
				break;
			case UNREADY:
				logger.info("Spiller {} er ikke klar i lobbyen {} ", spillerNavn, lobbyId);
				logger.error("UNREADY er ikke implementert");
				// TODO: Implementer unready
				// ikke viktig for første demo
				break;
			case DISCONNECT:
				logger.info("Spiller {} har blitt disconnected i lobbyen {} ", spillerNavn, lobbyId);
				logger.error("DISCONNECT er ikke implementert");
				// TODO: Implementer disconnect
				// ikke viktig for første demo
				break;
			case START:
				logger.info("Spiller {} prøver å starte spillet i lobbyen {} ", spillerNavn, lobbyId);
				if (!spillerNavn.equals(lobby.getLobbyLeder().getNavn())) {
					logger.warn("Spiller {} er ikke lobbyleder og kan ikke starte spillet", spillerNavn);
					sms.sendMelding(spillerNavn, "{\"msg\":\"Du er ikke lobbyleder og kan ikke starte spillet\"}");
					break;
				}
				// her må det opprettes et nytt TexasHoldemGame objekt og lagre det i lobbyen
				game = new TexasHoldemGame(lms, lobbyId, new ArrayList<>(lobby.getSpillere()));
				lobby.setGame(game);
				lms.sendAction(lobbyId, lobby.getSpillere(), spillerNavn, Action.START);
				game.startSpill();
				suksess = true;
				break;
			case END:
				logger.info("Spiller {} prøver å stoppe spillet i lobbyen {} ", spillerNavn, lobbyId);
				if (!lobby.getSpillere().contains(lobby.getLobbyLeder())) {
					logger.warn("Lobbyleder er ikke lenger i lobbyen, spillet blir avsluttet");
					sms.sendMelding(spillerNavn,
							"{\"msg\":\"Lobbyleder er ikke lenger i lobbyen, spillet avsluttes...\"}");
					lobbies.remove(lobbyId);
					lms.sendAction(lobbyId, lobby.getSpillere(), spillerNavn, Action.END);
					suksess = true;
					break;
				}
				if (!spillerNavn.equals(lobby.getLobbyLeder().getNavn())) {
					logger.warn("Spiller {} er ikke lobbyleder og kan ikke avslutte spillet", spillerNavn);
					sms.sendMelding(spillerNavn, "{\"msg\":\"Du er ikke lobbyleder og kan ikke avslutte spillet\"}");
					break;
				}
				// fjern denne lobbyen fra lobby listen
				lobbies.remove(lobbyId);
				lms.sendAction(lobbyId, lobby.getSpillere(), spillerNavn, Action.END);
				suksess = true;
				break;
			case RESTART:
				logger.info("Spiller {} prøver å restarte spillet i lobbyen {} ", spillerNavn, lobbyId);
				if (!spillerNavn.equals(lobby.getLobbyLeder().getNavn())) {
					logger.warn("Spiller {} er ikke lobbyleder og kan ikke restarte spillet", spillerNavn);
					sms.sendMelding(spillerNavn, "{\"msg\":\"Du er ikke lobbyleder og kan ikke restarte spillet\"}");
					break;
				}
				if (game.isErFerdig()) {
					lobby.getSpillere().forEach(s -> {
						s.emptyHand();
						spiller.setStatus(Status.WAITING);
					});
					game = new TexasHoldemGame(lms, lobbyId, new ArrayList<>(lobby.getSpillere()));
					lobby.setGame(game);
					lms.sendAction(lobbyId, lobby.getSpillere(), spillerNavn, Action.RESTART);
					game.startSpill();
					suksess = true;
				} else {
					logger.warn("Spillet i lobby {} er ikke ferdig og kan ikke restartes", lobbyId);
					sms.sendMelding(spillerNavn, "{\"msg\": \"Spillet er ikke ferdig og kan ikke restartes\"}");
				}
				break;
		}
		return suksess;
	}
}
