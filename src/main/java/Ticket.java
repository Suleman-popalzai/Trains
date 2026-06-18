package at.spengergasse.myproject.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Ticket - eigenstaendige Modellklasse.
 */
public class Ticket {

    private double preis;
    private int sitzplatz;
    private LocalDateTime kaufdatum;

    public Ticket(double preis, int sitzplatz, LocalDateTime kaufdatum) {
        this.preis = preis;
        this.sitzplatz = sitzplatz;
        this.kaufdatum = kaufdatum;
    }

    public double getPreis() { return preis; }
    public int getSitzplatz() { return sitzplatz; }
    public LocalDateTime getKaufdatum() { return kaufdatum; }

    /** Preis mit 10% Rabatt. */
    public double berechnePreisMitRabatt() {
        return preis * 0.9;
    }

    /** Enthaltene Mehrwertsteuer (20% in OEsterreich). */
    public double berechneSteuer() {
        return preis - (preis / 1.20);
    }

    @Override
    public String toString() {
        return "Ticket{preis=" + preis + ", sitzplatz=" + sitzplatz
                + ", kaufdatum=" + kaufdatum + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ticket ticket)) return false;
        return Double.compare(ticket.preis, preis) == 0
                && sitzplatz == ticket.sitzplatz
                && Objects.equals(kaufdatum, ticket.kaufdatum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(preis, sitzplatz, kaufdatum);
    }
}