package pl.iticity.dbfds.ui.settings;

import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import pl.iticity.dbfds.util.UIConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

@Scope("prototype")
@org.springframework.stereotype.Component
public class SettingsPanel extends Panel{

    @Autowired
    DomainTab domainTab;

    private TabSheet tabSheet;

    public SettingsPanel() {
       setVisible(false);

        setWidth("400px");
        setHeight(UIConstants.PERCENT_100);
        VerticalLayout verticalLayout = new VerticalLayout();


        Label header = new Label("Settings");
        header.addStyleName(ValoTheme.LABEL_H2);
        header.addStyleName(ValoTheme.LABEL_COLORED);
        header.setSizeUndefined();

        verticalLayout.addComponent(header);
        verticalLayout.setComponentAlignment(header, Alignment.TOP_CENTER);

        tabSheet = new TabSheet();
        tabSheet.setWidth("100%");
        tabSheet.setStyleName(ValoTheme.TABSHEET_FRAMED);

        verticalLayout.addComponent(tabSheet);
        verticalLayout.setComponentAlignment(tabSheet, Alignment.TOP_CENTER);

        setContent(verticalLayout);
    }

    @PostConstruct
    private void initPanel(){
        tabSheet.removeAllComponents();
    }

    public DomainTab getDomainTab() {
        return domainTab;
    }

    public void refresh(){
        initPanel();
    }
}
