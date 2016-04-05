package pl.iticity.dbfds.ui;

import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by pmajchrz on 4/4/16.
 */
public class ConfirmationWindow extends Window {

    public ConfirmationWindow(UI ui,String title, String text, Button.ClickListener confirmAction) {
        setModal(true);
        setResizable(false);
        setDraggable(false);

        VerticalLayout vl = new VerticalLayout();
        vl.addStyleName("window-confirmation");
        vl.setSizeUndefined();

        Label titleLabel = new Label(title);
        titleLabel.addStyleName(ValoTheme.LABEL_COLORED);
        titleLabel.addStyleName(ValoTheme.LABEL_H3);
        vl.addComponent(titleLabel);
        vl.setComponentAlignment(titleLabel, Alignment.MIDDLE_LEFT);

        Label textLabel = new Label(text);
        vl.addComponent(textLabel);
        vl.setComponentAlignment(textLabel, Alignment.MIDDLE_CENTER);

        HorizontalLayout hl = new HorizontalLayout();
        vl.addComponent(hl);
        hl.addStyleName(pl.iticity.dbfds.util.UIConstants.WINDOW_FORM_BUTTON);
        hl.addStyleName("top-bar-button");

        Button yes = new Button("Confirm");
        yes.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        yes.addClickListener(confirmAction);
        hl.addComponent(yes);
        hl.setComponentAlignment(yes,Alignment.MIDDLE_RIGHT);

        Button no = new Button("Cancel");
        no.addStyleName(ValoTheme.BUTTON_DANGER);
        no.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                close();
            }
        });
        hl.addComponent(no);
        hl.setComponentAlignment(no,Alignment.MIDDLE_RIGHT);
        setContent(vl);
        setSizeUndefined();
        ui.addWindow(this);
        setVisible(true);
    }
}
