package pl.iticity.dbfds.ui.documents;

import com.vaadin.ui.VerticalLayout;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.iticity.dbfds.ui.Refreshable;

/**
 * Created by pmajchrz on 4/7/16.
 */
@Scope("prototype")
@Component
public class RecentDocumentsTab extends VerticalLayout implements Refreshable{



    @Override
    public void refresh() {

    }
}
