package pl.iticity.dbfds.ui;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FileResource;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.*;
import pl.iticity.dbfds.util.UIConstants;

import javax.annotation.PostConstruct;
import java.io.File;

/**
 * Created by dacho on 29.03.2016.
 */
public abstract class AbstractView extends AbsoluteLayout implements View {

    private static final String TITLE = "DBFDS";

    //private static final String BASE_PATH = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
    //private static final String LOGO_PATH = "/VAADIN/themes/iticity/img/logo.png";

    private static final String TOP_BAR_HEIGHT = "60px";
    private static final String CSS_TOP_BAR = "v-menubar";
    private static final String LOGO_HEIGHT = "50px";
    private static final String CSS_TITLE = "nickainley title_size";

    private VerticalLayout contentLayout;
    private Panel topBar;

    public AbstractView(){
        setSizeFull();
        setImmediate(true);
        addComponent(createTopBar(),"top: 0px;left: 0px;");
        addComponent(createContentLayout(),"left: 0px; top:"+TOP_BAR_HEIGHT+";");
    }

    @PostConstruct
    public abstract void initView();

    public abstract void refreshDynamicContent();


    private VerticalLayout createContentLayout(){
        contentLayout = new VerticalLayout();
        contentLayout.setSizeFull();
        return contentLayout;
    }

    private Panel createTopBar(){
        topBar = new Panel();
        topBar.setImmediate(true);

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSizeFull();
        horizontalLayout.setImmediate(true);
        horizontalLayout.setSpacing(true);
        horizontalLayout.setResponsive(true);
        topBar.setContent(horizontalLayout);
        topBar.setResponsive(true);

        topBar.setWidth(UIConstants.PERCENT_100);
        topBar.setHeight(TOP_BAR_HEIGHT);
        topBar.setPrimaryStyleName(CSS_TOP_BAR);

        HorizontalLayout leftRegion = new HorizontalLayout();
        leftRegion.setSizeFull();
        leftRegion.setResponsive(true);
        Responsive.makeResponsive(leftRegion);
        HorizontalLayout middleRegion = new HorizontalLayout();
        middleRegion.setResponsive(true);
        middleRegion.setSizeFull();
        Responsive.makeResponsive(middleRegion);
        HorizontalLayout rightRegion = new HorizontalLayout();
        rightRegion.setSizeFull();
        rightRegion.setResponsive(true);
        Responsive.makeResponsive(rightRegion);

        horizontalLayout.addComponent(leftRegion);
        horizontalLayout.setComponentAlignment(leftRegion,Alignment.MIDDLE_LEFT);
        horizontalLayout.setExpandRatio(leftRegion,0.4f);
        horizontalLayout.addComponent(middleRegion);
        horizontalLayout.setComponentAlignment(middleRegion,Alignment.MIDDLE_LEFT);
        horizontalLayout.setExpandRatio(middleRegion,0.2f);
        horizontalLayout.addComponent(rightRegion);
        horizontalLayout.setComponentAlignment(rightRegion,Alignment.MIDDLE_LEFT);
        horizontalLayout.setExpandRatio(rightRegion,0.4f);

        return topBar;
    }

    public VerticalLayout getContentLayout() {
        return contentLayout;
    }

    @Override
    public void addComponent(Component c) {
        getContentLayout().addComponent(c);
    }

    public void setComponentAlignment(Component component, Alignment alignment){
        getContentLayout().setComponentAlignment(component,alignment);
    }

    @Override
    public final void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        onEnter(viewChangeEvent);
        refreshDynamicContent();
    }

    public void onEnter(ViewChangeListener.ViewChangeEvent viewChangeEvent){}

    public Panel getTopBar() {
        return topBar;
    }

    public HorizontalLayout getTopBarLayout(){
        return (HorizontalLayout) getTopBar().getContent();
    }

    public HorizontalLayout getTopBarLeftLayout(){
        return (HorizontalLayout) getTopBarLayout().getComponent(0);
    }

    public HorizontalLayout getTopBarRightLayout(){
        return (HorizontalLayout) getTopBarLayout().getComponent(2);
    }
}
