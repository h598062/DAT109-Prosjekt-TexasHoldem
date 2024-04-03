package no.hvl.dat109.texasholdem.websocket.message;

import no.hvl.dat109.texasholdem.enums.Trekk;

/**
 * Melding som sendes til alle spillere i en lobby når en spiller har gjort et trekk
 */
public class LobbyTrekkMessage extends LobbyMessage {
	/**
	 * Spilleren (navn) som har spilt et trekk
	 */
	private String spillerNavn;
	private Trekk trekk;
	private int mengde;

	public LobbyTrekkMessage(String lobbyId, String spillerNavn, Trekk trekk, int mengde) {
		super(lobbyId);
		this.spillerNavn = spillerNavn;
		this.trekk = trekk;
		this.mengde = mengde;
	}

	/**
	 * Tom konstruktør for serialisering<br>
	 * Lager du meldingen selv, bruk konstruktøren med parametre.
	 */
	public LobbyTrekkMessage() {
	}

	public int getMengde() {
		return mengde;
	}

	public void setMengde(int mengde) {
		this.mengde = mengde;
	}

	public String getSpillerNavn() {
		return spillerNavn;
	}

	public void setSpillerNavn(String spillerNavn) {
		this.spillerNavn = spillerNavn;
	}

	public Trekk getTrekk() {
		return trekk;
	}

	public void setTrekk(Trekk trekk) {
		this.trekk = trekk;
	}

	@Override
	public String toString() {
		return "LobbyTrekkMessage{" +
		       "lobbyId='" + getLobbyId() + '\'' +
		       ", spillerNavn='" + getSpillerNavn() + '\'' +
		       ", trekk=" + getTrekk() +
		       '}';
	}
}
