package no.hvl.dat109.texasholdem.websocket.message;

import no.hvl.dat109.texasholdem.enums.Trekk;

public class SpillerTrekkMessage extends SpillerMessage {
	private Trekk trekk;
	private int mengde;
	public SpillerTrekkMessage(String spiller, Trekk trekk, int mengde) {
		super(spiller);
		this.mengde = mengde;
		this.trekk = trekk;
	}

	public SpillerTrekkMessage(String spiller, Trekk trekk) {
		super(spiller);
		this.mengde = 0;
		this.trekk = trekk;
	}

	/**
	 * Tom konstruktør for serialisering<br>
	 * Lager du meldingen selv, bruk konstruktøren med parametre.
	 */
	public SpillerTrekkMessage() {
	}

	public Trekk getTrekk() {
		return trekk;
	}

	public void setTrekk(Trekk trekk) {
		this.trekk = trekk;
	}

	public int getMengde() {
		return mengde;
	}

	public void setMengde(int mengde) {
		this.mengde = mengde;
	}

	@Override
	public String toString() {
		return "SpillerTrekkMessage{" +
		       "spiller='" + getSpillerNavn() + '\'' +
		       ", trekk=" + getTrekk() +
		       ", mengde=" + getMengde() +
		       '}';
	}
}
