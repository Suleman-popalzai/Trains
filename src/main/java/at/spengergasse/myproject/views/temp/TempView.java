package at.spengergasse.myproject.views.temp;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Statistik")
@Route("statistik")
@Menu(order = 3, icon = LineAwesomeIconUrl.CHART_BAR_SOLID)
public class TempView extends VerticalLayout {

    public TempView() {

        addClassName("bahn-page");
        setPadding(false);
        setSpacing(false);

        // Titel
        H1 titel = new H1("Bahn Statistik");
        titel.addClassName("bahn-title");

        // Stat Cards
        Div cardZuege        = statCard("Züge",                "5");
        Div cardBusse        = statCard("Busse",               "3");
        Div cardStrassenbahn = statCard("Straßenbahnen",       "2");
        Div cardTickets      = statCard("Verkaufte Tickets",   "120");
        Div cardGesamt       = statCard("Gesamt Fahrzeuge",    "10");


        FlexLayout grid = new FlexLayout(cardZuege, cardBusse, cardStrassenbahn, cardTickets, cardGesamt);
        grid.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        grid.addClassName("gap-md");
        grid.getStyle().set("margin-top", "8px");

        // ── Card Wrapper ───────────────────────────────────────
        VerticalLayout card = new VerticalLayout(titel, grid);
        card.addClassName("bahn-card");
        card.setPadding(false);
        card.setSpacing(true);

        add(card);
        getStyle().set("padding", "32px");
    }

    private Div statCard(String label, String value) {
        Div card = new Div();
        card.addClassName("stat-card");

        Span lbl = new Span(label);
        lbl.addClassName("stat-label");

        Span val = new Span(value);
        val.addClassName("stat-value");

        card.add(lbl, val);
        return card;
    }
}