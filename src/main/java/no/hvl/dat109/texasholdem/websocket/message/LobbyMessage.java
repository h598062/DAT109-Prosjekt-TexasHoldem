package no.hvl.dat109.texasholdem.websocket.message;


/**
 * Melding base objekt for meldinger fra lobbyen (server) til spillere (klienter)<br>
 * Det er ikke ment å bruke denne klassen direkte, men implementere den i en subklasse.
 */
public abstract class LobbyMessage {
	private String lobbyId;

	protected LobbyMessage(String lobbyId) {
		this.lobbyId = lobbyId;
	}

	protected LobbyMessage() {
	}

	/**
	 * Hent lobbyId (lobby navn)
	 * @return lobbyId
	 */
	public String getLobbyId() {
		return lobbyId;
	}

	/**
	 * Sett lobbyId (lobby navn)
	 * @param lobbyId lobbyId

	 */
	public void setLobbyId(String lobbyId) {
		this.lobbyId = lobbyId;
	}

	@Override
	public String toString() {
		return "LobbyMessage{" +
		       "lobbyId='" + getLobbyId() + '\'' +
		       '}';
	}
}
