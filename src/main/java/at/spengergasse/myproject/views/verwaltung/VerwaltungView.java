package at.spengergasse.myproject.views.verwaltung;

import at.spengergasse.myproject.BahnVerwaltung;
import at.spengergasse.myproject.TrainStationException;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

@PageTitle("Verwaltung")
@Route("")
@Menu(order = 0, icon = LineAwesomeIconUrl.TRAIN_SOLID)
public class VerwaltungView extends VerticalLayout {

    private final BahnVerwaltung verwaltung = BahnVerwaltung.getInstance();
    private final Grid<at.spengergasse.myproject.model.Verkehrsmittel> grid = new Grid<>(at.spengergasse.myproject.model.Verkehrsmittel.class, false);

    public VerwaltungView() {
        addClassName("bahn-page");
        setPadding(false);
        setSpacing(false);

        H1 titel = new H1("Bahnverwaltung");
        titel.addClassName("bahn-title");

        // ── Grid ───────────────────────────────────────────
        grid.addColumn(v -> v.getClass().getSimpleName()).setHeader("Typ").setWidth("120px");
        grid.addColumn(at.spengergasse.myproject.model.Verkehrsmittel::getId).setHeader("ID / Name").setAutoWidth(true);
        grid.addColumn(at.spengergasse.myproject.model.Verkehrsmittel::getLinie).setHeader("Linie").setAutoWidth(true);
        grid.addColumn(at.spengergasse.myproject.model.Verkehrsmittel::getKapazitaet).setHeader("Kapazität").setAutoWidth(true);
        grid.addColumn(v -> String.format("%.3f", v.getCo2ProKm())).setHeader("CO₂/km").setAutoWidth(true);
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.setHeight("340px");
        refreshGrid();

        // ── Buttons ────────────────────────────────────────
        Button neuButton        = new Button("＋ Neu");
        Button bearbeitenButton = new Button("✎ Bearbeiten");
        Button loeschenButton   = new Button("✕ Löschen");
        Button exportButton     = new Button("↑ CSV Export");
        Button importButton     = new Button("↓ CSV Import");

        neuButton.addClassName("btn-primary");
        bearbeitenButton.addClassName("btn-secondary");
        loeschenButton.addClassName("btn-danger");
        exportButton.addClassName("btn-secondary");
        importButton.addClassName("btn-secondary");

        neuButton.addClickListener(e -> openAddDialog(null));

        bearbeitenButton.addClickListener(e -> {
            at.spengergasse.myproject.model.Verkehrsmittel sel = grid.asSingleSelect().getValue();
            if (sel == null) { error("Bitte zuerst ein Verkehrsmittel auswählen!"); return; }
            openAddDialog(sel);
        });

        loeschenButton.addClickListener(e -> {
            at.spengergasse.myproject.model.Verkehrsmittel sel = grid.asSingleSelect().getValue();
            if (sel == null) { error("Bitte zuerst ein Verkehrsmittel auswählen!"); return; }
            openDeleteDialog(sel);
        });

        exportButton.addClickListener(e -> doExport());
        importButton.addClickListener(e -> openImportDialog());

        HorizontalLayout buttons = new HorizontalLayout(
                neuButton, bearbeitenButton, loeschenButton, exportButton, importButton);
        buttons.getStyle().set("flex-wrap", "wrap");
        buttons.addClassName("gap-sm");
        buttons.addClassName("mt-md");

        VerticalLayout card = new VerticalLayout(titel, grid, buttons);
        card.addClassName("bahn-card");
        card.setPadding(false);
        card.setSpacing(true);

        add(card);
        setSizeFull();
        getStyle().set("padding", "32px");
    }

    private void refreshGrid() {
        grid.setItems(verwaltung.getVerkehrsmittel());
    }

    // ── Add / Edit Dialog ──────────────────────────────────
    private void openAddDialog(at.spengergasse.myproject.model.Verkehrsmittel existing) {
        Dialog dialog = new Dialog();
        dialog.setWidth("480px");

        H3 dialogTitel = new H3(existing == null ? "Neues Verkehrsmittel" : "Bearbeiten");

        ComboBox<String> typBox = new ComboBox<>("Typ");
        typBox.setItems("Zug", "Strassenbahn", "Bus");
        typBox.setWidth("100%");

        TextField idField    = new TextField("ID / Name");
        TextField linieField = new TextField("Linie");
        TextField kapField   = new TextField("Kapazität");
        TextField co2Field   = new TextField("CO₂ pro km");
        for (TextField f : new TextField[]{idField, linieField, kapField, co2Field})
            f.setWidth("100%");

        // Zug
        TextField zugTypField  = new TextField("Zugtyp (z. B. ICE)");
        TextField antriebField = new TextField("Antriebsart (z. B. Elektro)");
        zugTypField.setWidth("100%"); antriebField.setWidth("100%");
        VerticalLayout zugFelder = new VerticalLayout(zugTypField, antriebField);
        zugFelder.setPadding(false); zugFelder.setSpacing(true); zugFelder.setVisible(false);

        // Strassenbahn
        TextField waggonsField      = new TextField("Anzahl Waggons");
        TextField haltestelleSbField = new TextField("Haltestelle");
        waggonsField.setWidth("100%"); haltestelleSbField.setWidth("100%");
        VerticalLayout sbFelder = new VerticalLayout(waggonsField, haltestelleSbField);
        sbFelder.setPadding(false); sbFelder.setSpacing(true); sbFelder.setVisible(false);

        // Bus
        TextField kennzeichenField    = new TextField("Kennzeichen");
        TextField kraftstoffField     = new TextField("Kraftstoffart");
        TextField verbrauchField      = new TextField("Verbrauch pro km");
        TextField haltestelleBusField = new TextField("Haltestelle");
        for (TextField f : new TextField[]{kennzeichenField, kraftstoffField, verbrauchField, haltestelleBusField})
            f.setWidth("100%");
        VerticalLayout busFelder = new VerticalLayout(kennzeichenField, kraftstoffField, verbrauchField, haltestelleBusField);
        busFelder.setPadding(false); busFelder.setSpacing(true); busFelder.setVisible(false);

        typBox.addValueChangeListener(ev -> {
            zugFelder.setVisible("Zug".equals(ev.getValue()));
            sbFelder.setVisible("Strassenbahn".equals(ev.getValue()));
            busFelder.setVisible("Bus".equals(ev.getValue()));
        });

        // Vorausfüllen bei Edit
        if (existing != null) {
            idField.setValue(existing.getId());
            linieField.setValue(existing.getLinie());
            kapField.setValue(String.valueOf(existing.getKapazitaet()));
            co2Field.setValue(String.valueOf(existing.getCo2ProKm()));
            if (existing instanceof at.spengergasse.myproject.model.Strassenbahn s) {
                typBox.setValue("Strassenbahn");
                waggonsField.setValue(String.valueOf(s.getAnzahlWaggons()));
                haltestelleSbField.setValue(s.getHaltestelle());
            } else if (existing instanceof at.spengergasse.myproject.model.Bus b) {
                typBox.setValue("Bus");
                kennzeichenField.setValue(b.getKennzeichen());
                kraftstoffField.setValue(b.getKraftstoffArt());
                verbrauchField.setValue(String.valueOf(b.getVerbrauchProKm()));
                haltestelleBusField.setValue(b.getHaltestelle());
            }
            typBox.setReadOnly(true);
        }

        Button speichern = new Button("Speichern");
        Button abbrechen = new Button("Abbrechen");
        speichern.addClassName("btn-primary");
        abbrechen.addClassName("btn-secondary");

        speichern.addClickListener(e -> {
            if (typBox.isEmpty())    { error("Bitte Typ auswählen!"); return; }
            if (idField.isEmpty())   { error("ID / Name darf nicht leer sein!"); return; }
            if (linieField.isEmpty()){ error("Linie darf nicht leer sein!"); return; }
            if (kapField.isEmpty())  { error("Kapazität darf nicht leer sein!"); return; }
            if (co2Field.isEmpty())  { error("CO₂ darf nicht leer sein!"); return; }

            int kap;
            double co2;
            try {
                kap = Integer.parseInt(kapField.getValue().trim());
                co2 = Double.parseDouble(co2Field.getValue().trim());
                if (kap <= 0) { error("Kapazität muss größer als 0 sein!"); return; }
                if (co2 < 0)  { error("CO₂ darf nicht negativ sein!"); return; }
            } catch (NumberFormatException ex) {
                error("Kapazität und CO₂ müssen Zahlen sein!"); return;
            }

            if (existing != null) verwaltung.entferneVerkehrsmittel(existing);

            try {
                switch (typBox.getValue()) {
                    case "Zug" -> {
                        if (zugTypField.isEmpty() || antriebField.isEmpty()) {
                            error("Bitte Zugtyp und Antriebsart ausfüllen!"); return;
                        }
                        verwaltung.hinzufuegenVerkehrsmittel(new at.spengergasse.myproject.model.Zug(
                                idField.getValue(), zugTypField.getValue(),
                                kap, co2, antriebField.getValue(), linieField.getValue()));
                    }
                    case "Strassenbahn" -> {
                        if (waggonsField.isEmpty() || haltestelleSbField.isEmpty()) {
                            error("Bitte Waggons und Haltestelle ausfüllen!"); return;
                        }
                        verwaltung.hinzufuegenVerkehrsmittel(new at.spengergasse.myproject.model.Strassenbahn(
                                idField.getValue(), linieField.getValue(), kap, co2,
                                Integer.parseInt(waggonsField.getValue().trim()),
                                haltestelleSbField.getValue()));
                    }
                    case "Bus" -> {
                        if (kennzeichenField.isEmpty() || kraftstoffField.isEmpty()
                                || verbrauchField.isEmpty() || haltestelleBusField.isEmpty()) {
                            error("Bitte alle Bus-Felder ausfüllen!"); return;
                        }
                        verwaltung.hinzufuegenVerkehrsmittel(new at.spengergasse.myproject.model.Bus(
                                idField.getValue(), linieField.getValue(), kap, co2,
                                kennzeichenField.getValue(), kraftstoffField.getValue(),
                                Double.parseDouble(verbrauchField.getValue().trim()),
                                haltestelleBusField.getValue()));
                    }
                }
            } catch (NumberFormatException ex) {
                error("Ungültige Zahl eingegeben!"); return;
            }

            refreshGrid();
            success(existing == null ? "Verkehrsmittel hinzugefügt!" : "Verkehrsmittel aktualisiert!");
            dialog.close();
        });

        abbrechen.addClickListener(e -> dialog.close());

        HorizontalLayout dialogButtons = new HorizontalLayout(speichern, abbrechen);
        dialogButtons.addClassName("gap-sm");

        VerticalLayout content = new VerticalLayout(
                dialogTitel, typBox, idField, linieField, kapField, co2Field,
                zugFelder, sbFelder, busFelder, dialogButtons);
        content.setPadding(true);
        content.setSpacing(true);
        dialog.add(content);
        dialog.open();
    }

    // ── Delete Dialog ──────────────────────────────────────
    private void openDeleteDialog(at.spengergasse.myproject.model.Verkehrsmittel v) {
        Dialog dialog = new Dialog();
        dialog.setWidth("360px");

        H3 frage   = new H3("Wirklich löschen?");
        Paragraph info = new Paragraph(v.getId() + " (" + v.getClass().getSimpleName() + ") wird gelöscht.");

        Button ja   = new Button("Ja, löschen");
        Button nein = new Button("Abbrechen");
        ja.addClassName("btn-danger");
        nein.addClassName("btn-secondary");

        ja.addClickListener(e -> {
            verwaltung.entferneVerkehrsmittel(v);
            refreshGrid();
            success(v.getId() + " wurde gelöscht.");
            dialog.close();
        });
        nein.addClickListener(e -> dialog.close());

        HorizontalLayout btns = new HorizontalLayout(ja, nein);
        btns.addClassName("gap-sm");
        dialog.add(new VerticalLayout(frage, info, btns));
        dialog.open();
    }

    // ── CSV Export ─────────────────────────────────────────
    private void doExport() {
        try {
            Path tmp = Files.createTempFile("bahn_export_", ".csv");
            verwaltung.exportCSV(tmp.toString());

            StreamResource resource = new StreamResource("bahn_export.csv", () -> {
                try { return new FileInputStream(tmp.toFile()); }
                catch (FileNotFoundException ex) { return InputStream.nullInputStream(); }
            });

            Anchor anchor = new Anchor(resource, "");
            anchor.getElement().setAttribute("download", true);
            anchor.getElement().setAttribute("style", "display:none");
            add(anchor);
            anchor.getElement().executeJs("this.click(); setTimeout(()=>this.remove(),1000);");
            success("CSV Export erfolgreich!");

        } catch (TrainStationException | IOException ex) {
            error("Export fehlgeschlagen: " + ex.getMessage());
        }
    }

    // ── CSV Import Dialog ──────────────────────────────────
    private void openImportDialog() {
        Dialog dialog = new Dialog();
        dialog.setWidth("420px");

        H3 titel = new H3("CSV Import");
        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setMaxFiles(1);
        upload.setAcceptedFileTypes(".csv", "text/csv");

        upload.addSucceededListener(e -> {
            try {
                Path tmp = Files.createTempFile("bahn_import_", ".csv");
                try (OutputStream os = new FileOutputStream(tmp.toFile())) {
                    buffer.getInputStream().transferTo(os);
                }
                verwaltung.importCSV(tmp.toString());
                refreshGrid();
                success("Import erfolgreich! " + verwaltung.getVerkehrsmittel().size() + " Einträge geladen.");
                dialog.close();
            } catch (TrainStationException | IOException ex) {
                error("Import fehlgeschlagen: " + ex.getMessage());
            }
        });

        Button abbrechen = new Button("Abbrechen");
        abbrechen.addClassName("btn-secondary");
        abbrechen.addClickListener(e -> dialog.close());

        dialog.add(new VerticalLayout(titel, upload, abbrechen));
        dialog.open();
    }

    // ── Helpers ────────────────────────────────────────────
    private void success(String msg) {
        Notification n = Notification.show(msg, 3000, Notification.Position.BOTTOM_END);
        n.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    private void error(String msg) {
        Notification n = Notification.show(msg, 4000, Notification.Position.BOTTOM_END);
        n.addThemeVariants(NotificationVariant.LUMO_ERROR);
    }
}