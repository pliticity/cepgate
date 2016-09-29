package pl.iticity.dbfds.service.common;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pl.iticity.dbfds.model.Classification;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.repository.common.ClassificationRepository;
import pl.iticity.dbfds.security.AuthorizationProvider;
import pl.iticity.dbfds.security.Role;
import pl.iticity.dbfds.service.AbstractService;
import pl.iticity.dbfds.service.ScopedService;
import pl.iticity.dbfds.util.PrincipalUtils;

import javax.annotation.Nullable;
import java.util.List;

public interface ClassificationService extends ScopedService<Classification> {

    public List<Classification> findByDomain(Domain domain, boolean onlyActive);

    public List<Classification> findByDomain(Domain domain, boolean onlyActive, String without);

    public List<Classification> findByDomainForClassification(String domainId, boolean onlyActive, String forClassification);

    public List<Classification> addClassification(final Classification classification, String domainId);

    public List<Classification> toggleClassification(String id, boolean toggle);

    public boolean exists(String clId, String id, String domainId);

    public List<Classification> deleteClassification(String id);

    public List<Classification> findForModel(Domain domain,String model);

    public List<Classification> findForProduct();

    public boolean isAssigned(Classification classification);
}
