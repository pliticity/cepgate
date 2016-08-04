package pl.iticity.dbfds.service.common;

import pl.iticity.dbfds.model.DocumentInfo;
import pl.iticity.dbfds.model.Link;
import pl.iticity.dbfds.model.Linkable;

import java.util.List;

public interface LinkService {

    public List<Link> createLink(String id,Class<? extends Linkable> clazz, DocumentInfo doc);

    public List<Link> deleteLink(String id,Class<? extends Linkable> clazz, Link link);

}
