package no.hvl.dat109.texasholdem.websocket.message;

import no.hvl.dat109.texasholdem.game.Hand;

public class SpillerKortMessage extends SpillerMessage {

	private Hand hand;
	private String lobbyId;

	public SpillerKortMessage(String spillerNavn, Hand hand, String lobbyId) {
		super(spillerNavn);
		this.hand = hand;
		this.lobbyId = lobbyId;
	}

	public SpillerKortMessage() {
	}

	public Hand getHand() {
		return hand;
	}

	public void setHand(Hand hand) {
		this.hand = hand;
	}

	public String getLobbyId() {
		return lobbyId;
	}

	public void setLobbyId(String lobbyId) {
		this.lobbyId = lobbyId;
	}
}
