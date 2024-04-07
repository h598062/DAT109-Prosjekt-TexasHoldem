package no.hvl.dat109.texasholdem.game;

public class VinnerException extends Exception {
	private Spiller vinner;
	public VinnerException(Spiller vinner) {
		super("Vinneren er " + vinner.getNavn());
		this.vinner = vinner;
	}

	public Spiller getVinner() {
		return vinner;
	}
}
