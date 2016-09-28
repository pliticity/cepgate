package pl.iticity.dbfds.service.common;

import pl.iticity.dbfds.model.Linkable;
import pl.iticity.dbfds.model.Scoped;
import pl.iticity.dbfds.model.common.Link;
import pl.iticity.dbfds.model.common.LinkType;
import pl.iticity.dbfds.model.document.DocumentInformationCarrier;
import pl.iticity.dbfds.service.ScopedService;
import pl.iticity.dbfds.service.Service;

import java.util.List;
import java.util.Set;

public interface LinkService extends Service<Link>{

    public List<Link> createLink(String id, Class<? extends Linkable> clazz, String objectId, Class<? extends Scoped> objectClass, LinkType linkType);

    public List<Link> deleteLink(String id, Class<? extends Linkable> clazz, Link link);

    public Linkable getLinkObject(String id);

}
