package no.hvl.dat109.texasholdem.websocket.message;

/**
 * Melding base objekt for meldinger fra spillere (klienter) til lobbyen (server)<br>
 * Det er ikke ment å bruke denne klassen direkte, men implementere den i en subklasse.
 */
public abstract class SpillerMessage {
	private String spillerNavn;

	protected SpillerMessage(String spillerNavn) {
		this.spillerNavn = spillerNavn;
	}

	protected SpillerMessage() {
	}

	/**
	 * Hent spillerNavn
	 * @return spillerNavn
	 */
	public String getSpillerNavn() {
		return spillerNavn;
	}

	/**
	 * Sett spillerNavn
	 * @param spillerNavn spillerNavn
	 */
	public void setSpillerNavn(String spillerNavn) {
		this.spillerNavn = spillerNavn;
	}

	@Override
	public String toString() {
		return "SpillerMessage{" +
		       "spillerNavn='" + getSpillerNavn() + '\'' +
		       '}';
	}
}
