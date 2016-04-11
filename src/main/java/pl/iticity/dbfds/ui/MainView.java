package pl.iticity.dbfds.ui;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.vaadin.data.Property;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import pl.iticity.dbfds.model.DocumentInfo;
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

    TabSheet tabSheet;

    @Override
    public void initView() {
        tabSheet = new TabSheet();
        tabSheet.setSizeFull();

        searchDocumentsTab.mainView=this;

        tabSheet.addTab(recentDocumentsTab,"Recent Documents");
        tabSheet.addTab(searchDocumentsTab,"Search Documents");
        tabSheet.addTab(documentDetailsTab,"Create Document");
        tabSheet.addSelectedTabChangeListener(new TabSheet.SelectedTabChangeListener() {
            @Override
            public void selectedTabChange(TabSheet.SelectedTabChangeEvent selectedTabChangeEvent) {
                refreshDynamicContent();
            }
        });

        addComponent(tabSheet);
    }

    public void addTab(Refreshable component, String title){
        boolean flag = true;
        if(component instanceof DocumentDetailsTab){
            DocumentInfo di = ((DocumentDetailsTab)component).documentInfo;
            for(int i =0; i<tabSheet.getComponentCount();i++){
                TabSheet.Tab tab = tabSheet.getTab(i);
                if(tab.getComponent() instanceof DocumentDetailsTab){
                    DocumentInfo di2 = ((DocumentDetailsTab)tab.getComponent()).documentInfo;
                    if(di.getId()!=null && di.getId().equals(di2.getId())){
                        flag=false;
                        break;
                    }
                }
            }
        }
        if(flag) {
            TabSheet.Tab t = tabSheet.addTab((Component) component, title);
            component.refresh();
            t.setClosable(true);
        }
    }

    @Override
    public void refreshDynamicContent() {
        for(int i =0; i<tabSheet.getComponentCount();i++){
            TabSheet.Tab t = tabSheet.getTab(i);
            if(t.getComponent() instanceof Refreshable){
                ((Refreshable)t.getComponent()).refresh();
                if(t.getComponent() instanceof DocumentDetailsTab){
                    DocumentDetailsTab ddt = (DocumentDetailsTab) t.getComponent();
                    if(ddt.documentInfo.getDocumentName()!=null){
                        t.setCaption(ddt.documentInfo.getDocumentName());
                    }
                }
            }
        }
    }


    @Override
    public void onEnter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        if (!PrincipalUtils.isAuthenticated()) {
            getUI().getNavigator().navigateTo(IticityUI.DEFAULT_VIEW);
        }
        refreshDynamicContent();
    }

}
