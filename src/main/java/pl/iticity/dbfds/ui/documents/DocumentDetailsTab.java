package pl.iticity.dbfds.ui.documents;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.DefaultFieldGroupFieldFactory;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.validator.BeanValidator;
import com.vaadin.ui.*;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.iticity.dbfds.model.Classification;
import pl.iticity.dbfds.model.DocumentInfo;
import pl.iticity.dbfds.model.Principal;
import pl.iticity.dbfds.repository.PrincipalRepository;
import pl.iticity.dbfds.service.DocumentService;
import pl.iticity.dbfds.service.PrincipalService;
import pl.iticity.dbfds.ui.Refreshable;
import pl.iticity.dbfds.util.PrincipalUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Calendar;
import java.util.logging.Logger;

/**
 * Created by pmajchrz on 4/7/16.
 */
@Scope("prototype")
@Component
public class DocumentDetailsTab extends FormLayout implements Refreshable{

    @Autowired
    DocumentService documentService;

    @Autowired
    PrincipalRepository principalRepository;

    public DocumentDetailsTab() {
        setSpacing(true);
        setMargin(true);
    }

    @Override
    public void refresh() {
        removeAllComponents();
        final BeanFieldGroup<DocumentInfo> binder = new BeanFieldGroup<DocumentInfo>(DocumentInfo.class);

        DocumentInfo documentInfo = new DocumentInfo();
        documentInfo.setClassification(new Classification());
        documentInfo.setCreatedBy(PrincipalUtils.getCurrentPrincipal());

        documentInfo.setCreationDate(DateTime.now(DateTimeZone.forID("Europe/Oslo")));
        documentInfo.setMasterDocumentNumber(documentService.getNextMasterDocumentNumber());
        documentInfo.setDocumentNumber(String.valueOf(documentInfo.getMasterDocumentNumber()));

        binder.setItemDataSource(documentInfo);
        class FF extends DefaultFieldGroupFieldFactory{

            @Override
            public <T extends Field> T createField(Class<?> type, Class<T> fieldType) {
                if(type.equals(DocumentInfo.Type.class) && fieldType.equals(ComboBox.class)){
                    return (T) new ComboBox();
                }
                if(type.equals(DocumentInfo.Kind.class) && fieldType.equals(ComboBox.class)){
                    return (T) new ComboBox();
                }
                return super.createField(type, fieldType);
            }
        }
        binder.setFieldFactory(new FF());

        TextField masterDocumentNumber = buildField(binder,"Master Document Number","masterDocumentNumber");
        masterDocumentNumber.setEnabled(false);
        TextField documentNumber = buildField(binder,"Document Number","documentNumber");
        TextField documentName = buildField(binder,"Document Name","documentName");
        TextField classificationId = buildField(binder,"Classification Id","classification.classificationId");
        classificationId.addValidator(new BeanValidator(Classification.class, "classificationId"));
        TextField classificationName = buildField(binder,"Classification Name","classification.name");
        classificationName.addValidator(new BeanValidator(Classification.class, "name"));

        class DTTD implements Converter<Date,DateTime>{


            @Override
            public DateTime convertToModel(Date date, Class<? extends DateTime> aClass, Locale locale) throws ConversionException {
                return new DateTime(date.getTime(),DateTimeZone.forID("Europe/Oslo"));
            }

            @Override
            public Date convertToPresentation(DateTime dateTime, Class<? extends Date> aClass, Locale locale) throws ConversionException {
                Date d = new Date(dateTime.getMillis());
                return d;
            }

            @Override
            public Class<DateTime> getModelType() {
                return DateTime.class;
            }

            @Override
            public Class<Date> getPresentationType() {
                return Date.class;
            }
        }
        DateField creationDate = new DateField("Document Creation Date");
        creationDate.setConverter(new DTTD());
        binder.bind(creationDate,"creationDate");
        creationDate.setTimeZone(TimeZone.getTimeZone("Europe/Oslo"));
        creationDate.setDateFormat("dd/MM/yyyy HH:mm");
        creationDate.setEnabled(false);
        creationDate.setValidationVisible(false);
        addComponent(creationDate);

        class PTSC implements Converter<String,Principal>{

            @Override
            public Principal convertToModel(String s, Class<? extends Principal> aClass, Locale locale) throws ConversionException {
                return principalRepository.findByEmail(s == null ? StringUtils.EMPTY : s);
            }

            @Override
            public String convertToPresentation(Principal principal, Class<? extends String> aClass, Locale locale) throws ConversionException {
                return principal == null? StringUtils.EMPTY : principal.getEmail();
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
        binder.bind(createdBy,"createdBy");
        addComponent(createdBy);
        createdBy.setEnabled(false);

        ComboBox documentType = binder.buildAndBind("Document Type","type",ComboBox.class);
        documentType.addItems(DocumentInfo.Type.values());
        documentType.setTextInputAllowed(false);
        documentType.setNullSelectionAllowed(false);
        documentType.setValidationVisible(false);
        addComponent(documentType);

        ComboBox documentKind = binder.buildAndBind("Document placement","kind",ComboBox.class);
        documentKind.addItems(DocumentInfo.Kind.values());
        documentKind.setTextInputAllowed(false);
        documentKind.setNullSelectionAllowed(false);
        documentKind.setValidationVisible(false);
        addComponent(documentKind);

        HorizontalLayout buttonsWrap = new HorizontalLayout();

        Button save = new Button("Save");
        save.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                try {
                    binder.commit();
                } catch (FieldGroup.CommitException e) {
                    Logger.getAnonymousLogger().throwing(DocumentDetailsTab.class.getName(),"save.click",e);
                    for(int i=0; i<getComponentCount();i++){
                        com.vaadin.ui.Component c = getComponent(i);
                        if(c instanceof TextField){
                            ((TextField)c).setValidationVisible(true);
                        }
                        if(c instanceof ComboBox){
                            ((ComboBox)c).setValidationVisible(true);
                        }
                        if(c instanceof DateField){
                            ((DateField)c).setValidationVisible(true);
                        }
                    }
                }
            }
        });

        Button cancel = new Button("Cancel");
        Button upload = new Button("Upload Document");
        Button close = new Button("Close Tab");
        buttonsWrap.setDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);
        buttonsWrap.addComponents(save,cancel,upload,close);

        addComponent(buttonsWrap);
    }

    private TextField buildField(BeanFieldGroup<DocumentInfo> binder, String caption,String id){
        TextField f = binder.buildAndBind(caption,id,TextField.class);
        f.setNullRepresentation(StringUtils.EMPTY);
        f.setValidationVisible(false);
        addComponent(f);
        return f;
    }
}
