package no.hvl.dat109.texasholdem.controller;

import no.hvl.dat109.texasholdem.service.LobbyMeldingService;
import no.hvl.dat109.texasholdem.service.LobbyService;
import no.hvl.dat109.texasholdem.service.SpillerMeldingService;
import no.hvl.dat109.texasholdem.websocket.message.SpillerActionMessage;
import no.hvl.dat109.texasholdem.websocket.message.SpillerMessage;
import no.hvl.dat109.texasholdem.websocket.message.SpillerTrekkMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

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

	@Autowired
	private final LobbyService          lobbyService;
	@Autowired
	private final SpillerMeldingService sms;
	@Autowired
	private final LobbyMeldingService   lms;

	/**
	 * Konstruktør for Controlleren<br>
	 * Autowirer lobbyService og messagingTemplate i konstruktøren som er den gode måten å gjøre det på
	 *
	 * @param lobbyService service for lobbyen
	 * @param sms          service for å sende meldinger til en spiller
	 */
	public LobbyWebSocketController(LobbyService lobbyService, SpillerMeldingService sms,
	                                LobbyMeldingService lms) {
		this.lobbyService = lobbyService;
		this.sms          = sms;
		this.lms          = lms;
	}

	/**
	 * Sjekker om meldingen er ugyldig
	 *
	 * @param message meldingen som skal sjekkes
	 *
	 * @return true hvis meldingen er ugyldig, false ellers
	 */
	private boolean ugyldigMelding(String lobbyId, SpillerMessage message) {
		if (lobbyId == null || lobbyId.isBlank()) {
			logger.warn("LobbyId is missing or blank in message: {}", message);
			return true;
		}
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
	 * Håndterer en trekk-melding fra en spiller i en lobby<br>
	 * Server oppdaterer game-state og sender en oppdatert status til alle i lobbyen<br>
	 *
	 * @param lobbyId lobbyId som meldingen refererer til, denne hentes dynamisk fra path i request
	 * @param message meldingen som skal håndteres, Spring parser denne fra JSON til SpillerTrekkMessage
	 *
	 */
	@MessageMapping("/trekk/{lobbyId}")
	public void lobbyTrekkHandler(@DestinationVariable String lobbyId,
	                              @Payload SpillerTrekkMessage message) {
		logger.info("Received SpillerTrekkMessage: {}", message);
		if (ugyldigMelding(lobbyId, message)) {
			return;
		}

		lobbyService.doTrekk(lobbyId, message.getSpillerNavn(), message.getTrekk(),
				message.getMengde());

	}

	/**
	 * Håndterer en lobby-action-melding fra en spiller i en lobby<br>
	 * Server oppdaterer lobby-state og sender en oppdatert status til alle i lobbyen<br>
	 * Denne metoden oppdaterer spiller status etc i lobbyen<br>
	 *
	 * @param lobbyId lobbyId som meldingen refererer til, denne hentes dynamisk fra path i request
	 * @param message meldingen som skal håndteres, Spring parser denne fra JSON til SpillerActionMessage
	 *
	 */
	@MessageMapping("/action/{lobbyId}")
	public void lobbyActionHandler(@DestinationVariable String lobbyId,
	                               @Payload SpillerActionMessage message) {
		logger.info("Received SpillerActionMessage: {}", message);
		if (ugyldigMelding(lobbyId, message)) {
			return;
		}
		if (!lobbyService.doAction(lobbyId, message.getSpillerNavn(), message.getAction())) {
			sms.sendMelding(message.getSpillerNavn(),
					String.format("{\"msg\": \"Kunne ikke utføre handling %s i lobby %s\"}", message.getAction(),
							lobbyId));
		}
	}
}
