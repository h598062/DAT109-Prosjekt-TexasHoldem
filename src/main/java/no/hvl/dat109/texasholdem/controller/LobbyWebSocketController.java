package no.hvl.dat109.texasholdem.controller;

import no.hvl.dat109.texasholdem.enums.Action;
import no.hvl.dat109.texasholdem.game.Lobby;
import no.hvl.dat109.texasholdem.game.Spiller;
import no.hvl.dat109.texasholdem.service.LobbyService;
import no.hvl.dat109.texasholdem.websocket.message.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;

/**
 * WebSocket controller for lobbyer<br>
 * Håndterer meldinger fra spillerne og sender oppdatert status til alle i lobbyen<br>
 * Meldinger fra klienter sendes til /lobby/*lobbyid* og servermeldinger broadcastes til /lobby/status/*lobbyid*<br>
 * Meldinger som sendes til /lobby/status/*lobbyid* blir broadcastet til alle abonnenter på samme lobbyid<br>
 * Server sender også meldinger til spillerne sine egne topics /spiller/*spillernavn* hvis det skjer noe feil etc med deres meldinger<br>
 * Husk at alle inkommende requests blir håndtert i en egen tråd, så pass på concurrency og synkronisering<br>
 * <br>
 * Se {@link no.hvl.dat109.texasholdem.websocket.WebSocketConfig} for konfigurasjon av WebSocket endpoints
 */
@Controller
public class LobbyWebSocketController {
	private static final Logger logger = LoggerFactory.getLogger(LobbyWebSocketController.class);

	private final LobbyService lobbyService;

	private final SimpMessagingTemplate messagingTemplate;

	/**
	 * Konstruktør for Controlleren<br>
	 * Autowirer lobbyService og messagingTemplate i konstruktøren som er den gode måten å gjøre det på
	 *
	 * @param lobbyService      service for lobbyen
	 * @param messagingTemplate template for å sende meldinger
	 */
	@Autowired
	public LobbyWebSocketController(LobbyService lobbyService, SimpMessagingTemplate messagingTemplate) {
		this.lobbyService = lobbyService;
		this.messagingTemplate = messagingTemplate;
	}

	/**
	 * Sjekker om meldingen er ugyldig
	 *
	 * @param message meldingen som skal sjekkes
	 *
	 * @return true hvis meldingen er ugyldig, false ellers
	 */
	private boolean erUgyldigMelding(SpillerMessage message) {
		if (message == null) {
			logger.warn("Message is null");
			return true;
		}
		if (message.getSpillerNavn() == null || message.getSpillerNavn().isBlank()) {
			logger.warn("SpillerNavn is missing or blank: {}", message);
			return true;
		}
		return false;
	}

	/**
	 * Sender en melding til en spiller sin egen topic.<br>
	 * Metoden formatterer navnet til en gyldig URI før den sender meldingen.<br>
	 * Antar at navnet er gyldig, sjekk først med f.eks. {@link #erUgyldigMelding(SpillerMessage)}
	 *
	 * @param spillerNavn navnet på spilleren, må være sjekket først
	 * @param message     meldingen som skal sendes
	 */
	private void sendBrukerMelding(String spillerNavn, String message) {
		messagingTemplate.convertAndSend("/spiller/" + UriUtils.encode(spillerNavn, StandardCharsets.UTF_8), message);
	}

	/**
	 * Sjekker om lobbyId er gyldig og om lobbyen eksisterer<br>
	 * Antar at meldingen er gyldig, sjekk først med {@link #erUgyldigMelding(SpillerMessage)}
	 *
	 * @param spillerNavn meldingen som skal sjekkes
	 * @param lobbyId     lobbyId som skal sjekkes
	 *
	 * @return lobbyen som meldingen refererer til
	 */
	private Lobby sjekkLobby(String spillerNavn, String lobbyId) {
		if (lobbyId == null || lobbyId.isBlank()) {
			logger.warn("LobbyId is missing or blank in message from: {}", spillerNavn);
			sendBrukerMelding(spillerNavn, "Melding mangler lobbyId");
			throw new IllegalArgumentException("LobbyId is missing or blank");
		}
		logger.info("lobbyer: {}", lobbyService.getLobbies());
		Lobby lobby = lobbyService.getLobby(lobbyId);
		if (lobby == null) {
			logger.warn("Lobby {} does not exist", lobbyId);
			sendBrukerMelding(spillerNavn, String.format("Lobby %s finnes ikke", lobbyId));
			throw new IllegalArgumentException("Lobby does not exist");
		}
		return lobby;
	}

	/**
	 * Sjekker om spilleren eksisterer i lobbyen<br>
	 * Antar at meldingen er gyldig, sjekk først med {@link #erUgyldigMelding(SpillerMessage)}
	 *
	 * @param spillerNavn meldingen som skal sjekkes
	 * @param lobby       lobbyen som meldingen refererer til
	 *
	 * @return spilleren som meldingen refererer til
	 */
	private Spiller sjekkSpiller(String spillerNavn, Lobby lobby) {
		Spiller spiller = lobby.getSpiller(spillerNavn);
		if (spiller == null) {
			logger.warn("Spiller {} does not exist in lobby {}", spillerNavn, lobby.getLobbyId());
			sendBrukerMelding(spillerNavn,
			                  String.format("Spiller %s finnes ikke i lobby %s", spillerNavn, lobby.getLobbyId()));
			throw new IllegalArgumentException("Spiller does not exist in lobby");
		}
		return spiller;
	}

	/**
	 * Håndterer en trekk-melding fra en spiller i en lobby<br>
	 * Server oppdaterer game-state og sender en oppdatert status til alle i lobbyen<br>
	 *
	 * @param lobbyId lobbyId som meldingen refererer til, denne hentes dynamisk fra path i request
	 * @param message meldingen som skal håndteres, Spring parser denne fra JSON til SpillerTrekkMessage
	 *
	 * @return melding som skal broadcastes til alle i lobbyen, eller null hvis ingenting skal oppdateres
	 */
	@MessageMapping("/trekk/{lobbyId}")
	@SendTo("/lobbystatus/{lobbyId}")
	public LobbyTrekkMessage lobbyTrekkHandler(@DestinationVariable String lobbyId,
	                                           @Payload SpillerTrekkMessage message) {
		logger.info("Received SpillerTrekkMessage: {}", message);
		if (lobbyId == null || lobbyId.isBlank()) {
			logger.warn("LobbyId is missing or blank in message: {}", message);
			return null;
		}
		if (erUgyldigMelding(message)) {
			logger.warn("Invalid message: {}", message);
			return null;
		}
		Lobby lobby;
		Spiller spiller;
		try {
			lobby = sjekkLobby(message.getSpillerNavn(), lobbyId);
			spiller = sjekkSpiller(message.getSpillerNavn(), lobby);
		} catch (IllegalArgumentException e) {
			return null;
		}
		switch (message.getTrekk()) {
			case RAISE:
				logger.info("Spiller {} har raiset med {} i lobbyen {}", spiller.getNavn(), message.getMengde(),
				            lobbyId);
				logger.error("RAISE er ikke implementert");
				// TODO: Implementer raise
				break;
			case CALL:
				logger.info("Spiller {} har callet i lobbyen {}", spiller.getNavn(), lobbyId);
				logger.error("CALL er ikke implementert");
				// TODO: Implementer call
				break;
			case FOLD:
				logger.info("Spiller {} har foldet i lobbyen {}", spiller.getNavn(), lobbyId);
				logger.error("FOLD er ikke implementert");
				// TODO: Implementer fold
				break;
		}
		return null;
	}

	/**
	 * Håndterer en lobby-action-melding fra en spiller i en lobby<br>
	 * Server oppdaterer lobby-state og sender en oppdatert status til alle i lobbyen<br>
	 * Denne metoden oppdaterer spiller status etc i lobbyen<br>
	 *
	 * @param lobbyId lobbyId som meldingen refererer til, denne hentes dynamisk fra path i request
	 * @param message meldingen som skal håndteres, Spring parser denne fra JSON til SpillerActionMessage
	 *
	 * @return melding som skal broadcastes til alle i lobbyen, eller null hvis ingenting skal oppdateres
	 */
	@MessageMapping("/action/{lobbyId}")
	@SendTo("/lobbystatus/{lobbyId}")
	public LobbyActionMessage lobbyActionHandler(@DestinationVariable String lobbyId,
	                                             @Payload SpillerActionMessage message) {
		logger.info("Received SpillerActionMessage: {}", message);
		if (lobbyId == null || lobbyId.isBlank()) {
			logger.warn("LobbyId is missing or blank in message: {}", message);
			return null;
		}
		if (erUgyldigMelding(message)) {
			logger.warn("Invalid message: {}", message);
			return null;
		}
		Lobby lobby;
		Spiller spiller;
		try {
			lobby = sjekkLobby(message.getSpillerNavn(), lobbyId);
			spiller = sjekkSpiller(message.getSpillerNavn(), lobby);
		} catch (IllegalArgumentException e) {
			return null;
		}
		LobbyActionMessage returnMessage = null;
		switch (message.getAction()) {
			case JOIN:
				logger.info("Spiller {} har joinet lobbyen {}", spiller.getNavn(), lobbyId);
				logger.error("JOIN er ikke ferdig implementert");
				// TODO: Ferdigstill join implementasjon
				lobby.leggTilSpiller(spiller);
				returnMessage =  new LobbyActionMessage(lobbyId, lobby.getSpillernesNavn(), spiller.getNavn(), Action.JOIN);
				break;
			case LEAVE:
				logger.info("Spiller {} har forlatt lobbyen {} ", spiller.getNavn(), lobbyId);
				logger.error("LEAVE er ikke implementert");
				// TODO: Implementer leave
				break;
			case AFK:
				logger.info("Spiller {} er AFK i lobbyen {} ", spiller.getNavn(), lobbyId);
				logger.error("AFK er ikke implementert");
				// TODO: Implementer AFK
				break;
			case READY:
				logger.info("Spiller {} er klar i lobbyen {} ", spiller.getNavn(), lobbyId);
				logger.error("READY er ikke implementert");
				// TODO: Implementer ready
				break;
			case UNREADY:
				logger.info("Spiller {} er ikke klar i lobbyen {} ", spiller.getNavn(), lobbyId);
				logger.error("UNREADY er ikke implementert");
				// TODO: Implementer unready
				break;
			case DISCONNECT:
				logger.info("Spiller {} har blitt disconnected i lobbyen {} ", spiller.getNavn(), lobbyId);
				logger.error("DISCONNECT er ikke implementert");
				// TODO: Implementer disconnect
				break;
			case START:
				logger.info("Spiller {} prøver å starte spillet i lobbyen {} ", spiller.getNavn(), lobbyId);
				// TODO: Gjør ferdig start implementasjon
				if (lobby.getLobbyLeder().equals(spiller)) {
					returnMessage = new LobbyActionMessage(lobbyId, lobby.getSpillernesNavn(), spiller.getNavn(),
					                              Action.START);
				} else {
					logger.warn("Spiller {} er ikke lobbyleder i lobbyen {}", spiller.getNavn(), lobbyId);
				}
				break;
			case END:
				logger.info("Spiller {} prøver å stoppe spillet i lobbyen {} ", spiller.getNavn(), lobbyId);
				logger.error("END er ikke implementert");
				// TODO: Implementer end
				break;
		}
		logger.info("Action message sent: {}", returnMessage);
		return returnMessage;
	}
}
