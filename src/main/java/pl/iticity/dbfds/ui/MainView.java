package pl.iticity.dbfds.ui;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.vaadin.data.Property;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import pl.iticity.dbfds.ui.documents.DocumentDetailsTab;
import pl.iticity.dbfds.ui.documents.RecentDocumentsTab;
import pl.iticity.dbfds.ui.documents.SearchDocumentsTab;
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
    SearchDocumentsTab searchDocumentsTab;

    @Autowired
    DocumentDetailsTab documentDetailsTab;

    @Autowired
    RecentDocumentsTab recentDocumentsTab;

    @Override
    public void initView() {
        TabSheet tabSheet = new TabSheet();
        tabSheet.setSizeFull();

        tabSheet.addTab(recentDocumentsTab,"Recent Documents");
        tabSheet.addTab(searchDocumentsTab,"Search Documents");
        tabSheet.addTab(documentDetailsTab,"Create Document");

        addComponent(tabSheet);
    }


    @Override
    public void refreshDynamicContent() {
        searchDocumentsTab.refresh();
        recentDocumentsTab.refresh();
        documentDetailsTab.refresh();
    }


    @Override
    public void onEnter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        if (!PrincipalUtils.isAuthenticated()) {
            getUI().getNavigator().navigateTo(IticityUI.DEFAULT_VIEW);
        }
    }

}
