package at.spengergasse.myproject.model;

import java.util.Objects;

/**
 * Abstrakte Basisklasse fuer alle Verkehrsmittel.
 */
public abstract class Verkehrsmittel implements Comparable<Verkehrsmittel> {

    protected String id;
    protected int kapazitaet;
    protected double co2ProKm;
    protected String linie;

    // Konstruktor 1
    public Verkehrsmittel() {
        this.id = "unbekannt";
        this.linie = "";
    }

    // Konstruktor 2
    public Verkehrsmittel(String id,
                          int kapazitaet,
                          double co2ProKm,
                          String linie) {

        this.id = id;
        this.kapazitaet = kapazitaet;
        this.co2ProKm = co2ProKm;
        this.linie = linie;
    }

    // Abstrakte Methode
    public abstract double berechneCo2(double km);

    // Getter
    public String getId() {
        return id;
    }

    public int getKapazitaet() {
        return kapazitaet;
    }

    public double getCo2ProKm() {
        return co2ProKm;
    }

    public String getLinie() {
        return linie;
    }

    // Setter
    public void setId(String id) {
        this.id = id;
    }

    public void setKapazitaet(int kapazitaet) {
        this.kapazitaet = kapazitaet;
    }

    public void setCo2ProKm(double co2ProKm) {
        this.co2ProKm = co2ProKm;
    }

    public void setLinie(String linie) {
        this.linie = linie;
    }

    // Comparable
    @Override
    public int compareTo(Verkehrsmittel o) {
        return Double.compare(this.co2ProKm, o.co2ProKm);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                "{id='" + id + '\'' +
                ", kapazitaet=" + kapazitaet +
                ", co2ProKm=" + co2ProKm +
                ", linie='" + linie + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Verkehrsmittel that)) return false;

        return kapazitaet == that.kapazitaet
                && Double.compare(that.co2ProKm, co2ProKm) == 0
                && Objects.equals(id, that.id)
                && Objects.equals(linie, that.linie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, kapazitaet, co2ProKm, linie);
    }
}