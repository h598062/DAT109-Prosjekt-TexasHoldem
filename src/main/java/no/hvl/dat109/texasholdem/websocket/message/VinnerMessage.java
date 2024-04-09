package no.hvl.dat109.texasholdem.websocket.message;

public class VinnerMessage extends LobbyMessage {
	private String vinner;

	public VinnerMessage(String lobbyId, String vinner) {
		super(lobbyId);
		this.vinner = vinner;
	}

	public VinnerMessage() {
	}

	public String getVinner() {
		return vinner;
	}

	public void setVinner(String vinner) {
		this.vinner = vinner;
	}
}
