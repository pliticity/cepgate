package pl.iticity.dbfds.ui.documents;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.gwt.i18n.server.Message;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.DefaultFieldGroupFieldFactory;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.validator.BeanValidator;
import com.vaadin.data.validator.IntegerRangeValidator;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.iticity.dbfds.model.Classification;
import pl.iticity.dbfds.model.DocumentInfo;
import pl.iticity.dbfds.model.FileInfo;
import pl.iticity.dbfds.model.Principal;
import pl.iticity.dbfds.repository.DocumentInfoRepository;
import pl.iticity.dbfds.repository.FileRepository;
import pl.iticity.dbfds.repository.PrincipalRepository;
import pl.iticity.dbfds.service.DocumentService;
import pl.iticity.dbfds.service.PrincipalService;
import pl.iticity.dbfds.ui.MainView;
import pl.iticity.dbfds.ui.Refreshable;
import pl.iticity.dbfds.util.PrincipalUtils;

import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Calendar;
import java.util.logging.Logger;

/**
 * Created by pmajchrz on 4/7/16.
 */
@Scope("prototype")
@Component
public class DocumentDetailsTab extends FormLayout implements Refreshable {

    @Autowired
    DocumentService documentService;

    @Autowired
    DocumentInfoRepository documentInfoRepository;

    @Autowired
    PrincipalRepository principalRepository;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    FileRepository fileRepository;

    private boolean edit;

    public DocumentInfo documentInfo;

    private MainView mainView;

    public DocumentDetailsTab(DocumentInfo documentInfo,MainView mainView) {
        this.mainView=mainView;
        edit=true;
        this.documentInfo = documentInfo;
        if (this.documentInfo == null) {
            this.documentInfo = new DocumentInfo();
        }
        setSpacing(true);
        setMargin(true);
    }

    @PostConstruct
    public void postConstruct() {
        //this.documentInfo = documentInfoRepository.findAll().get(0);
    }

    public DocumentDetailsTab(MainView mainView) {
        this.mainView=mainView;
        edit=false;
        this.documentInfo=new DocumentInfo();
        setSpacing(true);
        setMargin(true);
    }

    public void refreshTable(){
        for(int i=0; i<getComponentCount(); i++){
            if(getComponent(i) instanceof Table){
                replaceComponent(getComponent(i),createTable());
            }
        }
    }

    @Override
    public void refresh() {
        removeAllComponents();
        final BeanFieldGroup<DocumentInfo> binder = new BeanFieldGroup<DocumentInfo>(DocumentInfo.class);

        if (documentInfo.getClassification() == null) {
            documentInfo.setClassification(new Classification());
        }
        if (documentInfo.getCreatedBy() == null) {
            documentInfo.setCreatedBy(PrincipalUtils.getCurrentPrincipal());
        }
        if (documentInfo.getCreationDate() == null) {
            documentInfo.setCreationDate(Calendar.getInstance(TimeZone.getTimeZone("Europe/Oslo")).getTime());
        }
        if (documentInfo.getMasterDocumentNumber() == null) {
            documentInfo.setMasterDocumentNumber(documentService.getNextMasterDocumentNumber());
        }
        if (documentInfo.getDocumentNumber() == null) {
            documentInfo.setDocumentNumber(String.valueOf(documentInfo.getMasterDocumentNumber()));
        }
        binder.setItemDataSource(documentInfo);
        binder.addCommitHandler(new FieldGroup.CommitHandler() {
            @Override
            public void preCommit(FieldGroup.CommitEvent commitEvent) throws FieldGroup.CommitException {

            }

            @Override
            public void postCommit(FieldGroup.CommitEvent commitEvent) throws FieldGroup.CommitException {
                documentInfoRepository.save(documentInfo);
                if(!edit){
                    documentInfo = new DocumentInfo();
                }
                refresh();
            }
        });
        class FF extends DefaultFieldGroupFieldFactory {

            @Override
            public <T extends Field> T createField(Class<?> type, Class<T> fieldType) {
                if (type.equals(DocumentInfo.Type.class) && fieldType.equals(ComboBox.class)) {
                    return (T) new ComboBox();
                }
                if (type.equals(DocumentInfo.Kind.class) && fieldType.equals(ComboBox.class)) {
                    return (T) new ComboBox();
                }
                return super.createField(type, fieldType);
            }
        }
        binder.setFieldFactory(new FF());

        TextField masterDocumentNumber = buildField(binder, "Master Document Number", "masterDocumentNumber");
        masterDocumentNumber.setEnabled(false);
        TextField documentNumber = buildField(binder, "Document Number", "documentNumber");
        //TextField documentName = buildField(binder, "Document Name", "documentName");
        TextArea documentName = binder.buildAndBind("DocumentName","documentName",TextArea.class);
        documentName.setRows(3);
        documentName.setWordwrap(false);
        addComponent(documentName);
        documentName.setNullRepresentation(StringUtils.EMPTY);
        documentName.setValidationVisible(false);

        TextField classificationId = buildField(binder, "Classification Id", "classification.classificationId");
        classificationId.addValidator(new BeanValidator(Classification.class, "classificationId"));
        TextField classificationName = buildField(binder, "Classification Name", "classification.name");
        classificationName.addValidator(new BeanValidator(Classification.class, "name"));

        TextField creationDate = new TextField("Document Creation Date");
        //DateField creationDate = new DateField("Document Creation Date");
        binder.bind(creationDate, "creationDate");
        //creationDate.setTimeZone(TimeZone.getTimeZone("Europe/Oslo"));
        //creationDate.setDateFormat("dd/MM/yyyy HH:mm");
        creationDate.setEnabled(false);
        creationDate.setValidationVisible(false);
        addComponent(creationDate);

        class PTSC implements Converter<String, Principal> {

            @Override
            public Principal convertToModel(String s, Class<? extends Principal> aClass, Locale locale) throws ConversionException {
                return principalRepository.findByEmail(s == null ? StringUtils.EMPTY : s);
            }

            @Override
            public String convertToPresentation(Principal principal, Class<? extends String> aClass, Locale locale) throws ConversionException {
                return principal == null ? StringUtils.EMPTY : principal.getEmail();
            }

            @Override
            public Class<Principal> getModelType() {
                return Principal.class;
            }

            @Override
            public Class<String> getPresentationType() {
                return String.class;
            }
        }
        TextField createdBy = new TextField("Created By");
        createdBy.setConverter(new PTSC());
        createdBy.setNullRepresentation(StringUtils.EMPTY);
        createdBy.setValidationVisible(false);
        binder.bind(createdBy, "createdBy");
        addComponent(createdBy);
        createdBy.setEnabled(false);

        ComboBox documentType = binder.buildAndBind("Document Type", "type", ComboBox.class);
        documentType.addItems(DocumentInfo.Type.values());
        documentType.setTextInputAllowed(false);
        documentType.setNullSelectionAllowed(false);
        documentType.setValidationVisible(false);
        addComponent(documentType);

        ComboBox documentKind = binder.buildAndBind("Document placement", "kind", ComboBox.class);
        documentKind.addItems(DocumentInfo.Kind.values());
        documentKind.setTextInputAllowed(false);
        documentKind.setNullSelectionAllowed(false);
        documentKind.setValidationVisible(false);
        addComponent(documentKind);


        addComponent(createTable());

        HorizontalLayout buttonsWrap = new HorizontalLayout();

        Button save = new Button("Save");
        save.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                try {
                    binder.commit();
                } catch (FieldGroup.CommitException e) {
                    Logger.getAnonymousLogger().throwing(DocumentDetailsTab.class.getName(), "save.click", e);
                    for (int i = 0; i < getComponentCount(); i++) {
                        com.vaadin.ui.Component c = getComponent(i);
                        if (c instanceof TextField) {
                            ((TextField) c).setValidationVisible(true);
                        }
                        if (c instanceof ComboBox) {
                            ((ComboBox) c).setValidationVisible(true);
                        }
                        if (c instanceof DateField) {
                            ((DateField) c).setValidationVisible(true);
                        }
                        if (c instanceof TextArea) {
                            ((TextArea) c).setValidationVisible(true);
                        }
                    }
                }
            }
        });

        Button cancel = new Button("Cancel");
        cancel.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                if(!edit){
                    documentInfo = new DocumentInfo();
                }
                refresh();
            }
        });
        Button upload = new Button("Upload Document");
        final DocumentDetailsTab documentDetailsTab = this;
        upload.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                Window w = applicationContext.getBean(UploadWindow.class, documentInfo, documentDetailsTab);
                getUI().addWindow(w);
            }
        });
        Button close = new Button("Close Tab");
        close.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                mainView.closeTab();
            }
        });
        buttonsWrap.setDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);
        buttonsWrap.addComponents(save, cancel, upload,close);

        addComponent(buttonsWrap);
    }

    private TextField buildField(BeanFieldGroup<DocumentInfo> binder, String caption, String id) {
        TextField f = binder.buildAndBind(caption, id, TextField.class);
        f.setNullRepresentation(StringUtils.EMPTY);
        f.setValidationVisible(false);
        addComponent(f);
        return f;
    }

    private Table createTable() {
        Table table = new Table();
        table.setVisible(true);
        table.addStyleName("minus-margin");
        table.addContainerProperty("Remove", Button.class, null);
        table.addContainerProperty("Download", Button.class, null);
        table.addContainerProperty("Name", String.class, StringUtils.EMPTY);
        table.setHeight("170px");
        table.setWidth("390px");
        int i = 0;
        for (FileInfo fileInfo : documentInfo.getFiles()) {
            final FileInfo fi = fileInfo;
            i++;
            //map.put(i,fileInfo);
            Button remove = new Button(FontAwesome.TRASH_O);
            remove.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    Iterables.removeIf(documentInfo.getFiles(), new Predicate<FileInfo>() {
                        @Override
                        public boolean apply(@Nullable FileInfo fileInfo) {
                            return fi.equals(fileInfo);
                        }
                    });
                    refresh();
                }
            });

            remove.addStyleName(ValoTheme.BUTTON_BORDERLESS);
            final Button download = new Button(FontAwesome.DOWNLOAD);
            download.addStyleName(ValoTheme.BUTTON_BORDERLESS);
            StreamResource.StreamSource streamSource = new StreamResource.StreamSource() {
                @Override
                public InputStream getStream() {
                    pl.iticity.dbfds.model.File file = fileRepository.findOne(fi.getFileId());
                    return new ByteArrayInputStream(file.getData());
                }
            };
            String fname = MessageFormat.format("{0}-{1}-{2}-{3}",documentInfo.getDocumentNumber(),documentInfo.getType(),documentInfo.getDocumentName(),fi.getName());
            FileDownloader downloader = new FileDownloader(new StreamResource(streamSource,fname));
            downloader.extend(download);
            table.addItem(new Object[]{remove,download,fileInfo.getName()},i);
        }
        return table;
    }
}
