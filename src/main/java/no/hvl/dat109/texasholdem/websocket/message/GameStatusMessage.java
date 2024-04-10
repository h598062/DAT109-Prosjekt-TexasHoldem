package no.hvl.dat109.texasholdem.websocket.message;

import no.hvl.dat109.texasholdem.enums.Status;
import no.hvl.dat109.texasholdem.game.Spiller;
import no.hvl.dat109.texasholdem.game.TexasHoldemGame;

import java.util.ArrayList;
import java.util.List;

public class GameStatusMessage extends LobbyMessage {
	private List<SpillerInfo>     spillere;
	private String                spillerSinTur;
	private TexasHoldemGame.Round runde;

	/**
	 * Lag en ny lobby status melding
	 *
	 * @param lobbyId              id til lobbyen
	 * @param ferdigeSpillere      liste over spillere som er ferdige med sitt trekk
	 * @param ventendeSpillere     liste over spillere som venter på å gjøre sitt trekk
	 * @param allInSpillere        liste over spillere som er all-in
	 * @param spillereSomHarFoldet liste over spillere som har foldet
	 * @param spillerSinTur        spilleren som skal gjøre sitt trekk
	 * @param runde                hvilken runde vi er på
	 */
	public GameStatusMessage(String lobbyId, List<Spiller> spillere, Spiller spillerSinTur,
	                         TexasHoldemGame.Round runde) {
		super(lobbyId);
		this.spillere      = new ArrayList<>();
		this.spillerSinTur = spillerSinTur.getNavn();
		this.runde         = runde;
		spillere.forEach(s -> {
			SpillerInfo si = new SpillerInfo();
			si.setNavn(s.getNavn());
			si.setChips(s.getChips());
			si.setStatus(s.getStatus());
			this.spillere.add(si);
		});
	}

	public GameStatusMessage() {
	}

	public List<SpillerInfo> getSpillere() {
		return spillere;
	}

	public void setSpillere(List<SpillerInfo> spillere) {
		this.spillere = spillere;
	}

	public String getSpillerSinTur() {
		return spillerSinTur;
	}

	public void setSpillerSinTur(String spillerSinTur) {
		this.spillerSinTur = spillerSinTur;
	}

	public TexasHoldemGame.Round getRunde() {
		return runde;
	}

	public void setRunde(TexasHoldemGame.Round runde) {
		this.runde = runde;
	}

}
