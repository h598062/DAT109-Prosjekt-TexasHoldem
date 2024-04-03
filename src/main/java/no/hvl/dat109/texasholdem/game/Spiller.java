package no.hvl.dat109.texasholdem.game;

import no.hvl.dat109.texasholdem.enums.Status;
import no.hvl.dat109.texasholdem.enums.Trekk;

import java.util.Objects;

public class Spiller {
    private Hand hand;
    private int chips;

	/**
	 * navn på spiller (brukernavn) == spillerid
	 */
    private String navn;
    private Status status;
    private Trekk trekk;

	public Spiller(String navn) {
		this.navn = navn;
	}

    /**
     * Trekker ett kort fra kortstokken og legger det til i hånden
     * @param ks (kortstokk)
     */
    public void drawCard(Kortstokk ks) {
        Kort kort = ks.trekKort();
        hand.addCard(kort);
    }

    private void dinTur() {

    }

    private void emptyHand() {
        hand.clear();
    }

	public int getChips() {
		return chips;
	}

	public void setChips(int chips) {
		this.chips = chips;
	}

	public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Trekk getTrekk() {
        return trekk;
    }

    public void setTrekk(Trekk trekk) {
        this.trekk = trekk;
    }

	public String getNavn() {
		return navn;
	}

	public void setNavn(String navn) {
		this.navn = navn;
	}

	// spillere er like hvis navnet er likt
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Spiller spiller = (Spiller) o;

		return Objects.equals(navn, spiller.navn);
	}

	@Override
	public int hashCode() {
		return navn != null ? navn.hashCode() : 0;
	}
}
