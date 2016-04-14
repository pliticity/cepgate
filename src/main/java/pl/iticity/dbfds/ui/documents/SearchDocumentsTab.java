package pl.iticity.dbfds.ui.documents;

import com.vaadin.data.util.AbstractBeanContainer;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import pl.iticity.dbfds.model.DocumentActivity;
import pl.iticity.dbfds.model.DocumentInfo;
import pl.iticity.dbfds.repository.DocumentInfoRepository;
import pl.iticity.dbfds.ui.MainView;
import pl.iticity.dbfds.ui.Refreshable;
import pl.iticity.dbfds.util.PrincipalUtils;

import javax.annotation.PostConstruct;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by pmajchrz on 4/7/16.
 */
@Scope("prototype")
@org.springframework.stereotype.Component
public class SearchDocumentsTab extends VerticalLayout implements Refreshable {

    @Autowired
    DocumentInfoRepository documentInfoRepository;

    @Autowired
    ApplicationContext applicationContext;

    public MainView mainView;
    BeanContainer<String, DocumentInfo> docBean;

    public SearchDocumentsTab() {
    }

    public String input = null;

    @PostConstruct
    public void postConstruct() {
        HorizontalLayout hl = new HorizontalLayout();
        final TextField searchField = new TextField();

        final Button button = new Button("Search");
        if ((this instanceof RecentDocumentsTab)) {
            searchField.setVisible(false);
            button.setVisible(false);
        }
        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                input = searchField.getValue();
                refresh();
            }
        });
        searchField.setImmediate(true);
        OnEnterKeyHandler onEnterHandler=new OnEnterKeyHandler(){
            @Override
            public void onEnterKeyPressed() {
                input = searchField.getValue();
                refresh();
            }
        };
        onEnterHandler.installOn(searchField);
        hl.addComponent(searchField);
        hl.addComponent(button);
        addComponent(hl);


        docBean = new BeanContainer<String, DocumentInfo>(DocumentInfo.class);
        docBean.setBeanIdResolver(new AbstractBeanContainer.BeanIdResolver<String, DocumentInfo>() {
            @Override
            public String getIdForBean(DocumentInfo documentInfo) {
                return documentInfo.getId();
            }
        });
        docBean.addNestedContainerBean("classification");
        docBean.addAll(getDocs());
        Table table = new Table(null, docBean);
        table.setSelectable(true);
        table.setVisible(true);
        table.setVisibleColumns("classification.classificationId", "classification.name", "documentNumber", "type", "documentName", "creationDate", "createdBy", "noOfFiles");
        table.setColumnHeader("classification.classificationId","Classification Id");
        table.setColumnHeader("classification.name","Classification Name");
        table.setColumnHeader("documentNumber","Document Number");
        table.setColumnHeader("type","Document Type");
        table.setColumnHeader("documentName","Document Name");
        table.setColumnHeader("creationDate","Creation Date");
        table.setColumnHeader("createdBy","Created By");
        table.setColumnHeader("noOfFiles","#Files");
        table.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent itemClickEvent) {
                String id = (String) itemClickEvent.getItem().getItemProperty("id").getValue();
                DocumentInfo documentInfo = documentInfoRepository.findOne(id);
                DocumentActivity da = documentInfo.getLastActivity();
                if (da == null) {
                    da = new DocumentActivity();
                    documentInfo.setLastActivity(da);
                }
                Calendar c = Calendar.getInstance(TimeZone.getTimeZone("Europe/Oslo"));
                da.setDate(c.getTime());
                da.setPrincipalId(PrincipalUtils.getCurrentPrincipal().getId());
                documentInfoRepository.save(documentInfo);
                mainView.addTab(applicationContext.getBean(DocumentDetailsTab.class, documentInfo,mainView), documentInfo.getDocumentNumber());
            }
        });
        addComponent(table);
    }

    public List<DocumentInfo> getDocs() {
        if (input == null || StringUtils.isEmpty(input)) {
            return documentInfoRepository.findByCreatedBy(PrincipalUtils.getCurrentPrincipal());
        } else {
            return documentInfoRepository.findByCreatedByAndClassification_ClassificationIdLikeOrClassification_NameLikeOrDocumentNumberLikeOrDocumentNameLike(PrincipalUtils.getCurrentPrincipal(),input,input,input,input);
        }
    }

    @Override
    public void refresh() {
        docBean.removeAllItems();
        docBean.addAll(getDocs());
    }
}
