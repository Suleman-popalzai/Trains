package at.spengergasse.myproject.views.add;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Add")
@Route("add")
@Menu(order = 1, icon = LineAwesomeIconUrl.PLUS_SOLID)
public class AddView extends VerticalLayout {

    public AddView() {

        addClassName("bahn-page");
        setPadding(false);
        setSpacing(false);

        // ── Titel ──────────────────────────────────────────────
        H1 titel = new H1("Neues Verkehrsmittel");
        titel.addClassName("bahn-title");

        // ── Felder ─────────────────────────────────────────────
        TextField nameField      = new TextField("Name");
        TextField typField       = new TextField("Typ");
        TextField kapazitaetField = new TextField("Kapazität");

        nameField.setPlaceholder("z. B. ICE 101");
        typField.setPlaceholder("z. B. Zug, Bus, Straßenbahn");
        kapazitaetField.setPlaceholder("z. B. 300");

        nameField.setRequired(true);
        typField.setRequired(true);
        kapazitaetField.setRequired(true);

        nameField.setWidth("400px");
        typField.setWidth("400px");
        kapazitaetField.setWidth("400px");

        // ── Speichern Button ───────────────────────────────────
        Button speichernButton = new Button("Speichern");
        speichernButton.addClassName("btn-primary");
        speichernButton.getStyle().set("margin-top", "8px");

        speichernButton.addClickListener(e -> {
            if (nameField.isEmpty() || typField.isEmpty() || kapazitaetField.isEmpty()) {
                Notification n = Notification.show("Bitte alle Felder ausfüllen!", 3000, Notification.Position.BOTTOM_END);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }
            Notification n = Notification.show("✓ " + nameField.getValue() + " gespeichert!", 3000, Notification.Position.BOTTOM_END);
            n.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            nameField.clear();
            typField.clear();
            kapazitaetField.clear();
        });

        // ── Card ───────────────────────────────────────────────
        VerticalLayout card = new VerticalLayout(titel, nameField, typField, kapazitaetField, speichernButton);
        card.addClassName("bahn-card");
        card.setPadding(false);
        card.setSpacing(true);
        card.setMaxWidth("520px");

        add(card);
        getStyle().set("padding", "32px");
    }
}