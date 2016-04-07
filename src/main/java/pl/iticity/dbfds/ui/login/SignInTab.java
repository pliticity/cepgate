package pl.iticity.dbfds.ui.login;

import com.vaadin.data.util.BeanItem;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.commons.lang.StringUtils;
import pl.iticity.dbfds.model.Principal;
import pl.iticity.dbfds.ui.IticityUI;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.springframework.context.annotation.Scope;
import pl.iticity.dbfds.util.PrincipalUtils;

/**
 * Created by pmajchrz on 3/30/16.
 */
@Scope("prototype")
@org.springframework.stereotype.Component
public class SignInTab extends VerticalLayout {

    public SignInTab() {
        setSizeUndefined();
        setSpacing(true);
        addComponent(buildFields());
    }

    private Component buildFields() {
        HorizontalLayout fields = new HorizontalLayout();
        fields.setSpacing(true);
        fields.setSizeUndefined();
        fields.addStyleName("fields");

        final Principal principal = new Principal();
        principal.setEmail("dachoin@gmail.com");
        principal.setPassword("dachoin@gmail.com");

        BeanItem<Principal> item = new BeanItem<Principal>(principal);

        final TextField email = new TextField("Email",item.getItemProperty("email"));
        email.setValidationVisible(false);
        email.setNullRepresentation(StringUtils.EMPTY);
        final PasswordField password = new PasswordField("Password",item.getItemProperty("password"));
        password.setValidationVisible(false);
        password.setNullRepresentation(StringUtils.EMPTY);

        final Button signin = new Button("Sign In");
        signin.addStyleName(ValoTheme.BUTTON_PRIMARY);
        signin.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        //signin.focus();

        fields.addComponents(email, password, signin);
        fields.setComponentAlignment(signin, Alignment.BOTTOM_LEFT);

        signin.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                AuthenticationToken token = new AuthenticationToken() {

                    @Override
                    public Object getPrincipal() {
                        return principal;
                    }

                    @Override
                    public Object getCredentials() {
                        return PrincipalUtils.hashPassword(principal.getPassword(),null);
                    }
                };
                Subject currentUser = SecurityUtils.getSubject();
                currentUser.login(token);

                getUI().getNavigator().navigateTo(IticityUI.MAIN_VIEW);
            }
        });
        return fields;
    }
}
