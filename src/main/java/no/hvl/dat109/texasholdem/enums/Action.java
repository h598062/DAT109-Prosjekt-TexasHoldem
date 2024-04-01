package no.hvl.dat109.texasholdem.enums;

public enum Action {
	JOIN, LEAVE, AFK, READY, UNREADY, DISCONNECT, START, END

	// START og STOPP er for lobbyleder, for å starte og stoppe spillet

	// gyldig før spillet i lobby har startet
	// READY
	// NOT_READY
	// JOIN
	// LEAVE
	// DISCONNECT

	// gyldig etter spillet i lobby har startet
	// AFK
	// READY
	// LEAVE
	// DISCONNECT
}
