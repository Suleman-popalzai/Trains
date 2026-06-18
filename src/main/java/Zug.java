package at.spengergasse.myproject.model;

import java.util.Objects;

/**
 * Subklasse: Zug
 */
public class Zug extends Verkehrsmittel {

    private String zugNummer;
    private String typ;
    private String antriebsArt;

    public Zug(String zugNummer,
               String typ,
               int kapazitaet,
               double co2ProKm,
               String antriebsArt,
               String linie) {

        super(zugNummer, kapazitaet, co2ProKm, linie);

        this.zugNummer = zugNummer;
        this.typ = typ;
        this.antriebsArt = antriebsArt;
    }

    @Override
    public double berechneCo2(double km) {
        return co2ProKm * km;
    }

    public double berechneAuslastung(int besetztePlaetze) {

        if (kapazitaet == 0) {
            return 0;
        }

        return (besetztePlaetze * 100.0) / kapazitaet;
    }

    public String toCsv() {
        return "Zug," +
                zugNummer + "," +
                typ + "," +
                kapazitaet + "," +
                co2ProKm + "," +
                antriebsArt + "," +
                linie;
    }

    public String getZugNummer() {
        return zugNummer;
    }

    public String getTyp() {
        return typ;
    }

    public String getAntriebsArt() {
        return antriebsArt;
    }

    @Override
    public String toString() {
        return "Zug{" +
                "zugNummer='" + zugNummer + '\'' +
                ", typ='" + typ + '\'' +
                ", kapazitaet=" + kapazitaet +
                ", co2ProKm=" + co2ProKm +
                ", antriebsArt='" + antriebsArt + '\'' +
                ", linie='" + linie + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Zug zug)) return false;

        return kapazitaet == zug.kapazitaet
                && Double.compare(zug.co2ProKm, co2ProKm) == 0
                && Objects.equals(zugNummer, zug.zugNummer)
                && Objects.equals(typ, zug.typ)
                && Objects.equals(antriebsArt, zug.antriebsArt)
                && Objects.equals(linie, zug.linie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                zugNummer,
                typ,
                kapazitaet,
                co2ProKm,
                antriebsArt,
                linie
        );
    }
}