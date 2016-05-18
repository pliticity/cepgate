package pl.iticity.dbfds.model.query;

import com.mysema.query.types.EntityPath;
import com.mysema.query.types.Path;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.path.StringPath;
import org.apache.shiro.SecurityUtils;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.SingleValueBinding;
import pl.iticity.dbfds.model.Domain;
import pl.iticity.dbfds.model.QDocumentInfo;
import pl.iticity.dbfds.model.QDomain;
import pl.iticity.dbfds.security.Principal;

import java.text.MessageFormat;

public class QDocumentInfoBinderCustomizer implements QuerydslBinderCustomizer<QDocumentInfo>{

    @Override
    public void customize(QuerydslBindings querydslBindings, QDocumentInfo entityPath) {
        querydslBindings.bind(entityPath.documentName).first(new SingleValueBinding<StringPath, String>() {
            @Override
            public Predicate bind(StringPath stringPath, String s) {
                return stringPath.like(MessageFormat.format("%{0}%",s));
            }
        });

        final Domain currentDomain = ((Principal)SecurityUtils.getSubject().getPrincipal()).getDomain();
        querydslBindings.bind(entityPath.domain()).first(new SingleValueBinding<QDomain, Domain>() {
            @Override
            public Predicate bind(QDomain qDomain, Domain domain) {
                return qDomain.eq(currentDomain);
            }
        });
    }
}
