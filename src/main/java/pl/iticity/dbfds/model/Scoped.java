package pl.iticity.dbfds.model;

import org.springframework.data.mongodb.core.mapping.DBRef;
import pl.iticity.dbfds.security.Principal;

import javax.validation.constraints.NotNull;

public abstract class Scoped extends BaseModel{

    @DBRef
    @NotNull
    protected Domain domain;

    @DBRef
    protected Principal principal;

    protected boolean removed;

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    public Principal getPrincipal() {
        return principal;
    }

    public void setPrincipal(Principal principal) {
        this.principal = principal;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }
}
