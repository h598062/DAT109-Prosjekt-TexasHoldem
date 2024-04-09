package no.hvl.dat109.texasholdem.service;

import no.hvl.dat109.texasholdem.enums.Action;
import no.hvl.dat109.texasholdem.enums.Trekk;
import no.hvl.dat109.texasholdem.game.Kort;
import no.hvl.dat109.texasholdem.game.Spiller;
import no.hvl.dat109.texasholdem.websocket.message.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LobbyMeldingService {

	private static final Logger logger = LoggerFactory.getLogger(LobbyMeldingService.class);

	private final SpillerMeldingService sms;

	private final SimpMessagingTemplate smt;

	@Autowired
	public LobbyMeldingService(SpillerMeldingService sms, SimpMessagingTemplate smt) {
		this.sms = sms;
		this.smt = smt;
	}

	/**
	 * Denne metoden sender ut en status melding i lobby, inneholder spillere, hvem sin tur, og hvilken runde vi er på
	 *
	 * @param lobbyId id til lobbyen
	 * @param message meldingen som skal sendes
	 *
	 * @return meldingen som blir sendt
	 */
	public void sendSpillStatus(String lobbyId, GameStatusMessage message) {
		logger.info("Sender spillstatus til lobby {}", lobbyId);
		smt.convertAndSend("/lobbystatus/" + lobbyId, message);
	}

	public void sendBordKort(String lobbyId, List<Kort> bordKort) {
		logger.info("Sender bordkort til lobby {}", lobbyId);
		smt.convertAndSend("/lobbystatus/" + lobbyId, new BordKortMessage(lobbyId, bordKort));
	}

	/**
	 * Sender ut informasjon om et trekk utført av en spiller i en lobby, til hele lobbyen
	 *
	 * @param lobbyId     id til lobbyen
	 * @param spillerNavn navnet på spilleren som utførte trekket
	 * @param trekk       typen trekk som ble utført
	 * @param mengde      mengden som ble satset (hvis trekk er RAISE)
	 *
	 * @return meldingen som blir sendt
	 */
	public void sendTrekk(String lobbyId, String spillerNavn, Trekk trekk,
	                      int mengde) {
		logger.info("Sender trekk til lobby {}", lobbyId);
		smt.convertAndSend("/lobbystatus/" + lobbyId, new LobbyTrekkMessage(lobbyId, spillerNavn, trekk, mengde));
	}

	public void sendAction(String lobbyId, List<String> spillere, String spillerNavn,
	                       Action action) {
		logger.info("Sender action til lobby {}", lobbyId);
		smt.convertAndSend("/lobbystatus/" + lobbyId, new LobbyActionMessage(lobbyId, spillere, spillerNavn, action));
	}

	/**
	 * Denne metoden sender ut kortene som blir delt ut til hver enkelt spiller
	 */
	public void sendKort(List<Spiller> spillere, String lobbyId) {
		logger.info("Sender kort til lobby {}", lobbyId);
		// spiller melding service tar inn et spiller navn og melding som tekst
		spillere.forEach(spiller -> sms.sendSpillerHand(spiller.getNavn(), spiller.getHand(), lobbyId));
	}

	public void sendVinner(Spiller vinner, String lobbyId) {
		smt.convertAndSend("/lobbystatus/" + lobbyId, new VinnerMessage(lobbyId, vinner.getNavn()));
	}
}
