package pl.iticity.dbfds.ui.settings;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import pl.iticity.dbfds.security.Principal;
import org.springframework.context.annotation.Scope;

/**
 * Created by pmajchrz on 3/29/16.
 */
@Scope("prototype")
@org.springframework.stereotype.Component
public class DomainTab extends VerticalLayout {



    private Table table;
    private TextField domainName;
    private TextField domainShortName;

    public DomainTab() {
        FormLayout formLayout = new FormLayout();
        addComponent(formLayout);
        formLayout.setStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        Label general = new Label("General");
        general.setSizeUndefined();
        general.addStyleName(ValoTheme.LABEL_H2);
        general.addStyleName(ValoTheme.LABEL_COLORED);
        general.addStyleName("settings-form-header");
        formLayout.addComponent(general);
        //formLayout.setComponentAlignment(general, Alignment.TOP_CENTER);

        domainShortName = new TextField("Short Name");
        domainShortName.setEnabled(false);
        formLayout.addComponent(domainShortName);

        domainName = new TextField("Name");
        formLayout.addComponent(domainName);


        Label users = new Label("User roles");
        users.setSizeUndefined();
        users.addStyleName(ValoTheme.LABEL_H2);
        users.addStyleName(ValoTheme.LABEL_COLORED);
        users.addStyleName("settings-form-header");

        final Button addUser = new Button("Invite User", FontAwesome.PLUS);
        addUser.setStyleName(ValoTheme.BUTTON_BORDERLESS+" top-bar-button");

        formLayout.addComponent(users);
        formLayout.addComponent(addUser);
        formLayout.setComponentAlignment(addUser, Alignment.MIDDLE_RIGHT);
        addUser.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                final Window window = new Window("Invite User");
                window.setResizable(false);
                window.setModal(true);
                window.setDraggable(false);

                HorizontalLayout hl = new HorizontalLayout();

                final TextField user = new TextField();
                hl.addComponent(user);
                Button invite = new Button("Invite");
                hl.addComponent(invite);
                invite.addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent clickEvent) {
                        Notification n = new Notification("User invited");
                        n.show(Page.getCurrent());
                        Principal p = new Principal();
                        //p.setUsername(user.getValue());
                        refresh();
                        window.close();
                    }
                });
                window.setContent(hl);
                getUI().addWindow(window);
                window.setVisible(true);
            }
        });


        addComponent(createPrincipalTable());

    }

    private HorizontalLayout createRow(String labelValue, Component component){
        HorizontalLayout layout = new HorizontalLayout();
        layout.setSizeFull();
        Label label = new Label(labelValue);
        layout.addComponent(label);
        layout.setComponentAlignment(label,Alignment.MIDDLE_LEFT);
        layout.addComponent(component);
        layout.setComponentAlignment(component,Alignment.MIDDLE_LEFT);
        layout.setExpandRatio(label,0.2f);
        layout.setExpandRatio(component,0.8f);
        return layout;
    }


    private Table createPrincipalTable() {
        table = new Table();
        table.setWidth("100%");
        table.setSelectable(false);
        /*table.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent itemClickEvent) {
                Window window = new Window("Domain roles");
                TwinColSelect twinColSelect = new TwinColSelect();
                twinColSelect.addItems(Role.getAll());
                window.setContent(twinColSelect);
                window.center();
                getUI().addWindow(window);
            }
        });*/
        return table;
    }

    public void refresh() {
        //BeanItemContainer source = new BeanItemContainer<Principal>(Principal.class, getUsers());
        table.removeAllItems();
        table.addContainerProperty("User",String.class,null);
        table.addContainerProperty("Active",CheckBox.class,null);
        table.addContainerProperty("Admin",CheckBox.class,null);
        table.addContainerProperty("Edit",Button.class,null);

        int id = 0;

        //table.setContainerDataSource(source);
        table.setVisibleColumns("User","Active","Admin","Edit");
        table.setColumnHeaders("User","Active","Admin","Edit");
    }

    public class EditPermissionsClickListener implements Button.ClickListener{

        private Long pId;

        public EditPermissionsClickListener(Long pId){
            this.pId = pId;
        }

        @Override
        public void buttonClick(Button.ClickEvent clickEvent) {

        }
    }
}
