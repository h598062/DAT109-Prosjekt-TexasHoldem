package no.hvl.dat109.texasholdem.websocket.message;

import no.hvl.dat109.texasholdem.game.Kort;

import java.util.List;

public class BordKortMessage extends LobbyMessage {
	private List<Kort> bordKort;

	public BordKortMessage(String lobbyId, List<Kort> bordKort) {
		super(lobbyId);
		this.bordKort = bordKort;
	}

	public BordKortMessage() {
	}

	public List<Kort> getBordKort() {
		return bordKort;
	}

	public void setBordKort(List<Kort> bordKort) {
		this.bordKort = bordKort;
	}
}
