package at.spengergasse.myproject.views.helloworld;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Suche")
@Route("suche")
@Menu(order = 2, icon = LineAwesomeIconUrl.SEARCH_SOLID)
public class HelloWorldView extends VerticalLayout {

    private final TextField suche;
    private final Button suchen;

    public HelloWorldView() {

        addClassName("bahn-page");
        setPadding(false);
        setSpacing(false);

        // ── Titel ──────────────────────────────────────────────
        H1 titel = new H1("Verkehrsmittel suchen");
        titel.addClassName("bahn-title");

        // ── Suchfeld + Button ──────────────────────────────────
        suche  = new TextField();
        suche.setPlaceholder("z. B. ICE, Bus 14A ...");
        suche.setWidth("360px");

        suchen = new Button("Suchen");
        suchen.addClassName("btn-primary");

        suchen.addClickListener(e -> doSearch());
        suchen.addClickShortcut(Key.ENTER);

        HorizontalLayout searchRow = new HorizontalLayout(suche, suchen);
        searchRow.setAlignItems(Alignment.END);
        searchRow.addClassName("gap-sm");

        // ── Card ───────────────────────────────────────────────
        VerticalLayout card = new VerticalLayout(titel, searchRow);
        card.addClassName("bahn-card");
        card.setPadding(false);
        card.setSpacing(true);
        card.setMaxWidth("600px");

        add(card);
        getStyle().set("padding", "32px");
    }

    private void doSearch() {
        if (suche.isEmpty()) {
            Notification n = Notification.show("Bitte Suchbegriff eingeben!", 3000, Notification.Position.BOTTOM_END);
            n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }
        Notification n = Notification.show("Suche nach: " + suche.getValue(), 3000, Notification.Position.BOTTOM_END);
        n.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }
}