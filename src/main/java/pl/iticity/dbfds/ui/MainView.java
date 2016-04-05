package pl.iticity.dbfds.ui;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.vaadin.data.Property;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import pl.iticity.dbfds.ui.settings.SettingsPanel;
import pl.iticity.dbfds.util.PrincipalUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import javax.annotation.Nullable;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@Scope("prototype")
@org.springframework.stereotype.Component
public class MainView extends AbstractView {


    @Autowired
    SettingsPanel settingsPanel;

    private HorizontalLayout mainLayout;

    @Override
    public void initView() {
    }


    @Override
    public void refreshDynamicContent() {
    }


    @Override
    public void onEnter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        if (!PrincipalUtils.isAuthenticated()) {
            getUI().getNavigator().navigateTo(IticityUI.DEFAULT_VIEW);
        }
    }

}
