package pl.iticity.dbfds.ui.login;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.subject.Subject;
import pl.iticity.dbfds.security.Principal;
import pl.iticity.dbfds.service.PrincipalService;
import pl.iticity.dbfds.ui.IticityUI;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import pl.iticity.dbfds.validator.UniquePrincipalEmailValidator;

import javax.annotation.PostConstruct;

/**
 * Created by pmajchrz on 3/30/16.
 */
@Scope("prototype")
@org.springframework.stereotype.Component
public class SignUpTab extends FormLayout {

    @Autowired
    private PrincipalService principalService;

    @Autowired
    UniquePrincipalEmailValidator uniquePrincipalEmailValidator;

    private Label errorLabel;

    public SignUpTab() {
        setHeight("100%");
        setWidth("300px");
        addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        //setSpacing(true);
        //buildFields();
        //addComponent(addErrorPanel());
    }

    private Component addErrorPanel(){
        Label label = new Label();
        label.setImmediate(true);
        label.setStyleName(ValoTheme.LABEL_FAILURE);
        errorLabel = label;
        errorLabel.setVisible(false);
        return label;
    }

    @PostConstruct
    private void buildFields() {
        final Principal principal = new Principal();

        final BeanFieldGroup<Principal> binder =
                new BeanFieldGroup<Principal>(Principal.class);
        binder.setItemDataSource(principal);
        binder.addCommitHandler(new FieldGroup.CommitHandler() {
            @Override
            public void preCommit(FieldGroup.CommitEvent commitEvent) throws FieldGroup.CommitException {

            }

            @Override
            public void postCommit(FieldGroup.CommitEvent commitEvent) throws FieldGroup.CommitException {
                principalService.registerPrincipal(principal);
                AuthenticationToken token = new AuthenticationToken() {

                    @Override
                    public Object getPrincipal() {
                        return principal;
                    }

                    @Override
                    public Object getCredentials() {
                        return principal.getPassword();
                    }
                };
                Subject currentUser = SecurityUtils.getSubject();
                currentUser.login(token);
                getUI().getNavigator().navigateTo(IticityUI.MAIN_VIEW);
            }
        });

        final TextField email = (TextField) binder.buildAndBind("Email", "email");
        email.addValidator(uniquePrincipalEmailValidator);
        email.setNullRepresentation(StringUtils.EMPTY);
        email.setValidationVisible(false);
        final PasswordField password = binder.buildAndBind("Password", "password",PasswordField.class);
        password.setNullRepresentation(StringUtils.EMPTY);
        password.setValidationVisible(false);
        final TextField firstName = (TextField) binder.buildAndBind("First name", "firstName");
        firstName.setNullRepresentation(StringUtils.EMPTY);
        firstName.setValidationVisible(false);
        final TextField lastName = (TextField) binder.buildAndBind("Last name", "lastName");
        lastName.setNullRepresentation(StringUtils.EMPTY);
        lastName.setValidationVisible(false);
        final TextField country = (TextField) binder.buildAndBind("Country", "country");
        country.setNullRepresentation(StringUtils.EMPTY);
        country.setValidationVisible(false);
        final TextField phone = (TextField) binder.buildAndBind("Phone", "phone");
        phone.setNullRepresentation(StringUtils.EMPTY);
        phone.setValidationVisible(false);
        final TextField company = (TextField) binder.buildAndBind("Company", "company");
        company.setNullRepresentation(StringUtils.EMPTY);
        company.setValidationVisible(false);

        final Button signUpButton = new Button("Sign Up");
        signUpButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
        signUpButton.addStyleName("window-form-button");
        signUpButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                try {
                    binder.commit();
                } catch (FieldGroup.CommitException e) {
                    email.setValidationVisible(true);
                    password.setValidationVisible(true);
                    firstName.setValidationVisible(true);
                    lastName.setValidationVisible(true);
                    phone.setValidationVisible(true);
                    country.setValidationVisible(true);
                    company.setValidationVisible(true);
                }
            }
        });

        addComponents(email,password,firstName,lastName,country,phone,company,signUpButton);
    }
}
