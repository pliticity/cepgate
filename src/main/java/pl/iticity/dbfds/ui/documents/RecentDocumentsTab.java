package pl.iticity.dbfds.ui.documents;

import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.iticity.dbfds.model.DocumentInfo;
import pl.iticity.dbfds.repository.DocumentInfoRepository;
import pl.iticity.dbfds.ui.Refreshable;
import pl.iticity.dbfds.util.PrincipalUtils;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by pmajchrz on 4/7/16.
 */
@Scope("prototype")
@Component
public class RecentDocumentsTab extends SearchDocumentsTab{

    @Autowired
    DocumentInfoRepository documentInfoRepository;

    @Override
    public List<DocumentInfo> getDocs() {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("Europe/Oslo"));
        c.add(Calendar.MONTH,-1);
        return documentInfoRepository.findByCreatedByAndLastActivity_dateAfter(PrincipalUtils.getCurrentPrincipal(),c.getTime());
    }
}
