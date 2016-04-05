package pl.iticity.dbfds.ui;

import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by pmajchrz on 4/4/16.
 */
public class AbstractWindow extends Window {

    protected VerticalLayout layout;

    public AbstractWindow(UI ui, String title) {
        setModal(true);
        setResizable(false);
        setDraggable(false);

        layout = new VerticalLayout();
        layout.addStyleName("window-confirmation");
        layout.setSizeUndefined();

        Label titleLabel = new Label(title);
        titleLabel.addStyleName(ValoTheme.LABEL_COLORED);
        titleLabel.addStyleName(ValoTheme.LABEL_H3);
        layout.addComponent(titleLabel);
        layout.setComponentAlignment(titleLabel, Alignment.MIDDLE_LEFT);

        setContent(layout);
        setSizeUndefined();
        ui.addWindow(this);
        setVisible(true);
    }
}
