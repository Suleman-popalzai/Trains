package at.spengergasse.myproject.model;

import java.util.Objects;

/**
 * Subklasse: Strassenbahn
 */
public class Strassenbahn extends at.spengergasse.myproject.model.Verkehrsmittel {

    private int anzahlWaggons;
    private String haltestelle;

    public Strassenbahn(String id,
                        String linie,
                        int kapazitaet,
                        double co2ProKm,
                        int anzahlWaggons,
                        String haltestelle) {

        super(id, kapazitaet, co2ProKm, linie);

        this.anzahlWaggons = anzahlWaggons;
        this.haltestelle = haltestelle;
    }

    @Override
    public double berechneCo2(double km) {
        return co2ProKm * km;
    }

    public double co2ErsparnisGegenPkw(int fahrgaeste,
                                       double km) {

        double pkwCo2 = fahrgaeste * 0.120 * km;

        double bahnCo2 = berechneCo2(km);

        return pkwCo2 - bahnCo2;
    }

    public String toCsv() {
        return "Strassenbahn," +
                id + "," +
                linie + "," +
                kapazitaet + "," +
                co2ProKm + "," +
                anzahlWaggons + "," +
                haltestelle;
    }

    public int getAnzahlWaggons() {
        return anzahlWaggons;
    }

    public String getHaltestelle() {
        return haltestelle;
    }

    @Override
    public String toString() {
        return "Strassenbahn{" +
                "id='" + id + '\'' +
                ", linie='" + linie + '\'' +
                ", anzahlWaggons=" + anzahlWaggons +
                ", kapazitaet=" + kapazitaet +
                ", co2ProKm=" + co2ProKm +
                ", haltestelle='" + haltestelle + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Strassenbahn that)) return false;

        return kapazitaet == that.kapazitaet
                && anzahlWaggons == that.anzahlWaggons
                && Double.compare(that.co2ProKm, co2ProKm) == 0
                && Objects.equals(id, that.id)
                && Objects.equals(linie, that.linie)
                && Objects.equals(haltestelle, that.haltestelle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id,
                linie,
                kapazitaet,
                co2ProKm,
                anzahlWaggons,
                haltestelle
        );
    }
}