package no.hvl.dat109.texasholdem.websocket.message;

import no.hvl.dat109.texasholdem.enums.Status;

public class SpillerInfo {
	private String navn;
	private int    chips;
	private Status status;
	private int   currentBet;

	public SpillerInfo(String navn, int chips, Status status, int currentBet) {
		this.navn   = navn;
		this.chips  = chips;
		this.status = status;
		this.currentBet = currentBet;
	}

	public SpillerInfo() {
	}

	public String getNavn() {
		return navn;
	}

	public int getChips() {
		return chips;
	}

	public Status getStatus() {
		return status;
	}

	public void setNavn(String navn) {
		this.navn = navn;
	}

	public void setChips(int chips) {
		this.chips = chips;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public int getCurrentBet() {
		return currentBet;
	}

	public void setCurrentBet(int currentBet) {
		this.currentBet = currentBet;
	}
}
