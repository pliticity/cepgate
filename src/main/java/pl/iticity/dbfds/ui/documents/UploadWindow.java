package pl.iticity.dbfds.ui.documents;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.iticity.dbfds.model.DocumentInfo;
import pl.iticity.dbfds.model.File;
import pl.iticity.dbfds.model.FileInfo;
import pl.iticity.dbfds.repository.FileRepository;
import pl.iticity.dbfds.service.DocumentService;

import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import java.io.*;
import java.util.*;

/**
 * Created by pmajchrz on 4/11/16.
 */
@Scope("prototype")
@Component
public class UploadWindow extends Window{

    @Autowired
    DocumentService documentService;

    @Autowired
    FileRepository fileRepository;

    List<FileInfo> files;
    Table table;
    Upload upload;
    DocumentInfo documentInfo;
    DocumentDetailsTab documentDetailsTab;

    public UploadWindow(final DocumentInfo documentInfo, final DocumentDetailsTab documentDetailsTab) {
        this.documentInfo=documentInfo;
        this.documentDetailsTab=documentDetailsTab;
        setModal(true);
        setDraggable(false);
        setResizable(false);
        addStyleName("upload-window");
        this.files = documentInfo.getFiles();
        if(files==null){
            this.files = Lists.newArrayList();
            documentInfo.setFiles(this.files);
        }

        final VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.addComponent(new Label("Upload files"));
        table = createTable();
        verticalLayout.addComponent(table);

        class FileReceiver implements Upload.Receiver, Upload.SucceededListener {

            public FileReceiver(DocumentInfo documentInfo){
                this.documentInfo = documentInfo;
            }

            DocumentInfo documentInfo;
            String fname;
            String type;
            ByteArrayOutputStream bos;


            @Override
            public OutputStream receiveUpload(String s, String s1) {
                bos = new ByteArrayOutputStream();
                type=s1;
                fname=s;
                return bos;
            }

            @Override
            public void uploadSucceeded(Upload.SucceededEvent succeededEvent) {
                FileInfo fileInfo = new FileInfo();
                fileInfo.setName(fname);
                fileInfo.setType(type);
                fileInfo.setUploadDate(java.util.Calendar.getInstance(TimeZone.getTimeZone("Europe/Oslo")).getTime());

                File file = new File();
                file.setData(bos.toByteArray());
                fileRepository.save(file);
                fileInfo.setFileId(file.getId());

                files.add(fileInfo);
                Table tb = createTable();
                verticalLayout.replaceComponent(table,tb);
                table= tb;

                FileReceiver fileReceiver = new FileReceiver(documentInfo);
                Upload tmp = new Upload(null,fileReceiver);
                tmp.addSucceededListener(fileReceiver);

                verticalLayout.replaceComponent(upload,tmp);
                upload = tmp;
                documentDetailsTab.refresh();
            }
        }

        FileReceiver fileReceiver = new FileReceiver(documentInfo);

        upload = new Upload(null, fileReceiver);
        upload.addSucceededListener(fileReceiver);

        verticalLayout.addComponent(upload);
        setContent(verticalLayout);
        setVisible(true);
    }

    @PostConstruct
    public void postConstruct(){

    }

    private Table createTable() {
        final Table table = new Table("Files");
        table.setHeight("170px");
        table.setVisible(true);
        table.addContainerProperty("Remove", Button.class, null);
        table.addContainerProperty("Name", String.class, StringUtils.EMPTY);
        int i = 0;
        for (FileInfo fileInfo : documentInfo.getFiles()) {
            final FileInfo fi = fileInfo;
            i++;
            //map.put(i,fileInfo);
            Button remove = new Button(FontAwesome.TRASH_O);
            final int finalI = i;
            remove.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    Iterables.removeIf(documentInfo.getFiles(), new Predicate<FileInfo>() {
                        @Override
                        public boolean apply(@Nullable FileInfo fileInfo) {
                            return fi.equals(fileInfo);
                        }
                    });
                    table.removeItem(finalI);
                    documentDetailsTab.refresh();
                }
            });

            remove.addStyleName(ValoTheme.BUTTON_BORDERLESS);
            table.addItem(new Object[]{remove,fileInfo.getName()},i);
        }
        return table;
    }
}
