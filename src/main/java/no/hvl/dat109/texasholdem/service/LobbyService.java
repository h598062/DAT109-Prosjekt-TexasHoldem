package no.hvl.dat109.texasholdem.service;

import no.hvl.dat109.texasholdem.game.Lobby;
import no.hvl.dat109.texasholdem.websocket.LobbyAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	Logger logger = LoggerFactory.getLogger(LobbyService.class);

	/**
	 * ingen direkte tilgang til lobbies hashmap utenfor denne klassen
	 */
	private final ConcurrentHashMap<String, Lobby> lobbies;

	/**
	 * Oppretter en ny lobby service.<br>
	 * Bruk @Autowired for å få en instans av denne klassen.
	 */
	public LobbyService() {
		this.lobbies = new ConcurrentHashMap<>();
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
			throw new LobbyAlreadyExistsException(String.format("Lobby with id %s already exists",lobbyId));
		}
		logger.info("Creating lobby with id {}", lobbyId);
		Lobby lobby = new Lobby(lobbyId, lobbyLeder);
		lobbies.put(lobbyId, lobby);
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
}
