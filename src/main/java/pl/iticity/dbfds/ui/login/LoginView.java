package pl.iticity.dbfds.ui.login;

import com.vaadin.ui.*;
import pl.iticity.dbfds.ui.AbstractView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

@Scope("prototype")
@org.springframework.stereotype.Component
public class LoginView extends AbstractView {

    private static final String CSS_BACKGROUND_BG = "background_bg";

    @Autowired
    SignInTab signInTab;

    @Autowired
    SignUpTab signUpTab;

    @Override
    public void initView() {
        addStyleName(CSS_BACKGROUND_BG);
        Component box = buildBox();

        addComponent(box);
        setComponentAlignment(box,Alignment.MIDDLE_CENTER);
    }

    @Override
    public void refreshDynamicContent() {
    }

    private Component buildBox(){
        TabSheet tabSheet = new TabSheet();
        tabSheet.setSizeUndefined();
        tabSheet.addStyleName("login-panel");
        tabSheet.addTab(signInTab,"Sign In");
        tabSheet.addTab(signUpTab,"Sign Up");
        return tabSheet;
    }

}
