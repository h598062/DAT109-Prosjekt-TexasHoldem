package no.hvl.dat109.texasholdem.service;

import no.hvl.dat109.texasholdem.game.Spiller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;

@Service
public class SpillerMeldingService {

	private final SimpMessagingTemplate messagingTemplate;

	@Autowired
	public SpillerMeldingService(SimpMessagingTemplate messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
	}

	/**
	 * Sender en melding til en spiller sin egen topic.<br>
	 * Metoden formatterer navnet til en gyldig URI før den sender meldingen.<br>
	 * Antar at navnet er gyldig
	 *
	 * @param spillerNavn navnet på spilleren, må være sjekket først
	 * @param melding     meldingen som skal sendes
	 */
	public void sendMelding(String spillerNavn, String melding) {
		messagingTemplate.convertAndSend("/spiller/" + UriUtils.encode(spillerNavn, StandardCharsets.UTF_8), melding);
	}

	/**
	 * Sender en melding til en spiller sin egen topic.<br>
	 * Metoden formatterer navnet til en gyldig URI før den sender meldingen.<br>
	 *
	 * @param spiller   Spiller melding skal sendes til
	 * @param melding   Meldingen som skal sendes
	 * @return Boolean true om meldingen kunne sendes, false om ikke
	 */
	public boolean sendMelding(Spiller spiller, String melding) {
		// TODO: implementer sjekker om spiller er gyldig etc.
		sendMelding(spiller.getNavn(), melding);
		return true;
	}
}
