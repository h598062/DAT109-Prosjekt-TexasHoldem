package no.hvl.dat109.texasholdem.service;

import no.hvl.dat109.texasholdem.game.Hand;
import no.hvl.dat109.texasholdem.websocket.message.SpillerKortMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class SpillerMeldingService {
	private final Logger logger = LoggerFactory.getLogger(SpillerMeldingService.class);

	private final SimpMessagingTemplate smt;

	@Autowired
	public SpillerMeldingService(SimpMessagingTemplate smt) {
		this.smt = smt;
	}

	/**
	 * Sender en String melding til en spiller sin egen topic.<br>
	 * Antar at navnet er gyldig
	 *
	 * @param spillerNavn navnet på spilleren, må være sjekket først
	 * @param melding     meldingen som skal sendes
	 */
	public void sendMelding(String spillerNavn, String melding) {
		smt.convertAndSend("/spiller/" + spillerNavn, melding);
	}

	public void sendSpillerHand(String spillerNavn, Hand hand, String lobbyId) {
		smt.convertAndSend("/spiller/" + spillerNavn, new SpillerKortMessage(spillerNavn, hand, lobbyId));
	}

}
