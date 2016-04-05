package pl.iticity.dbfds.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;
import pl.iticity.dbfds.model.Principal;
import pl.iticity.dbfds.repository.PrincipalRepository;
import pl.iticity.dbfds.ui.login.LoginView;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

/**
 * Created by dacho on 24.03.2016.
 */
@Title("DBFDS")
@Theme("iticity")
@Scope("prototype")
@SpringUI(path = "/*")
public class IticityUI extends UI {

    public static final String DEFAULT_VIEW = "";
    public static final String MAIN_VIEW = "mainView";

    @Autowired
    private LoginView loginView;

    @Autowired
    private MainView mainView;

    @Autowired
    PrincipalRepository principalRepository;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        initNavigator();
        Responsive.makeResponsive(this);
        Subject currentUser = SecurityUtils.getSubject();
        if(currentUser.isAuthenticated()){
            getNavigator().navigateTo(MAIN_VIEW);
        }else{
            setContent(loginView);
        }
    }

    private void initNavigator(){
        Navigator navigator = getNavigator();
        if(navigator==null){
            setNavigator(new Navigator(this,this));
            navigator = getNavigator();
        }
        navigator.addView(DEFAULT_VIEW, loginView);
        navigator.addView(MAIN_VIEW, mainView);
    }
}
