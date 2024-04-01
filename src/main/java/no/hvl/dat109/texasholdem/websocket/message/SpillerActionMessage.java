package no.hvl.dat109.texasholdem.websocket.message;

import no.hvl.dat109.texasholdem.enums.Action;

public class SpillerActionMessage extends SpillerMessage {
	private Action action;
	public SpillerActionMessage(String spillerNavn, Action action) {
		super(spillerNavn);
		this.action = action;
	}

	/**
	 * Tom konstruktør for serialisering<br>
	 * Lager du meldingen selv, bruk konstruktøren med parametre.
	 */
	public SpillerActionMessage() {
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	@Override
	public String toString() {
		return "SpillerActionMessage{" +
		       "spillerNavn='" + getSpillerNavn() + '\'' +
		       ", action=" + getAction() +
		       '}';
	}
}
