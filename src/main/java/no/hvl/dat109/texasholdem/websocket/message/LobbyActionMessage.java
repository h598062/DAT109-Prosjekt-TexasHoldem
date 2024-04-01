package no.hvl.dat109.texasholdem.websocket.message;

import no.hvl.dat109.texasholdem.enums.Action;

import java.util.List;

/**
 * Denne klassen representerer en melding som sendes til klientene når en handling
 * har blitt utført i en lobby, som f.eks. en spiller joiner, forlater, blir AFK, etc.
 * eller når spillet starter eller slutter.<br>
 * Den inneholder en liste med spillere som er i lobbyen etter handlingen.
 */
public class LobbyActionMessage extends LobbyMessage {
	private List<String> spillere;
	private String spillerNavn;
	private Action action;

	public LobbyActionMessage(String lobbyId, List<String> spillere, String spillerNavn, Action action) {
		super(lobbyId);
		this.spillere = spillere;
		this.spillerNavn = spillerNavn;
		this.action = action;
	}

	/**
	 * Tom konstruktør for serialisering<br>
	 * Lager du meldingen selv, bruk konstruktøren med parametre.
	 */
	public LobbyActionMessage() {
	}

	public List<String> getSpillere() {
		return spillere;
	}

	public void setSpillere(List<String> spillere) {
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
