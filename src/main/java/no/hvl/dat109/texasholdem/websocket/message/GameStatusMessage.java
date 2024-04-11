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
	private int                   pott;
	private int                   raiseTarget;

	/**
	 * Lag en ny lobby status melding
	 *
	 * @param lobbyId       id til lobbyen
	 * @param spillere      liste over spillere
	 * @param spillerSinTur spilleren som skal gjøre sitt trekk
	 * @param runde         hvilken runde vi er på
	 * @param pott
	 * @param raiseTarget
	 */
	public GameStatusMessage(String lobbyId, List<Spiller> spillere, Spiller spillerSinTur,
	                         TexasHoldemGame.Round runde, int pott, int raiseTarget) {
		super(lobbyId);
		this.spillere      = new ArrayList<>();
		this.spillerSinTur = spillerSinTur == null ? "" : spillerSinTur.getNavn();
		this.runde         = runde;
		this.pott          = pott;
		this.raiseTarget   = raiseTarget;
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

	public int getPott() {
		return pott;
	}

	public void setPott(int pott) {
		this.pott = pott;
	}

	public int getRaiseTarget() {
		return raiseTarget;
	}

	public void setRaiseTarget(int raiseTarget) {
		this.raiseTarget = raiseTarget;
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
