package pl.iticity.dbfds.service.common;

import pl.iticity.dbfds.model.common.Classification;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.service.ScopedService;

import java.util.List;

public interface ClassificationService extends ScopedService<Classification> {

    public List<Classification> findByDomain(Domain domain, boolean onlyActive);

    public List<Classification> findByDomain(Domain domain, boolean onlyActive, String without);

    public List<Classification> findByDomainForClassification(String domainId, boolean onlyActive, String forClassification);

    public List<Classification> addClassification(final Classification classification, String domainId);

    public List<Classification> toggleClassification(String id, boolean toggle);

    public boolean exists(String clId, String id, String domainId);

    public List<Classification> deleteClassification(String id);

    public List<Classification> findForProduct();

    public boolean isAssigned(Classification classification);
}
