package pl.iticity.dbfds.ui.documents;

import com.vaadin.data.util.AbstractBeanContainer;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
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

    @PostConstruct
    public void postConstruct() {
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
        table.setVisible(true);
        table.setVisibleColumns("classification.classificationId", "classification.name", "documentNumber", "type", "documentName", "creationDate", "createdBy", "noOfFiles");
        table.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent itemClickEvent) {
                String id = (String) itemClickEvent.getItem().getItemProperty("id").getValue();
                DocumentInfo documentInfo = documentInfoRepository.findOne(id);
                DocumentActivity da = documentInfo.getLastActivity();
                if(da==null){
                    da = new DocumentActivity();
                    documentInfo.setLastActivity(da);
                }
                da.setDate(Calendar.getInstance(TimeZone.getTimeZone("Europe/Oslo")).getTime());
                da.setPrincipalId(PrincipalUtils.getCurrentPrincipal().getId());
                documentInfoRepository.save(documentInfo);
                mainView.addTab(applicationContext.getBean(DocumentDetailsTab.class, documentInfo), documentInfo.getDocumentName());
            }
        });
        addComponent(table);
    }

    public List<DocumentInfo> getDocs(){
        return documentInfoRepository.findAll();
    }

    @Override
    public void refresh() {
        docBean.removeAllItems();
        docBean.addAll(getDocs());
    }
}
