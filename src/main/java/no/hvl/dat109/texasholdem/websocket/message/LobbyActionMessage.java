package no.hvl.dat109.texasholdem.websocket.message;

import no.hvl.dat109.texasholdem.enums.Action;
import no.hvl.dat109.texasholdem.game.Spiller;

import java.util.ArrayList;
import java.util.List;

/**
 * Denne klassen representerer en melding som sendes til klientene når en handling
 * har blitt utført i en lobby, som f.eks. en spiller joiner, forlater, blir AFK, etc.
 * eller når spillet starter eller slutter.<br>
 * Den inneholder en liste med spillere som er i lobbyen etter handlingen.
 */
public class LobbyActionMessage extends LobbyMessage {
	private List<SpillerInfo> spillere;
	private String spillerNavn;
	private Action action;

	public LobbyActionMessage(String lobbyId, List<Spiller> spillere, String spillerNavn, Action action) {
		super(lobbyId);
		this.spillere = new ArrayList<>();
		this.spillerNavn = spillerNavn;
		this.action = action;
		spillere.forEach(s -> {
			SpillerInfo si = new SpillerInfo();
			si.setNavn(s.getNavn());
			si.setChips(s.getChips());
			si.setStatus(s.getStatus());
			this.spillere.add(si);
		});
	}

	/**
	 * Tom konstruktør for serialisering<br>
	 * Lager du meldingen selv, bruk konstruktøren med parametre.
	 */
	public LobbyActionMessage() {
	}

	public List<SpillerInfo> getSpillere() {
		return spillere;
	}

	public void setSpillere(List<SpillerInfo> spillere) {
		this.spillere = spillere;
	}

	public String getSpillerNavn() {
		return spillerNavn;
	}

	public void setSpillerNavn(String spillerNavn) {
		this.spillerNavn = spillerNavn;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	@Override
	public String toString() {
		return "LobbyActionMessage{" +
		       "lobbyId='" + getLobbyId() + '\'' +
		       ", spillere=" + getSpillere() +
		       ", action=" + getAction() +
		       '}';
	}
}
