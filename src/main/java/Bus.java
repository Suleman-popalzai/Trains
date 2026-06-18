package at.spengergasse.myproject.model;

import java.util.Objects;

/**
 * Subklasse: Bus
 */
public class Bus extends at.spengergasse.myproject.model.Verkehrsmittel {

    private String kennzeichen;
    private String kraftstoffArt;
    private double verbrauchProKm;
    private String haltestelle;

    public Bus(String id,
               String linie,
               int kapazitaet,
               double co2ProKm,
               String kennzeichen,
               String kraftstoffArt,
               double verbrauchProKm,
               String haltestelle) {

        super(id, kapazitaet, co2ProKm, linie);

        this.kennzeichen = kennzeichen;
        this.kraftstoffArt = kraftstoffArt;
        this.verbrauchProKm = verbrauchProKm;
        this.haltestelle = haltestelle;
    }

    /**
     * Berechnet den CO2-Ausstoß für eine Strecke
     */
    @Override
    public double berechneCo2(double km) {
        return co2ProKm * km;
    }

    /**
     * Berechnet die Kraftstoffkosten
     */
    public double berechneKraftstoffKosten(double km,
                                           double preisProEinheit) {

        return verbrauchProKm * km * preisProEinheit;
    }

    /**
     * CSV Export
     */
    public String toCsv() {

        return "Bus," +
                id + "," +
                linie + "," +
                kapazitaet + "," +
                co2ProKm + "," +
                kennzeichen + "," +
                kraftstoffArt + "," +
                verbrauchProKm + "," +
                haltestelle;
    }

    // Getter

    public String getKennzeichen() {
        return kennzeichen;
    }

    public String getKraftstoffArt() {
        return kraftstoffArt;
    }

    public double getVerbrauchProKm() {
        return verbrauchProKm;
    }

    public String getHaltestelle() {
        return haltestelle;
    }

    @Override
    public String toString() {

        return "Bus{" +
                "id='" + id + '\'' +
                ", linie='" + linie + '\'' +
                ", kennzeichen='" + kennzeichen + '\'' +
                ", kapazitaet=" + kapazitaet +
                ", co2ProKm=" + co2ProKm +
                ", kraftstoffArt='" + kraftstoffArt + '\'' +
                ", verbrauchProKm=" + verbrauchProKm +
                ", haltestelle='" + haltestelle + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof Bus bus)) {
            return false;
        }

        return kapazitaet == bus.kapazitaet
                && Double.compare(bus.co2ProKm, co2ProKm) == 0
                && Double.compare(bus.verbrauchProKm, verbrauchProKm) == 0
                && Objects.equals(id, bus.id)
                && Objects.equals(linie, bus.linie)
                && Objects.equals(kennzeichen, bus.kennzeichen)
                && Objects.equals(kraftstoffArt, bus.kraftstoffArt)
                && Objects.equals(haltestelle, bus.haltestelle);
    }

    @Override
    public int hashCode() {

        return Objects.hash(
                id,
                linie,
                kennzeichen,
                kapazitaet,
                co2ProKm,
                kraftstoffArt,
                verbrauchProKm,
                haltestelle
        );
    }
}