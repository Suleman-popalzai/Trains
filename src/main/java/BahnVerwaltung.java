package at.spengergasse.myproject;

import at.spengergasse.myproject.model.Bus;
import at.spengergasse.myproject.model.Strassenbahn;
import at.spengergasse.myproject.model.Ticket;
import at.spengergasse.myproject.model.Verkehrsmittel;
import at.spengergasse.myproject.model.Zug;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BahnVerwaltung {

    private static BahnVerwaltung instance;

    private final List<Verkehrsmittel> verkehrsmittel;
    private final List<Ticket> tickets;

    private BahnVerwaltung() {
        verkehrsmittel = new ArrayList<>();
        tickets = new ArrayList<>();
        ladeBeispieldaten();
    }

    public static BahnVerwaltung getInstance() {
        if (instance == null) {
            instance = new BahnVerwaltung();
        }
        return instance;
    }

    private void ladeBeispieldaten() {
        verkehrsmittel.add(new Zug("ICE 101", "ICE", 450, 0.035, "Elektro", "Wien-Salzburg"));
        verkehrsmittel.add(new Zug("Railjet 45", "Railjet", 408, 0.040, "Elektro", "Wien-Graz"));
        verkehrsmittel.add(new Strassenbahn("U6", "U6", 200, 0.020, 4, "Floridsdorf"));
        verkehrsmittel.add(new Strassenbahn("D", "D", 180, 0.022, 3, "Hauptbahnhof"));
        verkehrsmittel.add(new Bus("Bus 14A", "14A", 90, 0.080, "W-12345", "Diesel", 0.30, "Praterstern"));
        tickets.add(new Ticket(59.90, 12, LocalDateTime.now()));
        tickets.add(new Ticket(29.90, 5, LocalDateTime.now()));
    }

    public List<Verkehrsmittel> getVerkehrsmittel() {
        return verkehrsmittel;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void hinzufuegenVerkehrsmittel(Verkehrsmittel v) {
        if (v != null) verkehrsmittel.add(v);
    }

    public void hinzufuegenTicket(Ticket t) {
        if (t != null) tickets.add(t);
    }

    public void entferneVerkehrsmittel(Verkehrsmittel v) {
        verkehrsmittel.remove(v);
    }

    public void sortiereNachKapazitaet() {
        verkehrsmittel.sort(Comparator.comparingInt(Verkehrsmittel::getKapazitaet));
    }

    public void sortiereNachCo2() {
        verkehrsmittel.sort(Comparator.comparingDouble(Verkehrsmittel::getCo2ProKm));
    }

    public double durchschnittsPreisTickets() {
        if (tickets.isEmpty()) return 0.0;
        return tickets.stream().mapToDouble(Ticket::getPreis).average().orElse(0.0);
    }

    public double durchschnittsCo2ProKm() {
        if (verkehrsmittel.isEmpty()) return 0.0;
        return verkehrsmittel.stream().mapToDouble(Verkehrsmittel::getCo2ProKm).average().orElse(0.0);
    }

    public int anzahlAllerObjekte() {
        return verkehrsmittel.size() + tickets.size();
    }

    public <T> long anzahlProKlasse(Class<T> clazz) {
        return verkehrsmittel.stream().filter(v -> v.getClass().equals(clazz)).count();
    }

    public void exportCSV(String pfad) throws TrainStationException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(pfad))) {
            for (Verkehrsmittel v : verkehrsmittel) {
                if (v instanceof Zug z)             writer.println(z.toCsv());
                else if (v instanceof Strassenbahn s) writer.println(s.toCsv());
                else if (v instanceof Bus b)          writer.println(b.toCsv());
            }
        } catch (IOException e) {
            throw new TrainStationException("Fehler beim CSV-Export: " + e.getMessage());
        }
    }

    public void importCSV(String pfad) throws TrainStationException {
        try (BufferedReader reader = new BufferedReader(new FileReader(pfad))) {
            String zeile;
            verkehrsmittel.clear();
            while ((zeile = reader.readLine()) != null) {
                if (zeile.isBlank()) continue;
                String[] f = zeile.split(",");
                switch (f[0]) {
                    case "Zug" -> verkehrsmittel.add(
                            new Zug(f[1], f[2], Integer.parseInt(f[3]), Double.parseDouble(f[4]), f[5], f[6]));
                    case "Strassenbahn" -> verkehrsmittel.add(
                            new Strassenbahn(f[1], f[2], Integer.parseInt(f[3]), Double.parseDouble(f[4]), Integer.parseInt(f[5]), f[6]));
                    case "Bus" -> verkehrsmittel.add(
                            new Bus(f[1], f[2], Integer.parseInt(f[3]), Double.parseDouble(f[4]), f[5], f[6], Double.parseDouble(f[7]), f[8]));
                    default -> throw new TrainStationException("Unbekannter Typ: " + f[0]);
                }
            }
        } catch (IOException | NumberFormatException | ArrayIndexOutOfBoundsException e) {
            throw new TrainStationException("Fehler beim CSV-Import: " + e.getMessage());
        }
    }
}